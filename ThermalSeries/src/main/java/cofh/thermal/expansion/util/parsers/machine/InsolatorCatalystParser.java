package cofh.thermal.expansion.util.parsers.machine;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.machine.InsolatorRecipeManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class InsolatorCatalystParser extends AbstractContentParser {

	private static final InsolatorCatalystParser INSTANCE = new InsolatorCatalystParser();

	public static InsolatorCatalystParser instance() {

		return INSTANCE;
	}

	private InsolatorCatalystParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		float primaryMod = 1.0F;
		float secondaryMod = 1.0F;
		float energyMod = 1.0F;
		float minChance = 0.0F;
		float useChance = 1.0F;

		/* INPUT */
		input = parseItemStack(object.get(INPUT));

		if (object.has(PRIMARY_MOD)) {
			primaryMod = object.get(PRIMARY_MOD).getAsFloat();
		}
		if (object.has(SECONDARY_MOD)) {
			secondaryMod = object.get(SECONDARY_MOD).getAsFloat();
		}
		if (object.has(ENERGY_MOD)) {
			energyMod = object.get(ENERGY_MOD).getAsFloat();
		}
		if (object.has(MIN_CHANCE)) {
			minChance = object.get(MIN_CHANCE).getAsFloat();
		}
		if (object.has(USE_CHANCE)) {
			useChance = object.get(USE_CHANCE).getAsFloat();
		}

		InsolatorRecipeManager.instance().addCatalyst(input, primaryMod, secondaryMod, energyMod, minChance, useChance);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			InsolatorRecipeManager.instance().removeCatalyst(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();

}
