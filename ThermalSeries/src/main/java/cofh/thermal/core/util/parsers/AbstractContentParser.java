package cofh.thermal.core.util.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import static cofh.lib.util.Constants.BASE_CHANCE_LOCKED;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.oredict.OreDictHelper.oreNameExists;
import static cofh.thermal.core.init.ParsersTSeries.CONSTANTS;
import static cofh.thermal.core.init.ParsersTSeries.ORES;

public abstract class AbstractContentParser implements IContentParser {

	protected abstract void parseObject(JsonObject object);

	protected void parseArray(JsonArray array) {

		for (JsonElement object : array) {
			if (!preCheck(object.getAsJsonObject())) {
				continue;
			}
			parseObject(object.getAsJsonObject());
		}
	}

	protected boolean preCheck(JsonObject object) {

		if (object.has(COMMENT)) {
			return false;
		}
		if (object.has(ENABLE) && !object.get(ENABLE).getAsBoolean()) {
			return false;
		}
		if (object.has(DEPENDENCY)) {
			return parseDependencies(object.get(DEPENDENCY));
		}
		return true;
	}

	// region IContentParser
	@Override
	public void preProcess() {

	}

	@Override
	public boolean process(JsonElement element) {

		if (element.isJsonObject()) {
			parseObject(element.getAsJsonObject());
			return true;
		}
		if (element.isJsonArray()) {
			parseArray(element.getAsJsonArray());
			return true;
		}
		return false;
	}

	@Override
	public void postProcess() {

	}
	// endregion

	// region HELPERS
	public static void parseInputs(List<ItemStack> items, List<FluidStack> fluids, JsonElement element) {

		if (element.isJsonArray()) {
			for (JsonElement arrayElement : element.getAsJsonArray()) {
				if (arrayElement.getAsJsonObject().has(FLUID)) {
					fluids.add(parseFluidStack(arrayElement));
				} else {
					items.add(parseItemStack(arrayElement));
				}
			}
		} else if (element.getAsJsonObject().has(FLUID)) {
			fluids.add(parseFluidStack(element));
		} else {
			items.add(parseItemStack(element));
		}
	}

	public static void parseOutputs(List<ItemStack> items, List<Float> chances, List<FluidStack> fluids, JsonElement element) {

		if (element.isJsonArray()) {
			for (JsonElement arrayElement : element.getAsJsonArray()) {
				if (arrayElement.getAsJsonObject().has(FLUID)) {
					fluids.add(parseFluidStack(arrayElement));
				} else {
					items.add(parseItemStack(arrayElement));
					chances.add(parseItemChance(arrayElement));
				}
			}
		} else if (element.getAsJsonObject().has(FLUID)) {
			fluids.add(parseFluidStack(element));
		} else {
			items.add(parseItemStack(element));
			chances.add(parseItemChance(element));
		}
	}

	public static boolean parseDependencies(JsonElement element) {

		if (element.isJsonArray()) {
			boolean check = true;
			for (JsonElement arrayElement : element.getAsJsonArray()) {
				check &= parseDependency(arrayElement);
			}
			return check;
		} else {
			return parseDependency(element);
		}
	}

	public static ItemStack parseItemStack(JsonElement element) {

		if (element == null || element.isJsonNull()) {
			return ItemStack.EMPTY;
		}
		// TODO: 1.13ify

		ItemStack stack;
		Item item = null;
		String ore = "";
		int data = 0;
		int count = 1;

		if (element.isJsonPrimitive()) {
			item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(element.getAsString()));
			return item == null ? ItemStack.EMPTY : new ItemStack(item);
		} else {
			JsonObject itemObject = element.getAsJsonObject();

			/* COUNT */
			if (itemObject.has(COUNT)) {
				count = itemObject.get(COUNT).getAsInt();
			}

			/* CONSTANT */
			if (itemObject.has(CONSTANT)) {
				ore = itemObject.get(CONSTANT).getAsString();
				if (CONSTANTS.containsKey(ore)) {
					return cloneStack(CONSTANTS.get(ore), count);
				} else {
					ore = "";
				}
			}

			/* ORE NAME */
			if (itemObject.has(ORE)) {
				ore = itemObject.get(ORE).getAsString();
			}
			if (oreNameExists(ore)) {
				if (ORES.containsKey(ore)) {
					return cloneStack(ORES.get(ore), count);
				}
				NonNullList<ItemStack> ores = OreDictionary.getOres(ore, false);
				if (!ores.isEmpty()) {
					return cloneStack(ores.get(0), count);
				}
			}

			/* DATA */
			if (itemObject.has(DATA)) {
				data = itemObject.get(DATA).getAsInt();
			} else if (itemObject.has(WILDCARD) && itemObject.get(WILDCARD).getAsBoolean()) {
				data = OreDictionary.WILDCARD_VALUE;
			}

			/* ITEM */
			if (itemObject.has(ITEM)) {
				item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemObject.get(ITEM).getAsString()));
			}
			if (item == null) {
				return ItemStack.EMPTY;
			}
			stack = new ItemStack(item, count, data);

			/* NBT */
			if (itemObject.has(NBT)) {
				try {
					stack.setTagCompound(JsonToNBT.getTagFromJson(itemObject.get(NBT).getAsString()));
				} catch (NBTException t) {
					return ItemStack.EMPTY;
				}
			}
		}
		return stack;
	}

	public static void parseItemStacks(List<ItemStack> items, JsonElement element) {

		if (element.isJsonArray()) {
			for (JsonElement arrayElement : element.getAsJsonArray()) {
				items.add(parseItemStack(arrayElement));
			}
		} else {
			items.add(parseItemStack(element));
		}
	}

	public static void parseItemStacks(List<ItemStack> items, List<Float> chances, JsonElement element) {

		if (element.isJsonArray()) {
			for (JsonElement arrayElement : element.getAsJsonArray()) {
				items.add(parseItemStack(arrayElement));
				chances.add(parseItemChance(arrayElement));
			}
		} else {
			items.add(parseItemStack(element));
			chances.add(parseItemChance(element));
		}
	}

	public static FluidStack parseFluidStack(JsonElement element) {

		if (element == null || element.isJsonNull()) {
			return null;
		}
		FluidStack stack;
		Fluid fluid = null;
		int amount = Fluid.BUCKET_VOLUME;

		if (element.isJsonPrimitive()) {
			fluid = FluidRegistry.getFluid(element.getAsString());
			return fluid == null ? null : new FluidStack(fluid, amount);
		} else {
			JsonObject fluidObject = element.getAsJsonObject();

			/* AMOUNT */
			if (fluidObject.has(AMOUNT)) {
				amount = fluidObject.get(AMOUNT).getAsInt();
			}

			/* FLUID */
			if (fluidObject.has(FLUID)) {
				fluid = FluidRegistry.getFluid(fluidObject.get(FLUID).getAsString());
			}
			if (fluid == null) {
				return null;
			}
			stack = new FluidStack(fluid, amount);

			/* NBT */
			if (fluidObject.has(NBT)) {
				try {
					stack.tag = JsonToNBT.getTagFromJson(fluidObject.get(NBT).getAsString());
				} catch (NBTException t) {
					return null;
				}
			}
		}
		return stack;
	}

	public static void parseFluidStacks(List<FluidStack> fluids, JsonElement element) {

		if (element.isJsonArray()) {
			for (JsonElement arrayElement : element.getAsJsonArray()) {
				fluids.add(parseFluidStack(arrayElement));
			}
		} else {
			fluids.add(parseFluidStack(element));
		}
	}

	public static float parseItemChance(JsonElement element) {

		JsonObject object = element.getAsJsonObject();

		if (object.has(CHANCE)) {
			float chance = object.get(CHANCE).getAsFloat();
			if (chance > 0.0F && object.has(LOCKED) && object.get(LOCKED).getAsBoolean()) {
				chance *= BASE_CHANCE_LOCKED;
			}
			return chance;
		}
		return BASE_CHANCE_LOCKED;
	}

	public static boolean parseDependency(JsonElement element) {

		JsonObject depObject = element.getAsJsonObject();

		if (depObject.has(MOD)) {
			return Loader.isModLoaded(depObject.get(MOD).getAsString());
		} else if (depObject.has(ORE)) {
			return oreNameExists(depObject.get(ORE).getAsString());
		}
		return true;
	}
	// endregion

	// region STRING CONSTANTS
	public static final String AMOUNT = "amount";
	public static final String CHANCE = "chance";
	public static final String COMMENT = "//";
	public static final String CONSTANT = "constant";
	public static final String COUNT = "count";
	public static final String DATA = "data";
	public static final String DEPENDENCY = "dependency";
	public static final String ENABLE = "enable";
	public static final String ENERGY = "energy";
	public static final String ENERGY_MOD = "energy_mod";
	public static final String ENTRY = "entry";
	public static final String FLUID = "fluid";
	public static final String INPUT = "input";
	public static final String ITEM = "item";
	public static final String LOCKED = "locked";
	public static final String MOD = "mod";
	public static final String NBT = "nbt";
	public static final String ORE = "ore";
	public static final String OUTPUT = "output";
	public static final String REMOVE = "remove";
	public static final String TYPE = "type";
	public static final String WILDCARD = "wildcard";

	public static final String RECIPE_TYPE_CROP_DEFAULT = "default-crop";
	public static final String RECIPE_TYPE_LOG_DEFAULT = "default-log";
	public static final String RECIPE_TYPE_ORE_DEFAULT = "default-ore";
	public static final String RECIPE_TYPE_PLANT_DEFAULT = "default-plant";

	public static final String RECIPE_TYPE_ORE = "ore";
	public static final String RECIPE_TYPE_PLANT = "plant";
	public static final String RECIPE_TYPE_RECYCLE = "recycle";
	// endregion
}
