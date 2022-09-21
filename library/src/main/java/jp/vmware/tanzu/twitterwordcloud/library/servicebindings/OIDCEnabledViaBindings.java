package jp.vmware.tanzu.twitterwordcloud.library.servicebindings;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class OIDCEnabledViaBindings implements BindingsPropertiesProcessor {

	public static final String TYPE = "oauth2";

	@Override
	public void process(Environment environment, Bindings bindings, Map<String, Object> map) {

		List<Binding> oauth2Bindings = bindings.filterBindings(TYPE);
		if (oauth2Bindings.size() > 0) {
			map.put("oauth2.enabled", "true");
		}

	}

}
