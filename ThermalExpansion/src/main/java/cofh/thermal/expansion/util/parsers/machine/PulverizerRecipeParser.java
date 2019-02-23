package cofh.thermal.expansion.util.parsers.machine;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.machine.PulverizerRecipeManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Set;

import static cofh.lib.util.Constants.BASE_CHANCE_LOCKED;

public class PulverizerRecipeParser extends AbstractContentParser {

	private static final PulverizerRecipeParser INSTANCE = new PulverizerRecipeParser();

	public static PulverizerRecipeParser instance() {

		return INSTANCE;
	}

	private PulverizerRecipeParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		ArrayList<ItemStack> output = new ArrayList<>();
		ArrayList<Float> chance = new ArrayList<>();
		int energy = PulverizerRecipeManager.instance().getDefaultEnergy();

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
				case RECIPE_TYPE_ORE:
					for (int i = 0; i < output.size(); i++) {
						if (output.get(i).getCount() > 1) {
							chance.set(i, chance.get(i) * output.get(i).getCount());
							output.get(i).setCount(1);
						}
					}
					PulverizerRecipeManager.instance().addOreRecipe(energy, input, output, chance);
					return;
				case RECIPE_TYPE_ORE_DEFAULT:
					for (int i = 0; i < output.size(); i++) {
						if (output.get(i).getCount() > 1) {
							chance.set(i, chance.get(i) * output.get(i).getCount());
							output.get(i).setCount(1);
						}
						chance.set(0, PulverizerRecipeManager.instance().getOreMultiplier());
					}
					PulverizerRecipeManager.instance().addOreRecipe(energy, input, output, chance);
					return;
				case RECIPE_TYPE_RECYCLE:
					// If the recipe is explicitly a recycle recipe, then all chance results should be locked.
					for (int i = 0; i < output.size(); i++) {
						if (chance.get(i) > 0.0F) {
							chance.set(i, chance.get(i) * BASE_CHANCE_LOCKED);
						}
					}
				default:
			}
		}
		PulverizerRecipeManager.instance().addRecipe(energy, input, output, chance);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			PulverizerRecipeManager.instance().removeRecipe(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();

}
