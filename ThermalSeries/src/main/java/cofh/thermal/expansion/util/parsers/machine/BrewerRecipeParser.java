package cofh.thermal.expansion.util.parsers.machine;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.machine.BrewerRecipeManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Set;

public class BrewerRecipeParser extends AbstractContentParser {

	private static final BrewerRecipeParser INSTANCE = new BrewerRecipeParser();

	public static BrewerRecipeParser instance() {

		return INSTANCE;
	}

	private BrewerRecipeParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ArrayList<ItemStack> inputItems = new ArrayList<>();
		ArrayList<FluidStack> inputFluids = new ArrayList<>();
		FluidStack outputFluid;
		int energy = BrewerRecipeManager.instance().getDefaultEnergy();

		/* INPUT */
		parseInputs(inputItems, inputFluids, object.get(INPUT));

		if (inputItems.isEmpty() || inputFluids.isEmpty()) {
			return;
		}

		/* REMOVAL */
		if (object.has(REMOVE) && object.get(REMOVE).getAsBoolean()) {
			removeQueue.add(Pair.of(inputItems.get(0), inputFluids.get(0)));
			return;
		}

		/* OUTPUT */
		outputFluid = parseFluidStack(object.get(OUTPUT));

		/* ENERGY */
		if (object.has(ENERGY)) {
			energy = object.get(ENERGY).getAsInt();
		}
		if (object.has(ENERGY_MOD)) {
			energy *= object.get(ENERGY_MOD).getAsFloat();
		}

		BrewerRecipeManager.instance().addRecipe(energy, inputItems.get(0), inputFluids.get(0), outputFluid);
	}

	@Override
	public void postProcess() {

		for (Pair<ItemStack, FluidStack> removal : removeQueue) {
			BrewerRecipeManager.instance().removeRecipe(removal.getLeft(), removal.getRight());
		}
	}

	Set<Pair<ItemStack, FluidStack>> removeQueue = new ObjectOpenHashSet<>();

}
