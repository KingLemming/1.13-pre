package cofh.lib.util.oredict;

import net.minecraft.item.ItemStack;

import java.util.List;

import static cofh.lib.util.Constants.ORE_UNKNOWN;

/**
 * Essentially a passthrough class for OreDict related functions. Utilizes a proxy. If CoFHCore is installed, then
 * a more advanced version of the proxy is used for faster lookups. It's entirely transparent though.
 */
public class OreDictHelper {

	private OreDictHelper() {

	}

	public static void initializeProxy(OreDictionaryProxy newProxy) {

		if (initialized) {
			return;
		}
		oreProxy = newProxy;
		initialized = true;
	}

	private static OreDictionaryProxy oreProxy = new OreDictionaryProxy();
	private static boolean initialized = false;

	public static ItemStack getOre(String oreName) {

		return getOre(oreName, 1);
	}

	public static ItemStack getOre(String oreName, int count) {

		return oreProxy.getOre(oreName, count);
	}

	public static boolean hasOreName(ItemStack stack) {

		return !getOreName(stack).equals(ORE_UNKNOWN);
	}

	public static boolean hasOrePrefix(ItemStack stack, String prefix) {

		return getOreName(stack).startsWith(prefix);
	}

	public static boolean oreNameExists(String oreName) {

		return oreProxy.oreNameExists(oreName);
	}

	public static boolean isOreNameEqual(ItemStack stack, String oreName) {

		return oreProxy.isOreNameEqual(stack, oreName);
	}

	public static boolean isOreIDEqual(ItemStack stack, int oreID) {

		return oreProxy.isOreIDEqual(stack, oreID);
	}

	public static String getOreName(ItemStack stack) {

		return oreProxy.getOreName(stack);
	}

	public static String getOreName(int oreID) {

		return oreProxy.getOreName(oreID);
	}

	public static int getOreID(ItemStack stack) {

		return getOreID(getOreName(stack));
	}

	public static int getOreID(String oreName) {

		return oreProxy.getOreID(oreName);
	}

	public static List<Integer> getAllOreIDs(ItemStack stack) {

		return oreProxy.getAllOreIDs(stack);
	}

}
