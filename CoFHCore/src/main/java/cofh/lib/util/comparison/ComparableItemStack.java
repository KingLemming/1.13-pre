package cofh.lib.util.comparison;

import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.*;
import static cofh.lib.util.helpers.ItemHelper.getItemDamage;

/**
 * This class allows for OreDictionary-compatible ItemStack comparisons and Integer-based Hashes without collisions.
 *
 * The intended purpose of this is for things such as Recipe Handlers or HashMaps of ItemStacks.
 *
 * @author King Lemming
 */
public class ComparableItemStack {

	public static final OreValidator DEFAULT_VALIDATOR = new OreValidator();

	static {
		DEFAULT_VALIDATOR.addPrefix(PREFIX_BLOCK);
		DEFAULT_VALIDATOR.addPrefix(PREFIX_ORE);
		DEFAULT_VALIDATOR.addPrefix(PREFIX_DUST);
		DEFAULT_VALIDATOR.addPrefix(PREFIX_INGOT);
		DEFAULT_VALIDATOR.addPrefix(PREFIX_NUGGET);
		DEFAULT_VALIDATOR.addPrefix(PREFIX_GEM);
		DEFAULT_VALIDATOR.addPrefix(PREFIX_PLATE);
	}

	public Item item;
	public int metadata;
	public int stackSize;

	public int oreID = -1;
	public String oreName = "Unknown";

	public ComparableItemStack(String oreName) {

		this(OreDictHelper.getOre(oreName));
	}

	public ComparableItemStack(ItemStack stack) {

		this.item = stack.getItem();
		this.metadata = getItemDamage(stack);

		if (!stack.isEmpty()) {
			stackSize = stack.getCount();
			oreID = OreDictHelper.getOreID(stack);
			oreName = OreDictHelper.getOreName(oreID);
		}
	}

	public ComparableItemStack(Item item, int metadata, int stackSize) {

		this.item = item;
		this.metadata = metadata;
		this.stackSize = stackSize;
		this.oreID = OreDictHelper.getOreID(this.toItemStack());
		this.oreName = OreDictHelper.getOreName(oreID);
	}

	public ComparableItemStack(ComparableItemStack stack) {

		this.item = stack.item;
		this.metadata = stack.metadata;
		this.stackSize = stack.stackSize;
		this.oreID = stack.oreID;
		this.oreName = stack.oreName;
	}

	public boolean isEqual(ComparableItemStack other) {

		if (other == null) {
			return false;
		}
		if (metadata == other.metadata) {
			if (item == other.item) {
				return true;
			}
			if (item != null && other.item != null) {
				return item.delegate.get() == other.item.delegate.get();
			}
		}
		return false;
	}

	public boolean isItemEqual(ComparableItemStack other) {

		return other != null && (oreID != -1 && oreID == other.oreID || isEqual(other));
	}

	public boolean isStackEqual(ComparableItemStack other) {

		return isItemEqual(other) && stackSize == other.stackSize;
	}

	public int getId() {

		return Item.getIdFromItem(item); // '0' is null. '-1' is an unmapped item (missing in this World)
	}

	public ItemStack toItemStack() {

		return item != Items.AIR ? new ItemStack(item, stackSize, metadata) : ItemStack.EMPTY;
	}

	@Override
	public ComparableItemStack clone() {

		return new ComparableItemStack(this);
	}

	@Override
	public boolean equals(Object o) {

		return o instanceof ComparableItemStack && isItemEqual((ComparableItemStack) o);
	}

	@Override
	public int hashCode() {

		return oreID != -1 ? oreName.hashCode() : (metadata & 65535) | getId() << 16;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder(768);

		builder.append(getClass().getName()).append('@');
		builder.append(System.identityHashCode(this)).append('{');
		builder.append("ID:").append(getId()).append(", ");
		builder.append("DMG:").append(metadata).append(", ");
		builder.append("ORE:").append(oreID).append('|').append(oreName).append(", ");
		builder.append("ITM:");
		if (item == null) {
			builder.append("null");
		} else {
			builder.append(item.getClass().getName()).append('@');
			builder.append(System.identityHashCode(item)).append(' ');
			builder.append('[').append(item.getRegistryName()).append(']');
		}
		builder.append('}');

		return builder.toString();
	}

}
