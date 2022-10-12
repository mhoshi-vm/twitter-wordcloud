package jp.vmware.tanzu.twitterwordcloud.library.servicebindings;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class PostgresEnabledViaBindings implements BindingsPropertiesProcessor {

	public static final String TYPE = "postgresql";

	@Override
	public void process(Environment environment, Bindings bindings, Map<String, Object> map) {

		List<Binding> rmqBindings = bindings.filterBindings(TYPE);
		System.out.println(environment.getActiveProfiles());
		if (rmqBindings.size() > 0) {
			map.put("db.type", "postgresql");
		}

	}

}
