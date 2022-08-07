package jp.vmware.tanzu.twitterwordclouddemo.config.spans;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WfSpanCustomizer {

	private final Tracer tracer;

	private final String appName;

	public WfSpanCustomizer(Tracer tracer, @Value("${wavefront.application.name:none}") String appName) {
		this.tracer = tracer;
		this.appName = appName;
	}

	@After("execution(* jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamHandler.tweetHandler(String))")
	public void addTwitterSpanTags() {
		Span span = tracer.currentSpan();
		if (span != null) {
			span.tag("_outboundExternalService", "Twitter");
			span.tag("_externalApplication", appName);
			span.tag("_externalComponent", "tweets");
		}
	}

}