package cofh.thermal.expansion.util.parsers.machine;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.machine.CrucibleRecipeManager;
import cofh.thermal.expansion.util.managers.machine.FurnaceRecipeManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Set;

public class CrucibleRecipeParser extends AbstractContentParser {

	private static final CrucibleRecipeParser INSTANCE = new CrucibleRecipeParser();

	public static CrucibleRecipeParser instance() {

		return INSTANCE;
	}

	private CrucibleRecipeParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		FluidStack output;
		int energy = FurnaceRecipeManager.instance().getDefaultEnergy();

		/* INPUT */
		input = parseItemStack(object.get(INPUT));

		/* REMOVAL */
		if (object.has(REMOVE) && object.get(REMOVE).getAsBoolean()) {
			removeQueue.add(input);
			return;
		}

		/* OUTPUT */
		output = parseFluidStack(object.get(OUTPUT));

		/* ENERGY */
		if (object.has(ENERGY)) {
			energy = object.get(ENERGY).getAsInt();
		}
		if (object.has(ENERGY_MOD)) {
			energy *= object.get(ENERGY_MOD).getAsFloat();
		}

		CrucibleRecipeManager.instance().addRecipe(energy, input, output);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			CrucibleRecipeManager.instance().removeRecipe(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();

}
