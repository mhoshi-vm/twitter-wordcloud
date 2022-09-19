package jp.vmware.tanzu.twitterwordcloud.library.servicebindings;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class RabbitMQHealthCheckEnabledBindings implements BindingsPropertiesProcessor {

	public static final String TYPE = "rabbitmq";

	@Override
	public void process(Environment environment, Bindings bindings, Map<String, Object> map) {

		List<Binding> rmqBindings = bindings.filterBindings(TYPE);
		if (rmqBindings.size() > 1) {
			map.put("management.health.rabbit.enabled", "true");
		}

	}

}
