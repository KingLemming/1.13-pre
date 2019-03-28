package cofh.thermal.expansion.util.parsers.dynamo;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.dynamo.LapidaryFuelManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class LapidaryFuelParser extends AbstractContentParser {

	private static final LapidaryFuelParser INSTANCE = new LapidaryFuelParser();

	public static LapidaryFuelParser instance() {

		return INSTANCE;
	}

	private LapidaryFuelParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		int energy = LapidaryFuelManager.instance().getDefaultEnergy();

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

		LapidaryFuelManager.instance().addFuel(energy, input);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			LapidaryFuelManager.instance().removeFuel(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();
}
