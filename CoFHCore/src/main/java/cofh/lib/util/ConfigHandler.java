package cofh.lib.util;

import net.minecraftforge.common.config.Configuration;

/**
 * Configuration wrapper to make config/migration easier.
 */
public class ConfigHandler {

	private final Configuration config;

	public ConfigHandler(Configuration config) {

		this.config = config;
	}

	public void load() {

		this.config.load();
	}

	public void save() {

		this.config.save();
	}

	public boolean get(String name, String category, boolean value) {

		return config.get(category, name, value).getBoolean();
	}

	public boolean get(String name, String category, boolean defaultValue, String comment) {

		return config.getBoolean(name, category, defaultValue, comment);
	}

	public int get(String name, String category, int value) {

		return config.get(category, name, value).getInt();
	}

	public int get(String name, String category, int defaultValue, int minValue, int maxValue, String comment) {

		return config.getInt(name, category, defaultValue, minValue, maxValue, comment);
	}

	public float get(String name, String category, float value) {

		return (float) config.get(category, name, value).getDouble();
	}

	public float get(String name, String category, float defaultValue, float minValue, float maxValue, String comment) {

		return config.getFloat(name, category, defaultValue, minValue, maxValue, comment);
	}

}
