package jp.vmware.tanzu.twitterwordclouddemo.system.spans.utils;

import brave.TracingCustomizer;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestSpanHolder {

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
    TracingCustomizer tracingCustomizer() {
        return t -> t.traceId128Bit(true).supportsJoin(false).addSpanHandler(new SpanHandler() {
            @Override
            public boolean end(TraceContext traceContext, MutableSpan span, SpanHandler.Cause cause) {
                addSpans(span);
                return false; // Dump all span
            }
        });
    }

}