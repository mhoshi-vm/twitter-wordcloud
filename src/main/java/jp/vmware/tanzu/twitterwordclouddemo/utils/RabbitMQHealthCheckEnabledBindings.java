package jp.vmware.tanzu.twitterwordclouddemo.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class RabbitMQHealthCheckEnabledBindings implements BindingsPropertiesProcessor {
    public static final String TYPE = "rabbitmq";

    @Override
    public void process(@NotNull Environment environment, Bindings bindings, @NotNull Map<String, Object> map) {

        List<Binding> rmqBindings = bindings.filterBindings(TYPE);
        if (rmqBindings.size() > 1) {
            map.put("management.health.rabbit.enabled", "true");
        }

    }
}
