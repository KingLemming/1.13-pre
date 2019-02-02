package cofh.thermal.core.util.parsers;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import static cofh.thermal.core.init.ParsersTSeries.ORES;

public class OreDictParser extends AbstractContentParser {

	private static final OreDictParser INSTANCE = new OreDictParser();

	public static OreDictParser instance() {

		return INSTANCE;
	}

	private OreDictParser() {

	}

	@Override
	protected void parseObject(JsonObject object) {

		if (object.has(COMMENT) || object.has(ENABLE) && !object.get(ENABLE).getAsBoolean()) {
			return;
		}
		String ore;
		ItemStack entry;

		/* ORE */
		ore = object.get(ORE).getAsString();

		/* ENTRY */
		entry = parseItemStack(object.get(ENTRY));

		if (!ORES.containsKey(ore)) {
			ORES.put(ore, entry);
		}
	}

}
