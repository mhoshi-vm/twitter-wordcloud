package jp.vmware.tanzu.twitterwordcloud.library.observability;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WfRabbitMQSpans {

	private final String appName;

	public WfRabbitMQSpans(@Value("${app.name}") String appName) {
		this.appName = appName;
	}

	@Bean
	SpanHandler handlerRabbit() {
		return new SpanHandler() {
			@Override
			public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {

				System.out.println(span.kind().name());
				if (span.kind().name().equals("PRODUCER") || span.kind().name().equals("CONSUMER")) {
					span.tag("_outboundExternalService", "RabbitMQ");
					span.tag("_externalApplication", appName);
					span.tag("_externalComponent", "RabbitMQ");
				}
				return true;
			}
		};
	}

}
