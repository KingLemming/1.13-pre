package cofh.quartermastery.init;

import cofh.core.item.armor.ItemHorseArmorCoFH;
import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

import static cofh.lib.util.Constants.GEN_JSON_FILES;
import static cofh.lib.util.Constants.ID_QUARTERMASTERY;
import static cofh.lib.util.helpers.RecipeHelper.addShapedRecipe;
import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.quartermastery.Quartermastery.config;

public class EquipmentHorseArmor {

	private EquipmentHorseArmor() {

	}

	// region REGISTRATION
	public static void config() {

		String category = "HorseArmor";

		disableAll = config.get(category, "DisableAllHorseArmor", disableAll).getBoolean();
	}

	public static void registerItems() {

		config();

		for (HorseArmor armor : HorseArmor.values()) {
			armor.registerItems();
		}
	}

	public static void registerRecipes() {

		for (HorseArmor armor : HorseArmor.values()) {
			armor.registerRecipes();
		}
	}

	@SideOnly (Side.CLIENT)
	public static void registerModels() {

		for (HorseArmor armor : HorseArmor.values()) {
			armor.registerModels();
		}
	}
	// endregion

	// region HELPERS
	private static HorseArmorType addHorseArmor(String name, int protection) {

		return EnumHelper.addHorseArmor("QM:" + name.toUpperCase(Locale.ROOT), ID_QUARTERMASTERY + ":textures/horse_armor/" + name.toLowerCase(Locale.ROOT) + ".png", protection);
	}
	// endregion

	public static HorseArmorType armorTypeEmerald = addHorseArmor("emerald", 8);

	public static HorseArmorType armorTypeCopper = addHorseArmor("copper", 4);
	public static HorseArmorType armorTypeTin = addHorseArmor("tin", 4);
	public static HorseArmorType armorTypeSilver = addHorseArmor("silver", 8);
	public static HorseArmorType armorTypeLead = addHorseArmor("lead", 5);
	public static HorseArmorType armorTypeNickel = addHorseArmor("nickel", 9);
	public static HorseArmorType armorTypePlatinum = addHorseArmor("platinum", 12);

	public static HorseArmorType armorTypeAluminum = addHorseArmor("aluminum", 5);

	public static HorseArmorType armorTypeSteel = addHorseArmor("steel", 9);
	public static HorseArmorType armorTypeBronze = addHorseArmor("bronze", 9);
	public static HorseArmorType armorTypeInvar = addHorseArmor("invar", 10);
	public static HorseArmorType armorTypeElectrum = addHorseArmor("electrum", 8);
	public static HorseArmorType armorTypeConstantan = addHorseArmor("constantan", 8);

	public static boolean disableAll;

	// region HORSE ARMOR
	public enum HorseArmor {

		// @formatter:off
		EMERALD("emerald", "gemEmerald", armorTypeEmerald),

		COPPER("copper", "ingotCopper", armorTypeCopper),
		TIN("tin", "ingotTin", armorTypeTin),
		LEAD("lead", "ingotLead", armorTypeLead),
		SILVER("silver", "ingotSilver", armorTypeSilver),
		NICKEL("nickel", "ingotNickel", armorTypeNickel),
		PLATINUM("platinum", "ingotPlatinum", armorTypePlatinum),
		
		ALUMINUM("aluminum", "ingotAluminum", armorTypeAluminum),

		STEEL("steel", "ingotSteel", armorTypeSteel),
		BRONZE("bronze", "ingotBronze", armorTypeBronze),
		INVAR("invar", "ingotInvar", armorTypeInvar),
		ELECTRUM("electrum", "ingotElectrum", armorTypeElectrum),
		CONSTANTAN("constantan", "ingotConstantan", armorTypeConstantan);
		// @formatter:on

		public final String name;
		public final String ingot;
		public final HorseArmorType type;

		public ItemStack horseArmor;

		public boolean enable;

		HorseArmor(String name, String ingot, HorseArmorType type) {

			this.name = name.toLowerCase(Locale.ROOT);
			this.ingot = ingot;
			this.type = type;
		}

		void config() {

			String category = "HorseArmor." + titleCase(name);
			String comment = "Amount of protection for the armor; same scale as player armor, except horses only wear one piece.";

			type.protection = config.getInt("Protection", category, type.protection, 0, 20, comment);
			enable = config.get(category, "Enable", true).getBoolean() && !disableAll;
		}

		void registerItems() {

			config();
			boolean ingotExists = OreDictHelper.oreNameExists(ingot);

			horseArmor = registerItem("horse_armor_" + name, new ItemHorseArmorCoFH(type).setGroup("horse_armor").showInCreativeTab(enable && ingotExists));
		}

		void registerRecipes() {

			if (enable) {
				addShapedRecipe(horseArmor, "I I", "LCL", "I I", 'C', "blockWool", 'L', Items.LEATHER, 'I', ingot);
			}
		}

		// region HELPERS
		private ItemStack registerItem(String itemName, Item item) {

			item.setRegistryName(ID_QUARTERMASTERY, itemName);
			item.setUnlocalizedName(ID_QUARTERMASTERY + "." + itemName);
			ForgeRegistries.ITEMS.register(item);

			return new ItemStack(item);
		}

		@SideOnly (Side.CLIENT)
		public void registerModels() {

			ItemHorseArmorCoFH item = (ItemHorseArmorCoFH) horseArmor.getItem();
			if (GEN_JSON_FILES) {
				item.generateModelFiles();
			}
			item.registerModel();
		}
		// endregion
	}
	// endregion
}
