package jp.vmware.tanzu.twitterwordcloud.library.observability;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WfRedisSpans {

	private final Tracer tracer;

	private final String appName;

	public WfRedisSpans(Tracer tracer, @Value("${app.name}") String appName) {
		this.tracer = tracer;
		this.appName = appName;
	}

	@Bean
	SpanHandler handlerTwo() {
		return new SpanHandler() {
			@Override
			public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {
				for (int i = 0; i < span.tagCount(); i++) {
					if (span.tagKeyAt(i).startsWith("session.")) {
						span.tag("_outboundExternalService", "session-cache");
						span.tag("_externalApplication", appName);
						span.tag("_externalComponent", "session-cache");
					}
				}
				return true;
			}
		};
	}

}
