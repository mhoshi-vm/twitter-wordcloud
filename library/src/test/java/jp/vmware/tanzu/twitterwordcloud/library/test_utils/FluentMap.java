package jp.vmware.tanzu.twitterwordcloud.library.test_utils;

import java.util.HashMap;

public final class FluentMap extends HashMap<String, String> {

	public FluentMap withEntry(String key, String value) {
		put(key, value);
		return this;
	}

}