package cofh.thermal.expansion.util.parsers.machine;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.machine.CentrifugeRecipeManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Set;

public class CentrifugeRecipeParser extends AbstractContentParser {

	private static final CentrifugeRecipeParser INSTANCE = new CentrifugeRecipeParser();

	public static CentrifugeRecipeParser instance() {

		return INSTANCE;
	}

	private CentrifugeRecipeParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		if (!preCheck(object)) {
			return;
		}
		ItemStack input;
		ArrayList<ItemStack> output = new ArrayList<>();
		ArrayList<Float> chance = new ArrayList<>();
		FluidStack fluid = null;
		int energy = CentrifugeRecipeManager.instance().getDefaultEnergy();

		/* INPUT */
		input = parseItemStack(object.get(INPUT));

		/* REMOVAL */
		if (object.has(REMOVE) && object.get(REMOVE).getAsBoolean()) {
			removeQueue.add(input);
			return;
		}

		/* OUTPUT */
		parseItemStacks(output, chance, object.get(OUTPUT));

		/* FLUID */
		if (object.has(FLUID)) {
			fluid = parseFluidStack(object.get(FLUID));
		}

		/* ENERGY */
		if (object.has(ENERGY)) {
			energy = object.get(ENERGY).getAsInt();
		} else if (object.has(ENERGY_MOD)) {
			energy *= object.get(ENERGY_MOD).getAsFloat();
		}

		CentrifugeRecipeManager.instance().addRecipe(energy, input, output, chance, fluid);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			CentrifugeRecipeManager.instance().removeRecipe(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();

}
