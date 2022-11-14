package jp.vmware.tanzu.twitterwordcloud.library.observability;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WfServiceSpans {

	public final String dbType;

	public final String dbInstance;

	public final String appName;

	public WfServiceSpans(@Value("${db.type:localdb}") String dbType, @Value("${db.instance:local}") String dbInstance,
			@Value("${app.name}") String appName) {
		this.dbType = dbType;
		this.dbInstance = dbInstance;
		this.appName = appName;
	}

	@Bean
	SpanHandler handlerOne() {
		return new SpanHandler() {
			@Override
			public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {
				for (int i = 0; i < span.tagCount(); i++) {
					if (span.tagKeyAt(i).equals("jdbc.query")) {
						span.tag("component", "java-jdbc");
						span.tag("db.type", dbType);
						span.tag("db.instance", dbInstance);
					}
				}

				if (span.remoteServiceName().equals("redis")) {
					span.tag("_outboundExternalService", "Redis");
					span.tag("_externalApplication", appName);
					span.tag("_externalComponent", "Redis");
				}
				else if (span.remoteServiceName().equals("RabbitMQ")) {
					span.tag("_outboundExternalService", "RabbitMQ");
					span.tag("_externalApplication", appName);
					span.tag("_externalComponent", "RabbitMQ");
				}
				return true;

			}
		};
	}

}
