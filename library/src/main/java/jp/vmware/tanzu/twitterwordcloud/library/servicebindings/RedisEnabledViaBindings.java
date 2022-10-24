package jp.vmware.tanzu.twitterwordcloud.library.servicebindings;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class RedisEnabledViaBindings implements BindingsPropertiesProcessor {

	public static final String TYPE = "redis";

	@Override
	public void process(Environment environment, Bindings bindings, Map<String, Object> map) {

		List<Binding> redisBindings = bindings.filterBindings(TYPE);
		if (redisBindings.size() == 0) {
			return;
		}
		map.put("spring.session.store-type", "redis");
		map.put("management.health.redis.enabled", "true");
	}

}
