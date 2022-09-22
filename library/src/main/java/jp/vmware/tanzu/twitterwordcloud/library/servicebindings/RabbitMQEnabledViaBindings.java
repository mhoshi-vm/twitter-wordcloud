package jp.vmware.tanzu.twitterwordcloud.library.servicebindings;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class RabbitMQEnabledViaBindings implements BindingsPropertiesProcessor {

	public static final String TYPE = "rabbitmq";

	@Override
	public void process(Environment environment, Bindings bindings, Map<String, Object> map) {

		List<Binding> rmqBindings = bindings.filterBindings(TYPE);
		if (rmqBindings.size() > 0) {
			map.put("message.queue.enabled", "true");
			map.put("log.mode", "false");
			map.put("management.health.rabbit.enabled", "true");
		}

	}

}
