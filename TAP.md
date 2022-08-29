# Tanzu Application Platform での起動

[Tanzu Application Platform](https://tanzu.vmware.com/application-platform)の場合 Service Bindings 機能を使い、デプロイを簡単にすることができます。

事前に以下のように、Services Toolkit を使い、以下のようにそれぞれ、サービスがResourceClaimとして扱えるようにします。


```
% tanzu service class list
  NAME      DESCRIPTION
  appsso    It's a SSO service!
  postgres  It's a Postgres cluster!
  rabbitmq  It's a RabbitMQ cluster!
  secrets   It's a set of Secrets!
```

以下のようにサービスをクレームします。なお `--resource-name`,`--resource-kind`,`--resource-api-version`,`--resource-namespace` にはいる値は環境ごとに異なります。

```
tanzu services claim create rmq-claim --resource-name rmq-1 --resource-kind RabbitmqCluster --resource-api-version rabbitmq.com/v1beta1 --resource-namespace service-instances
tanzu services claim create twitter-claim --resource-name production-twitter --resource-kind Secret --resource-api-version v1 --resource-namespace service-instances
tanzu services claim create wavefront-claim --resource-name production-wavefront --resource-kind Secret --resource-api-version v1 --resource-namespace service-instances
tanzu services claim create postgres-claim --resource-name postgres-11 --resource-kind Postgres --resource-api-version sql.tanzu.vmware.com/v1 --resource-namespace service-instances
tanzu services claim create sso-claim --resource-name basic-client-registration --resource-kind ClientRegistration --resource-api-version sso.apps.tanzu.vmware.com/v1alpha1 --resource-namespace service-instances
```


以下のようにまずステートレスモードを記載します。

```
RESOURCE_CLAIM="services.apps.tanzu.vmware.com/v1alpha1:ResourceClaim"
tanzu apps workload apply twitter-demo \
    --type web \
    --label app.kubernetes.io/part-of=twitter-demo \
    --service-ref "sso=${RESOURCE_CLAIM}:sso-claim" \
    --service-ref "postgres=${RESOURCE_CLAIM}:postgres-claim" \
    --service-ref "rabbitmq=${RESOURCE_CLAIM}:rmq-claim" \
    --service-ref "wavefront=${RESOURCE_CLAIM}:wavefront-claim" \
    --env SPRING_PROFILES_ACTIVE=stateless \
    --git-repo https://github.com/tanzu-japan/twitter-wordcloud-demo \
    --git-branch staging
```

ステートフルモードを`--image=` で指定して起動を行う。

```
RESOURCE_CLAIM="services.apps.tanzu.vmware.com/v1alpha1:ResourceClaim"
IMAGE_TAG=`kubectl get images.kpack.io -n tap-demo -o template --template={{.spec.tag}} twitter-demo`
tanzu apps workload apply twitter-demo-stateful \
    --type tcp \
    --label app.kubernetes.io/part-of=twitter-demo-stateful \
    --service-ref "rabbitmq=${RESOURCE_CLAIM}:rmq-claim" \
    --service-ref "twitter=${RESOURCE_CLAIM}:twitter-claim" \
    --service-ref "wavefront=${RESOURCE_CLAIM}:wavefront-claim" \
    --env SPRING_PROFILES_ACTIVE=stateful \
    --image=${IMAGE_TAG}:latest
```
