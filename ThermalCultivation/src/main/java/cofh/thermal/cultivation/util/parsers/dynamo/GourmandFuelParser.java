package cofh.thermal.cultivation.util.parsers.dynamo;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.cultivation.util.managers.dynamo.GourmandFuelManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class GourmandFuelParser extends AbstractContentParser {

	private static final GourmandFuelParser INSTANCE = new GourmandFuelParser();

	public static GourmandFuelParser instance() {

		return INSTANCE;
	}

	private GourmandFuelParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		int energy = GourmandFuelManager.instance().getDefaultEnergy();

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

		GourmandFuelManager.instance().addFuel(energy, input);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			GourmandFuelManager.instance().removeFuel(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();
}
