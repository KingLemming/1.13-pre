package cofh.thermal.expansion.util.parsers.machine;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.machine.InsolatorRecipeManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class InsolatorRecipeParser extends AbstractContentParser {

	private static final InsolatorRecipeParser INSTANCE = new InsolatorRecipeParser();

	public static InsolatorRecipeParser instance() {

		return INSTANCE;
	}

	private InsolatorRecipeParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		ArrayList<ItemStack> output = new ArrayList<>();
		ArrayList<Float> chance = new ArrayList<>();
		int energy = InsolatorRecipeManager.instance().getDefaultEnergy();

		/* INPUT */
		input = parseItemStack(object.get(INPUT));

		/* REMOVAL */
		if (object.has(REMOVE) && object.get(REMOVE).getAsBoolean()) {
			removeQueue.add(input);
			return;
		}

		/* OUTPUT */
		parseItemStacks(output, chance, object.get(OUTPUT));

		/* ENERGY */
		if (object.has(ENERGY)) {
			energy = object.get(ENERGY).getAsInt();
		}
		if (object.has(ENERGY_MOD)) {
			energy *= object.get(ENERGY_MOD).getAsFloat();
		}

		/* ORE RECIPES */
		if (object.has(TYPE)) {
			switch (object.get(TYPE).getAsString()) {
				case RECIPE_TYPE_PLANT:
					for (int i = 0; i < output.size(); i++) {
						if (output.get(i).getCount() > 1) {
							chance.set(i, chance.get(i) * output.get(i).getCount());
							output.get(i).setCount(1);
						}
						// If ore recipe, no chances are locked.
						chance.set(i, Math.abs(chance.get(i)));
					}
					InsolatorRecipeManager.instance().addPlantRecipe(energy, input, output, chance);
					return;
				case RECIPE_TYPE_PLANT_DEFAULT:
					for (int i = 0; i < output.size(); i++) {
						if (output.get(i).getCount() > 1) {
							chance.set(i, chance.get(i) * output.get(i).getCount());
							output.get(i).setCount(1);
						}
						// If ore recipe, no chances are locked.
						chance.set(i, Math.abs(chance.get(i)));
					}
					// If default-plant recipe, use the configured plant multiplier.
					chance.set(0, InsolatorRecipeManager.instance().getPlantMultiplier());
					InsolatorRecipeManager.instance().addPlantRecipe(energy, input, output, chance);
					return;
				default:
			}
		}
		InsolatorRecipeManager.instance().addRecipe(energy, input, output, chance);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			InsolatorRecipeManager.instance().removeRecipe(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();

}
