package com.ceteva.mosaic.util;

import java.util.Properties;

/**
 * Same as java.util.Properties, but allows to query property values in a case-
 * insensitive manner.
 * 
 * @author Jens Gulden
 */
public class PropertiesCaseInsensitive extends Properties {

	public PropertiesCaseInsensitive() {
		super();
	}

	public PropertiesCaseInsensitive(Properties defaults) {
		super(defaults);
	}
	
	@Override
	public String getProperty(String key) {
		return (String)this.get(key); // make sure superclass doesn't do anything else
	}
	
	/**
	 * Get value associated to key, key is treated case-insensitive.
	 * @param key case-insensitive entry name
	 * @return value associated to the entry, or null if no value set
	 */
	@Override
	public Object get(Object key) {
		if (key instanceof String) {
			// find case-sensitive key name
			String keyInsens = (String)key;
			String keySens = null;
			for (Object k : this.keySet()) {
				if (k instanceof String) {
					if (((String)k).equalsIgnoreCase(keyInsens)) {
						keySens = (String)k;
					}
				}
			}
			if (keySens != null) {
				return super.get(keySens); // get case-sensitive
			}
		}
		return super.get(key); // default fallback
	}

}
