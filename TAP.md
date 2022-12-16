# Using Tanzu Application Platform 
[Tanzu Application Platform](https://tanzu.vmware.com/application-platform) can ease the configuration by using service bindings.

With services toolkit create the following class

```
% tanzu service class list
  NAME      DESCRIPTION
  appsso    It's a SSO service!
  postgres  It's a Postgres cluster!
  rabbitmq  It's a RabbitMQ cluster!
  secrets   It's a set of Secrets!
```

Perform the claim.  `--resource-name`,`--resource-kind`,`--resource-api-version`,`--resource-namespace` will be different on all env。

```
tanzu services claim create rmq-claim --resource-name rmq-1 --resource-kind RabbitmqCluster --resource-api-version rabbitmq.com/v1beta1 --resource-namespace service-instances
tanzu services claim create twitter-claim --resource-name production-twitter --resource-kind Secret --resource-api-version v1 --resource-namespace service-instances
tanzu services claim create wavefront-claim --resource-name production-wavefront --resource-kind Secret --resource-api-version v1 --resource-namespace service-instances
tanzu services claim create postgres-claim --resource-name postgres-11 --resource-kind Postgres --resource-api-version sql.tanzu.vmware.com/v1 --resource-namespace service-instances
tanzu services claim create gemfire-claim --resource-name gemfire-redis1 --resource-kind Secret --resource-api-version v1 --resource-namespace service-instances
tanzu services claim create sso-claim --resource-name basic-client-registration --resource-kind ClientRegistration --resource-api-version sso.apps.tanzu.vmware.com/v1alpha1 --resource-namespace service-instances
```

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
    --build-env "BP_JVM_VERSION=17" \
    --build-env "BP_MAVEN_BUILT_MODULE=wordcloud" \
    --build-env BP_MAVEN_BUILD_ARGUMENTS="-pl wordcloud -am -P modelviewcontroller package" \
    --env "SERVICE_NAME=mvc" \
    --env "JAVA_TOOL_OPTIONS=-Dmanagement.health.probes.enabled='false'" \
    --annotation autoscaling.knative.dev/minScale=1 \
    --param-yaml buildServiceBindings='[{"name": "bucketrepo-settings-xml", "kind": "Secret"}]' \
    --git-repo https://github.com/mhoshi-vm/twitter-wordcloud \
    --git-branch staging
```

Run the twitter api client app in the following way.

```
RESOURCE_CLAIM="services.apps.tanzu.vmware.com/v1alpha1:ResourceClaim"
tanzu apps workload apply twitter-api-client \
    --type server \
    --label app.kubernetes.io/part-of=wordcloud \
    --service-ref "rabbitmq=${RESOURCE_CLAIM}:rmq-claim" \
    --service-ref "twitter=${RESOURCE_CLAIM}:twitter-claim" \
    --service-ref "wavefront=${RESOURCE_CLAIM}:wavefront-claim" \
    --build-env "BP_MAVEN_BUILT_MODULE=wordcloud" \
    --build-env "BP_JVM_VERSION=17" \
    --build-env BP_MAVEN_BUILD_ARGUMENTS="-pl wordcloud -am -P twitterapiclient package" \
    --env "SERVICE_NAME=twitterclient" \
    --env "JAVA_TOOL_OPTIONS=-Dmanagement.health.probes.enabled='false'" \
    --git-repo https://github.com/mhoshi-vm/twitter-wordcloud \
    --git-branch staging
```
