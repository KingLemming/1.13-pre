package cofh.thermal.cultivation.plugins.jei;

import cofh.thermal.core.plugins.jei.JEIPluginTSeries;
import cofh.thermal.cultivation.plugins.jei.dynamo.gourmand.GourmandFuelCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEIPluginTC implements IModPlugin {

	public JEIPluginTC() {

		JEIPluginTSeries.addCategory(new GourmandFuelCategory(GOURMAND));
	}

	public static final String GOURMAND = "thermal:dynamo_gourmand";
}
