package cofh.thermal.expansion.util.parsers.dynamo;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.dynamo.StirlingFuelManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class StirlingFuelParser extends AbstractContentParser {

	private static final StirlingFuelParser INSTANCE = new StirlingFuelParser();

	public static StirlingFuelParser instance() {

		return INSTANCE;
	}

	private StirlingFuelParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		int energy = StirlingFuelManager.instance().getDefaultEnergy();

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

		StirlingFuelManager.instance().addFuel(energy, input);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			StirlingFuelManager.instance().removeFuel(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();
}
