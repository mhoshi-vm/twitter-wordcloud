package jp.vmware.tanzu.twitterwordcloud.library.observability;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WfDBSpans {

	public final String dbType;

	public final String dbInstance;

	public WfDBSpans(@Value("${db.type:localdb}") String dbType, @Value("${db.instance:local}") String dbInstance) {
		this.dbType = dbType;
		this.dbInstance = dbInstance;
	}

	@Bean
	SpanHandler handlerDB() {
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
				return true;
			}
		};
	}

}
