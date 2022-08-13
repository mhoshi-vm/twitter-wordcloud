SOURCE_IMAGE = os.getenv("SOURCE_IMAGE", default='your-registry.io/library/twitter-demo-deploy')
LOCAL_PATH = os.getenv("LOCAL_PATH", default='.')
NAMESPACE = os.getenv("NAMESPACE", default='default')
TWITTER_BEARER_TOKEN = os.getenv("TWITTER_BEARER_TOKEN", default='my-bearer-token')

k8s_custom_deploy(
    'twitter-demo-deploy',
    apply_cmd="tanzu apps workload apply twitter-demo-deploy --live-update=true" +
               " --local-path " + LOCAL_PATH +
               " --source-image " + SOURCE_IMAGE +
               " --env TWITTER_BEARER_TOKEN=" + TWITTER_BEARER_TOKEN +
               " --type web" +
               " --namespace " + NAMESPACE +
               " --yes >/dev/null" +
               " && kubectl get workload twitter-demo-deploy --namespace " + NAMESPACE + " -o yaml",
    delete_cmd="tanzu apps workload delete twitter-demo-deploy --namespace " + NAMESPACE + " --yes",
    deps=['pom.xml', './target/classes'],
    container_selector='workload',
    live_update=[
      sync('./target/classes', '/workspace/BOOT-INF/classes')
    ]
)

k8s_resource('twitter-demo-deploy', port_forwards=["8080:8080"],
            extra_pod_selectors=[{'serving.knative.dev/service': 'twitter-demo-deploy'}])