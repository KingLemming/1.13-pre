package cofh.lib.util.modhelpers;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static cofh.lib.util.Constants.ID_ENSORCELLMENT;

public class EnsorcellmentHelper {

	private EnsorcellmentHelper() {

	}

	// region ENCHANTMENT IDS
	public static final String ID_PROTECTION = "minecraft:protection";
	public static final String ID_THORNS = "minecraft:thorns";
	public static final String ID_FROST_WALKER = "minecraft:frost_walker";
	public static final String ID_FIRE_ASPECT = "minecraft:fire_aspect";
	public static final String ID_LOOTING = "minecraft:looting";
	public static final String ID_MENDING = "minecraft:mending";

	public static final String ID_HOLDING = ID_ENSORCELLMENT + ":holding";
	public static final String ID_SOULBOUND = ID_ENSORCELLMENT + ":soulbound";

	public static final String ID_LEECH = ID_ENSORCELLMENT + ":leech";
	public static final String ID_VORPAL = ID_ENSORCELLMENT + ":vorpal";

	public static final String ID_INSIGHT = ID_ENSORCELLMENT + ":insight";
	public static final String ID_SMASHING = ID_ENSORCELLMENT + ":smashing";
	public static final String ID_SMELTING = ID_ENSORCELLMENT + ":smelting";

	public static final String ID_MULTISHOT = ID_ENSORCELLMENT + ":multishot";
	public static final String ID_QUICKDRAW = ID_ENSORCELLMENT + ":quickdraw";
	public static final String ID_TRUESHOT = ID_ENSORCELLMENT + ":trueshot";

	public static final String ID_BULWARK = ID_ENSORCELLMENT + ":bulwark";
	public static final String ID_PHALANX = ID_ENSORCELLMENT + ":phalanx";
	// endregion

	// region ENCHANTMENTS
	/**
	 * ALL
	 */
	@ObjectHolder (ID_HOLDING)
	public static final Enchantment HOLDING = null;

	@ObjectHolder (ID_SOULBOUND)
	public static final Enchantment SOULBOUND = null;

	/**
	 * WEAPON
	 */
	@ObjectHolder (ID_LEECH)
	public static final Enchantment LEECH = null;

	@ObjectHolder (ID_VORPAL)
	public static final Enchantment VORPAL = null;

	/**
	 * DIGGER
	 */
	@ObjectHolder (ID_INSIGHT)
	public static final Enchantment INSIGHT = null;

	@ObjectHolder (ID_SMASHING)
	public static final Enchantment SMASHING = null;

	@ObjectHolder (ID_SMELTING)
	public static final Enchantment SMELTING = null;

	/**
	 * BOW
	 */
	@ObjectHolder (ID_MULTISHOT)
	public static final Enchantment MULTISHOT = null;

	@ObjectHolder (ID_QUICKDRAW)
	public static final Enchantment QUICKDRAW = null;

	@ObjectHolder (ID_TRUESHOT)
	public static final Enchantment TRUESHOT = null;

	/**
	 * SHIELD
	 */
	@ObjectHolder (ID_BULWARK)
	public static final Enchantment BULWARK = null;

	@ObjectHolder (ID_PHALANX)
	public static final Enchantment PHALANX = null;
	// endregion
}
