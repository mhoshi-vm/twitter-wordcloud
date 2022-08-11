package jp.vmware.tanzu.twitterwordclouddemo.observability.utils;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestSpanHolder {

	Logger logger = LoggerFactory.getLogger(TestSpanHolder.class);

	List<MutableSpan> spans;

	public TestSpanHolder() {
		this.spans = new ArrayList<>();
	}

	public List<MutableSpan> getSpans() {
		return spans;
	}

	public void addSpans(MutableSpan span) {
		spans.add(span);
	}

	@Bean
	SpanHandler spanHolder() {
		return new SpanHandler() {
			@Override
			public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {
				logger.info("Span name : " + span.name());
				span.tags().entrySet().forEach(stringStringEntry -> logger
						.info("     tag :" + stringStringEntry.getKey() + " value: " + stringStringEntry.getValue()));

				addSpans(span);
				return true;
			}
		};
	}

}