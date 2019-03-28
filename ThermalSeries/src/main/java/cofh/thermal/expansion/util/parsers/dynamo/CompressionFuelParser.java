package cofh.thermal.expansion.util.parsers.dynamo;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.dynamo.CompressionFuelManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraftforge.fluids.FluidStack;

import java.util.Set;

public class CompressionFuelParser extends AbstractContentParser {

	private static final CompressionFuelParser INSTANCE = new CompressionFuelParser();

	public static CompressionFuelParser instance() {

		return INSTANCE;
	}

	private CompressionFuelParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		FluidStack input;
		int energy = CompressionFuelManager.instance().getDefaultEnergy();

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
		CompressionFuelManager.instance().addFuel(energy, input);
	}

	@Override
	public void postProcess() {

		for (FluidStack stack : removeQueue) {
			CompressionFuelManager.instance().removeFuel(stack);
		}
	}

	Set<FluidStack> removeQueue = new ObjectOpenHashSet<>();
}
