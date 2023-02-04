package jp.vmware.tanzu.twitterwordcloud.library.observability;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WfServiceSpans {

	Logger logger = LoggerFactory.getLogger(WfServiceSpans.class);

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
	SpanHandler spanServiceHandler() {
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

				String remoteService = span.remoteServiceName();

				if (remoteService != null) {
					if (remoteService.equals("redis")) {
						span.tag("_outboundExternalService", "Redis");
						span.tag("_externalApplication", appName);
						span.tag("_externalComponent", "Redis");
					}
					else if (remoteService.equals("rabbitmq")) {
						span.tag("_outboundExternalService", "RabbitMQ");
						span.tag("_externalApplication", appName);
						span.tag("_externalComponent", "RabbitMQ");
					}
				}

				return true;
			}
		};
	}

	/*
	 * for debugging
	 *
	 * @Bean SpanHandler logHandler() { return new SpanHandler() {
	 *
	 * @Override public boolean end(TraceContext traceContext, MutableSpan span, Cause
	 * cause) {
	 *
	 * logger.info("Span name : " + span.name()); logger.info("Span kind : " +
	 * span.kind()); logger.info("Span Remote Source :" + span.remoteServiceName());
	 * span.tags().forEach((key, value) -> logger.info("     tag :" + key + " value: " +
	 * value)); return true;
	 *
	 * } }; }
	 */

}
