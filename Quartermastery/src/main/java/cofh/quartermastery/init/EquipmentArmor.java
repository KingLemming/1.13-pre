package cofh.quartermastery.init;

import cofh.core.item.armor.ItemArmorCoFH;
import cofh.lib.util.IModelRegister;
import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Locale;

import static cofh.lib.util.Constants.GEN_JSON_FILES;
import static cofh.lib.util.Constants.ID_QUARTERMASTERY;
import static cofh.lib.util.helpers.RecipeHelper.addShapedRecipe;
import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.quartermastery.Quartermastery.config;

public class EquipmentArmor {

	private EquipmentArmor() {

	}

	// region REGISTRATION
	public static void config() {

		String category = "Armor";

		disableAll[0] = config.get(category, "Disable All Helmets", disableAll[0]).getBoolean();
		disableAll[1] = config.get(category, "Disable All Chestplates", disableAll[1]).getBoolean();
		disableAll[2] = config.get(category, "Disable All Leggings", disableAll[2]).getBoolean();
		disableAll[3] = config.get(category, "Disable All Boots", disableAll[3]).getBoolean();
	}

	public static void registerItems() {

		config();

		for (ArmorSet set : ArmorSet.values()) {
			set.registerItems();
		}
	}

	public static void registerRecipes() {

		for (ArmorSet set : ArmorSet.values()) {
			set.registerRecipes();
		}
	}

	@SideOnly (Side.CLIENT)
	public static void registerModels() {

		for (ArmorSet set : ArmorSet.values()) {
			set.registerModels();
		}
	}
	// endregion

	public static final ArmorMaterial armorMaterialEmerald = EnumHelper.addArmorMaterial("QM:EMERALD", "emerald_armor", 10, new int[] { 2, 4, 4, 2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0F);

	public static final ArmorMaterial armorMaterialCopper = EnumHelper.addArmorMaterial("QM:COPPER", "copper_armor", 10, new int[] { 1, 3, 3, 1 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static final ArmorMaterial armorMaterialTin = EnumHelper.addArmorMaterial("QM:TIN", "tin_armor", 8, new int[] { 1, 3, 4, 1 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static final ArmorMaterial armorMaterialLead = EnumHelper.addArmorMaterial("QM:LEAD", "lead_armor", 12, new int[] { 2, 4, 5, 3 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static final ArmorMaterial armorMaterialSilver = EnumHelper.addArmorMaterial("QM:SILVER", "silver_armor", 8, new int[] { 2, 4, 4, 1 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0F);
	public static final ArmorMaterial armorMaterialNickel = EnumHelper.addArmorMaterial("QM:NICKEL", "nickel_armor", 15, new int[] { 2, 5, 5, 2 }, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static final ArmorMaterial armorMaterialPlatinum = EnumHelper.addArmorMaterial("QM:PLATINUM", "platinum_armor", 35, new int[] { 3, 6, 8, 3 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2F);

	public static final ArmorMaterial armorMaterialAluminum = EnumHelper.addArmorMaterial("QM:ALUMINUM", "aluminum_armor", 9, new int[] { 1, 3, 4, 2 }, 14, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0F);

	public static final ArmorMaterial armorMaterialSteel = EnumHelper.addArmorMaterial("QM:STEEL", "steel_armor", 22, new int[] { 2, 5, 7, 2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1F);
	public static final ArmorMaterial armorMaterialBronze = EnumHelper.addArmorMaterial("QM:BRONZE", "bronze_armor", 18, new int[] { 2, 6, 6, 2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1F);
	public static final ArmorMaterial armorMaterialInvar = EnumHelper.addArmorMaterial("QM:INVAR", "invar_armor", 21, new int[] { 2, 5, 7, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1F);
	public static final ArmorMaterial armorMaterialElectrum = EnumHelper.addArmorMaterial("QM:ELECTRUM", "electrum_armor", 8, new int[] { 2, 4, 4, 2 }, 30, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0F);
	public static final ArmorMaterial armorMaterialConstantan = EnumHelper.addArmorMaterial("QM:CONSTANTAN", "constantan_armor", 13, new int[] { 2, 4, 4, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);

	public static boolean disableAll[] = new boolean[4];

	// region ARMOR SET
	public enum ArmorSet {

		// @formatter:off
		EMERALD("emerald", "gemEmerald", armorMaterialEmerald),

		COPPER("copper", "ingotCopper", armorMaterialCopper),
		TIN("tin", "ingotTin", armorMaterialTin),
		LEAD("lead", "ingotLead", armorMaterialLead),
		SILVER("silver", "ingotSilver", armorMaterialSilver),
		NICKEL("nickel", "ingotNickel", armorMaterialNickel),
		PLATINUM("platinum", "ingotPlatinum", armorMaterialPlatinum),

		ALUMINUM("aluminum", "ingotAluminum", armorMaterialAluminum),

		STEEL("steel", "ingotSteel", armorMaterialSteel),
		BRONZE("bronze", "ingotBronze", armorMaterialBronze),
		INVAR("invar", "ingotInvar", armorMaterialInvar),
		ELECTRUM("electrum", "ingotElectrum", armorMaterialElectrum),
		CONSTANTAN("constantan", "ingotConstantan", armorMaterialConstantan);
		// @formatter:on

		private final String name;
		private final String ingot;
		private final ArmorMaterial material;

		public ArrayList<Item> itemList = new ArrayList<>();

		/* REFERENCES */
		public ItemStack armorHelmet;
		public ItemStack armorChestplate;
		public ItemStack armorLegs;
		public ItemStack armorBoots;

		public boolean[] enable = new boolean[4];

		ArmorSet(String name, String ingot, ArmorMaterial material) {

			this.name = name.toLowerCase(Locale.ROOT);
			this.ingot = ingot;
			this.material = material;
		}

		void config() {

			String category = "Armor." + titleCase(name) + ".Material";
			String comment;

			comment = "Adjust this value to change the durability of the armor. This number is multiplied by 13, 15, 16, 11 for the Boots, Leggings, Chestplate, and Helmet, respectively. Don't ask, those are Minecraft defaults.";
			material.maxDamageFactor = config.get(category, "Max Damage Factor", material.maxDamageFactor, comment).getInt();

			comment = "This is the raw amount of protection for each piece of armor in this order: Boots, Leggings, Chestplate, Helmet. The maximum effective value here is 20 TOTAL.";
			material.damageReductionAmountArray = config.get(category, "DamageReduction", material.damageReductionAmountArray, comment, 0, 20, true, 4).getIntList();
			material.enchantability = config.get(category, "Enchantability", material.enchantability).getInt();

			comment = "Attribute which reduces damage from high damage attacks. Minecraft combat is weird and non-intuitive. Diamond Armor is 2.0, for reference.";
			material.toughness = config.getFloat("Toughness", category, material.toughness, 0.0F, 20.0F, comment);

			boolean disableMaterial = false;
			comment = "If TRUE, all Armor for this Material will be disabled.";
			disableMaterial = config.get(category, "Disable All", disableMaterial, comment).getBoolean();

			category = "Armor." + titleCase(name);
			boolean enableDefault = true;

			enable[0] = config.get(category, "Helmet", enableDefault).getBoolean() && !disableMaterial && !disableAll[0];
			enable[1] = config.get(category, "Chestplate", enableDefault).getBoolean() && !disableMaterial && !disableAll[1];
			enable[2] = config.get(category, "Legs", enableDefault).getBoolean() && !disableMaterial && !disableAll[2];
			enable[3] = config.get(category, "Boots", enableDefault).getBoolean() && !disableMaterial && !disableAll[3];
		}

		void registerItems() {

			config();
			boolean ingotExists = OreDictHelper.oreNameExists(ingot);
			String[] textures = { ID_QUARTERMASTERY + ":textures/armor/" + name + "_1.png", ID_QUARTERMASTERY + ":textures/armor/" + name + "_2.png" };

			armorHelmet = registerItem("helmet_" + name, new ItemArmorCoFH(material, EntityEquipmentSlot.HEAD).setArmorTextures(textures).setRepairItem(ingot).showInCreativeTab(enable[0] && ingotExists));
			armorChestplate = registerItem("chestplate_" + name, new ItemArmorCoFH(material, EntityEquipmentSlot.CHEST).setArmorTextures(textures).setRepairItem(ingot).showInCreativeTab(enable[1] && ingotExists));
			armorLegs = registerItem("leggings_" + name, new ItemArmorCoFH(material, EntityEquipmentSlot.LEGS).setArmorTextures(textures).setRepairItem(ingot).showInCreativeTab(enable[2] && ingotExists));
			armorBoots = registerItem("boots_" + name, new ItemArmorCoFH(material, EntityEquipmentSlot.FEET).setArmorTextures(textures).setRepairItem(ingot).showInCreativeTab(enable[3] && ingotExists));
		}

		void registerRecipes() {

			if (enable[0]) {
				addShapedRecipe(armorHelmet, "III", "I I", 'I', ingot);
			}
			if (enable[1]) {
				addShapedRecipe(armorChestplate, "I I", "III", "III", 'I', ingot);
			}
			if (enable[2]) {
				addShapedRecipe(armorLegs, "III", "I I", "I I", 'I', ingot);
			}
			if (enable[3]) {
				addShapedRecipe(armorBoots, "I I", "I I", 'I', ingot);
			}
		}

		// region HELPERS
		private ItemStack registerItem(String itemName, Item item) {

			item.setRegistryName(ID_QUARTERMASTERY, itemName);
			item.setUnlocalizedName(ID_QUARTERMASTERY + "." + itemName);
			ForgeRegistries.ITEMS.register(item);

			itemList.add(item);

			return new ItemStack(item);
		}

		@SideOnly (Side.CLIENT)
		public void registerModels() {

			if (GEN_JSON_FILES) {
				for (Item item : itemList) {
					if (item instanceof IModelRegister) {
						((IModelRegister) item).generateModelFiles();
					}
				}
			}
			for (Item item : itemList) {
				if (item instanceof IModelRegister) {
					((IModelRegister) item).registerModel();
				}
			}
		}
		// endregion
	}
	// endregion
}
