package cofh.thermal.expansion.util.parsers.dynamo;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.dynamo.MagmaticFuelManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraftforge.fluids.FluidStack;

import java.util.Set;

public class MagmaticFuelParser extends AbstractContentParser {

	private static final MagmaticFuelParser INSTANCE = new MagmaticFuelParser();

	public static MagmaticFuelParser instance() {

		return INSTANCE;
	}

	private MagmaticFuelParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		FluidStack input;
		int energy = MagmaticFuelManager.instance().getDefaultEnergy();

		/* INPUT */
		if (object.has(FLUID)) {
			input = parseFluidStack(object.get(FLUID));
		} else {
			input = parseFluidStack(object.get(INPUT));
		}

		/* REMOVAL */
		if (object.has(REMOVE) && object.get(REMOVE).getAsBoolean()) {
			removeQueue.add(input);
			return;
		}

		/* ENERGY */
		if (object.has(ENERGY)) {
			energy = object.get(ENERGY).getAsInt();
		}
		if (object.has(ENERGY_MOD)) {
			energy *= object.get(ENERGY_MOD).getAsFloat();
		}

		if (input == null) {
			return;
		}
		MagmaticFuelManager.instance().addFuel(energy, input);
	}

	@Override
	public void postProcess() {

		for (FluidStack stack : removeQueue) {
			MagmaticFuelManager.instance().removeFuel(stack);
		}
	}

	Set<FluidStack> removeQueue = new ObjectOpenHashSet<>();
}
