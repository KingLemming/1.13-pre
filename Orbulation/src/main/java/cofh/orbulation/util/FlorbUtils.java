package cofh.orbulation.util;

import cofh.lib.util.DefaultedHashMap;
import cofh.lib.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cofh.lib.util.Constants.MAGMATIC_TEMPERATURE;
import static cofh.lib.util.Constants.TAG_FLUID;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.orbulation.Orbulation.*;

public class FlorbUtils {

	private FlorbUtils() {

	}

	public static final ArrayList<ItemStack> FLORB_LIST = new ArrayList<>();
	public static final Map<String, ItemStack> FLORB_MAP = new DefaultedHashMap<>(ItemStack.EMPTY);

	public static boolean enable = true;
	public static List<String> blacklist = new ArrayList<>();

	public static ItemStack setTag(ItemStack container, Fluid fluid) {

		if (fluid != null && fluid.canBePlacedInWorld()) {
			container.setTagCompound(new NBTTagCompound());
			container.getTagCompound().setString(TAG_FLUID, fluid.getName());
		}
		return container;
	}

	public static void config() {

		String category = "Florbs";
		String comment = "If TRUE, the recipes for Florbs are enabled. Setting this to FALSE means that you actively dislike fun things.";
		enable = config.getBoolean("EnableRecipe", category, enable, comment);

		category = "Florbs.Blacklist";
		comment = "List of fluids that are not allowed to be placed in Florbs.";
		blacklist = Arrays.asList(config.getStringList("Blacklist", category, new String[0], comment));

		addDefaultFlorbs();
	}

	// region HELPERS
	public static void addDefaultFlorbs() {

		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			if (!fluid.canBePlacedInWorld()) {
				continue;
			}
			if (blacklist.contains(fluid.getName())) {
				continue;
			}
			addFlorb(fluid);
		}
	}

	public static void addFlorb(Fluid fluid) {

		ItemStack florb = fluid.getTemperature() < MAGMATIC_TEMPERATURE ? cloneStack(itemFlorb) : cloneStack(itemFlorbMagmatic);
		setTag(florb, fluid);
		FLORB_LIST.add(florb);
		FLORB_MAP.put(fluid.getName(), florb);
	}

	public static void dropFlorb(Fluid fluid, World world, BlockPos pos) {

		Utils.dropItemStackIntoWorldWithVelocity(getFlorb(fluid), world, pos);
	}

	@Nonnull
	public static ItemStack getFlorb(Fluid fluid) {

		if (!FLORB_MAP.containsKey(fluid.getName())) {
			return cloneStack(itemFlorb);
		}
		return FLORB_MAP.get(fluid.getName());
	}
	// endregion
}
