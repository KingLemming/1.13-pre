package cofh.thermal.expansion.util.parsers.dynamo;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.dynamo.NumismaticFuelManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class NumismaticFuelParser extends AbstractContentParser {

	private static final NumismaticFuelParser INSTANCE = new NumismaticFuelParser();

	public static NumismaticFuelParser instance() {

		return INSTANCE;
	}

	private NumismaticFuelParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		int energy = NumismaticFuelManager.instance().getDefaultEnergy();

		/* INPUT */
		input = parseItemStack(object.get(INPUT));

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

		NumismaticFuelManager.instance().addFuel(energy, input);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			NumismaticFuelManager.instance().removeFuel(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();
}
