# Using Tanzu Application Platform 
[Tanzu Application Platform](https://tanzu.vmware.com/application-platform) can ease the configuration by using service bindings.

## Prerequisite

Additional to TAP itself, users need to prepare the following
- Tanzu RabbitMQ Operator
- Tanzu Postgres Operator
- Tanzu Gemfire Operator
- Configure AppSSO
- Configure ResourceClaims via Tanzu Toolkit

## Installing Prerequisite (for non Prod / Demo env Only)

For simplicity all the above can be configured using the following.

> :warning: The following script has no warranty and is defaulted towards quick start thus should never be used for production.

Install the following package

```
tanzu package repository add tap-carvel-tools \
  --url ghcr.io/mhoshi-vm/tap-carvel:latest \
  --namespace tap-install
```

Verify the available version. 
> :warning: Please choose `TAP_TOOLKIT_VERSION =< TAP_VERSION.` version 1.3.4 or higher is recommended

```
% tanzu package available list tap-toolkit-starter.tanzu.japan.com

  NAME                                 VERSION  RELEASED-AT
  tap-toolkit-starter.tanzu.japan.com  1.2.0    0001-01-01 00:00:00 +0000 UTC
  tap-toolkit-starter.tanzu.japan.com  1.3.0    0001-01-01 00:00:00 +0000 UTC
  tap-toolkit-starter.tanzu.japan.com  1.3.2    0001-01-01 00:00:00 +0000 UTC
  tap-toolkit-starter.tanzu.japan.com  1.3.4    0001-01-01 00:00:00 +0000 UTC
```

Review the default values

```
export VERSION=<TOOLKIT_VERSION>
% tanzu package available get tap-toolkit-starter.tanzu.japan.com/${VERSION} --values-schema
```


Install the TAP toolkit starter. The following is the minimum value based on default TAP settings.

```
export VERSION=<TOOLKIT_VERSION>
cat <<EOF > values.yaml
sso:
 redirect_urls:
 - http://wordcloud.<developernamespace>.<shared-domain>/login/oauth2/code/sso
EOF
tanzu package install tap-toolkit -p tap-toolkit-starter.tanzu.japan.com -v ${VERSION} --values-file values.yaml -n tap-install
```

After executing, review the services toolkit create the following class

```
% tanzu service class list
  NAME      DESCRIPTION
  appsso    It's a SSO service!
  gemfire   It's a Gemfire cluster !
  postgres  It's a Postgres cluster!
  rabbitmq  It's a RabbitMQ cluster!
  secrets   It's a set of Secrets!
```

## Claim Resource

Define a twitter secret

```
cat <<EOF | kubectl apply -f-
apiVersion: v1
kind: Secret
metadata:
  name: production-twitter
  namespace: service-instances
  labels:
    claimable: "true"
type: servicebinding.io/twitter
stringData:
  type: twitter
  bearer-token: <TWITTER_BEARER_TOKEN>
EOF
```

Define wavefront api endpoint

```
cat <<EOF | kubectl apply -f-
apiVersion: v1
kind: Secret
metadata:
  name: production-wavefront
  labels:
    claimable: "true"
type: servicebinding.io/wavefront
stringData:
  type: wavefront
  api-token: <WAVEFRONT_API_TOKEN>
  uri: https://longboard.wavefront.com
EOF
```

**From developer namespace (if required change via `kubectl config set-context --current --namespace=<developer_namespace>`)** perform the claim.  
```
tanzu services claim create rmq-claim --resource-name rmq-1 --resource-kind RabbitmqCluster --resource-api-version rabbitmq.com/v1beta1 --resource-namespace service-instances
tanzu services claim create twitter-claim --resource-name production-twitter --resource-kind Secret --resource-api-version v1 --resource-namespace service-instances
tanzu services claim create wavefront-claim --resource-name production-wavefront --resource-kind Secret --resource-api-version v1 --resource-namespace service-instances
tanzu services claim create postgres-claim --resource-name postgres-11 --resource-kind Postgres --resource-api-version sql.tanzu.vmware.com/v1 --resource-namespace service-instances
tanzu services claim create gemfire-claim --resource-name gemfire-redis1 --resource-kind Secret --resource-api-version v1 --resource-namespace service-instances
tanzu services claim create sso-claim --resource-name basic-client-registration --resource-kind ClientRegistration --resource-api-version sso.apps.tanzu.vmware.com/v1alpha1 --resource-namespace service-instances
```

## Run the app


Run the mvc-app in the following way.

```
RESOURCE_CLAIM="services.apps.tanzu.vmware.com/v1alpha1:ResourceClaim"
tanzu apps workload apply wordcloud \
    --type web \
    --label app.kubernetes.io/part-of=wordcloud \
    --label apis.apps.tanzu.vmware.com/register-api=true \
    --param-yaml api_descriptor='{"description":"Twitter Wordcloud","location":{"path":"/v3/api-docs"},"owner":"demo","system":"dev","type":"openapi"}' \
    --service-ref "sso=${RESOURCE_CLAIM}:sso-claim" \
    --service-ref "postgres=${RESOURCE_CLAIM}:postgres-claim" \
    --service-ref "rabbitmq=${RESOURCE_CLAIM}:rmq-claim" \
    --service-ref "redis=${RESOURCE_CLAIM}:gemfire-claim" \
    --service-ref "wavefront=${RESOURCE_CLAIM}:wavefront-claim" \
    --param "clusterBuilder=base-jammy" \
    --build-env "BP_MAVEN_BUILT_MODULE=wordcloud" \
    --build-env BP_MAVEN_BUILD_ARGUMENTS="-pl wordcloud -am -P modelviewcontroller package" \
    --env "SERVICE_NAME=mvc" \
    --env "JAVA_TOOL_OPTIONS=-Dmanagement.health.probes.enabled='false'" \
    --annotation autoscaling.knative.dev/minScale=1 \
    --git-repo https://github.com/mhoshi-vm/twitter-wordcloud \
    --git-branch springboot2
```

Run the Twitter api client app in the following way.

```
RESOURCE_CLAIM="services.apps.tanzu.vmware.com/v1alpha1:ResourceClaim"
tanzu apps workload apply twitter-api-client \
    --type server \
    --label app.kubernetes.io/part-of=twitter-demo \
    --service-ref "rabbitmq=${RESOURCE_CLAIM}:rmq-claim" \
    --service-ref "twitter=${RESOURCE_CLAIM}:twitter-claim" \
    --service-ref "wavefront=${RESOURCE_CLAIM}:wavefront-claim" \
    --build-env "BP_MAVEN_BUILT_MODULE=wordcloud" \
    --build-env BP_MAVEN_BUILD_ARGUMENTS="-pl wordcloud -am -P twitterapiclient package" \
    --env "SERVICE_NAME=twitterclient" \
    --env "JAVA_TOOL_OPTIONS=-Dmanagement.health.probes.enabled='false'" \
    --git-repo https://github.com/mhoshi-vm/twitter-wordcloud \
    --git-branch springboot2
```
