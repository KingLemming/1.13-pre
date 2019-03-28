package cofh.thermal.expansion.util.parsers.machine;

import cofh.lib.util.helpers.FluidHelper;
import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.machine.ExtruderRecipeManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Set;

public class ExtruderRecipeParser extends AbstractContentParser {

	private static final ExtruderRecipeParser INSTANCE = new ExtruderRecipeParser();

	public static ExtruderRecipeParser instance() {

		return INSTANCE;
	}

	private ExtruderRecipeParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack stone;
		ArrayList<FluidStack> inputFluids = new ArrayList<>();
		int lava = 0;
		int water = 0;
		int energy = ExtruderRecipeManager.instance().getDefaultEnergy();

		/* OUTPUT */
		stone = parseItemStack(object.get(OUTPUT));

		/* REMOVAL */
		if (object.has(REMOVE) && object.get(REMOVE).getAsBoolean()) {
			removeQueue.add(stone);
			return;
		}

		/* INPUT */
		if (object.has(LAVA)) {
			lava = object.get(LAVA).getAsInt();
		}
		if (object.has(WATER)) {
			water = object.get(WATER).getAsInt();
		}

		if (lava == 0 && water == 0 && object.has(INPUT)) {
			parseFluidStacks(inputFluids, object.get(INPUT));

			if (!inputFluids.isEmpty()) {
				for (FluidStack fluid : inputFluids) {
					if (FluidHelper.isLava(fluid)) {
						lava = fluid.amount;
					} else if (FluidHelper.isWater(fluid)) {
						water = fluid.amount;
					}
				}
			}
		}

		/* ENERGY */
		if (object.has(ENERGY)) {
			energy = object.get(ENERGY).getAsInt();
		}
		if (object.has(ENERGY_MOD)) {
			energy *= object.get(ENERGY_MOD).getAsFloat();
		}

		ExtruderRecipeManager.instance().addRecipe(energy, stone, lava, water);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			ExtruderRecipeManager.instance().removeRecipe(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();

}
