package jp.vmware.tanzu.twitterwordcloud.library.servicebindings;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class WfEnableOnlyViaBindings implements BindingsPropertiesProcessor {

	public static final String TYPE = "wavefront";

	@Override
	public void process(Environment environment, Bindings bindings, Map<String, Object> map) {

		List<Binding> wfBindings = bindings.filterBindings(TYPE);
		if (wfBindings.size() == 0) {
			map.put("management.wavefront.metrics.export.enabled", "false");
		}
		else {
			map.put("management.wavefront.metrics.export.enabled", "true");
		}

	}

}
