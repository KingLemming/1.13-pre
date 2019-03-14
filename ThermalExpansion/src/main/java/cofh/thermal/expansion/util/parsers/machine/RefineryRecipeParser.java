package cofh.thermal.expansion.util.parsers.machine;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.machine.RefineryRecipeManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Set;

public class RefineryRecipeParser extends AbstractContentParser {

	private static final RefineryRecipeParser INSTANCE = new RefineryRecipeParser();

	public static RefineryRecipeParser instance() {

		return INSTANCE;
	}

	private RefineryRecipeParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		FluidStack input;
		ArrayList<ItemStack> outputItems = new ArrayList<>();
		ArrayList<Float> chance = new ArrayList<>();
		ArrayList<FluidStack> outputFluids = new ArrayList<>();
		int energy = RefineryRecipeManager.instance().getDefaultEnergy();

		/* INPUT */
		input = parseFluidStack(object.get(INPUT));

		/* REMOVAL */
		if (object.has(REMOVE) && object.get(REMOVE).getAsBoolean()) {
			removeQueue.add(input);
			return;
		}

		/* OUTPUT */
		parseOutputs(outputItems, chance, outputFluids, object.get(OUTPUT));

		/* ENERGY */
		if (object.has(ENERGY)) {
			energy = object.get(ENERGY).getAsInt();
		}
		if (object.has(ENERGY_MOD)) {
			energy *= object.get(ENERGY_MOD).getAsFloat();
		}

		if (outputItems.isEmpty()) {
			RefineryRecipeManager.instance().addRecipe(energy, input, outputFluids);
		} else {
			RefineryRecipeManager.instance().addRecipe(energy, input, outputFluids, outputItems.get(0));
		}
	}

	@Override
	public void postProcess() {

		for (FluidStack stack : removeQueue) {
			RefineryRecipeManager.instance().removeRecipe(stack);
		}
	}

	Set<FluidStack> removeQueue = new ObjectOpenHashSet<>();

}
