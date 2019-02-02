package cofh.thermal.expansion.plugins.jei;

import cofh.thermal.core.plugins.jei.JEIPluginTSeries;
import cofh.thermal.expansion.plugins.jei.machine.furnace.FurnaceRecipeCategory;
import cofh.thermal.expansion.plugins.jei.machine.pulverizer.PulverizerRecipeCategory;
import cofh.thermal.expansion.plugins.jei.machine.sawmill.SawmillRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEIPluginTE implements IModPlugin {

	public JEIPluginTE() {

		JEIPluginTSeries.addCategory(new FurnaceRecipeCategory(FURNACE));
		JEIPluginTSeries.addCategory(new PulverizerRecipeCategory(PULVERIZER));
		JEIPluginTSeries.addCategory(new SawmillRecipeCategory(SAWMILL));
	}

	public static final String FURNACE = "thermal:machine_furnace";
	public static final String PULVERIZER = "thermal:machine_pulverizer";
	public static final String SAWMILL = "thermal:machine_sawmill";

}
