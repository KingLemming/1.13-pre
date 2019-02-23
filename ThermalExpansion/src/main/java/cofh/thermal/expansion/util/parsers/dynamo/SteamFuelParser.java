package cofh.thermal.expansion.util.parsers.dynamo;

import cofh.thermal.core.util.parsers.AbstractContentParser;
import cofh.thermal.expansion.util.managers.dynamo.SteamFuelManager;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class SteamFuelParser extends AbstractContentParser {

	private static final SteamFuelParser INSTANCE = new SteamFuelParser();

	public static SteamFuelParser instance() {

		return INSTANCE;
	}

	private SteamFuelParser() {

	}

	// TODO: Fix
	@Override
	protected void parseObject(JsonObject object) {

		ItemStack input;
		int energy = SteamFuelManager.instance().getDefaultEnergy();

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

		SteamFuelManager.instance().addFuel(energy, input);
	}

	@Override
	public void postProcess() {

		for (ItemStack stack : removeQueue) {
			SteamFuelManager.instance().removeFuel(stack);
		}
	}

	Set<ItemStack> removeQueue = new ObjectOpenHashSet<>();
}
