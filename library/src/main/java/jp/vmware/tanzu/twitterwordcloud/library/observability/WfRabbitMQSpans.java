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

				if (span.remoteServiceName().equals("rabbitmq")) {
					span.tag("_outboundExternalService", "RabbitMQ");
					span.tag("_externalApplication", appName);
					span.tag("_externalComponent", "RabbitMQ");
				}
				return true;
			}
		};
	}

}
