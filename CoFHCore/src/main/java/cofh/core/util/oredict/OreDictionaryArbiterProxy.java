package cofh.core.util.oredict;

import cofh.lib.util.oredict.OreDictionaryProxy;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;

/**
 * If CoFHCore is present, an instance of this class is initialized by the OreDictionaryArbiter and the functionality in OreDictHelper is much improved.
 *
 * Translation: Don't touch.
 *
 * @author King Lemming
 */
public class OreDictionaryArbiterProxy extends OreDictionaryProxy {

	@Override
	public final ItemStack getOre(String oreName, int amount) {

		if (OreDictionaryArbiter.getOres(oreName) == null) {
			return ItemStack.EMPTY;
		}
		return cloneStack(OreDictionaryArbiter.getOres(oreName).get(0), amount);
	}

	@Override
	public final boolean oreNameExists(String oreName) {

		ArrayList<ItemStack> ores = OreDictionaryArbiter.getOres(oreName);
		return ores != null && ores.size() > 0;
	}

	@Override
	public final boolean isOreNameEqual(ItemStack stack, String oreName) {

		return OreDictionaryArbiter.getOreName(OreDictionaryArbiter.getOreID(stack)).equals(oreName);
	}

	@Override
	public final boolean isOreIDEqual(ItemStack stack, int oreID) {

		return OreDictionaryArbiter.getOreID(stack) == oreID;
	}

	@Override
	public final String getOreName(ItemStack stack) {

		return OreDictionaryArbiter.getOreName(OreDictionaryArbiter.getOreID(stack));
	}

	@Override
	public final String getOreName(int oreID) {

		return OreDictionaryArbiter.getOreName(oreID);
	}

	@Override
	public final int getOreID(ItemStack stack) {

		return OreDictionaryArbiter.getOreID(stack);
	}

	@Override
	public final int getOreID(String oreName) {

		return OreDictionaryArbiter.getOreID(oreName);
	}

	@Override
	public List<Integer> getAllOreIDs(ItemStack stack) {

		return OreDictionaryArbiter.getAllOreIDs(stack);
	}

}
