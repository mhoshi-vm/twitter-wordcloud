package jp.vmware.tanzu.twitterwordcloud.library.observability;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WfRedisSpans {

	private final String appName;

	public WfRedisSpans(@Value("${app.name}") String appName) {
		this.appName = appName;
	}

	@Bean
	SpanHandler handlerRedis() {
		return new SpanHandler() {
			@Override
			public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {

				if (span.remoteServiceName().equals("redis")) {
					span.tag("_outboundExternalService", "Redis");
					span.tag("_externalApplication", appName);
					span.tag("_externalComponent", "Redis");
				}
				return true;
			}
		};
	}

}