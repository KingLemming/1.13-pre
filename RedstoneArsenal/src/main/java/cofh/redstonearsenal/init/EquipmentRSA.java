package cofh.redstonearsenal.init;

import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.oredict.OreDictHelper;
import cofh.redstonearsenal.item.ItemArmorFlux;
import cofh.redstonearsenal.item.tool.*;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.GEN_JSON_FILES;
import static cofh.lib.util.Constants.ID_REDSTONE_ARSENAL;
import static cofh.lib.util.helpers.RecipeHelper.addShapedRecipe;
import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.redstonearsenal.RedstoneArsenal.config;
import static cofh.redstonearsenal.init.ItemsRSA.rodObsidianFlux;

public class EquipmentRSA {

	private EquipmentRSA() {

	}

	public static void config() {

		disableAllArmor = config.get("Armor", "DisableAllArmor", disableAllArmor).getBoolean();
		disableAllTools = config.get("Tools", "DisableAllTools", disableAllTools).getBoolean();
	}

	// region REGISTRATION
	public static void registerItems() {

		config();

		for (ArmorSet set : ArmorSet.values()) {
			set.registerItems();
		}
		for (ToolSet set : ToolSet.values()) {
			set.registerItems();
		}
	}

	public static void registerRecipes() {

		for (ArmorSet set : ArmorSet.values()) {
			set.registerRecipes();
		}
		for (ToolSet set : ToolSet.values()) {
			set.registerRecipes();
		}
	}

	@SideOnly (Side.CLIENT)
	public static void registerModels() {

		for (ArmorSet set : ArmorSet.values()) {
			set.registerModels();
		}
		for (ToolSet set : ToolSet.values()) {
			set.registerModels();
		}
	}
	// endregion

	public static ArmorMaterial armorMaterialFluxInfused = EnumHelper.addArmorMaterial("RA:FLUX_INFUSED", "flux_infused_armor", 100, new int[] { 3, 6, 8, 3 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
	public static ToolMaterial toolMaterialFluxInfused = EnumHelper.addToolMaterial("RA:FLUX_INFUSED", 4, 100, 8.0F, 0, 25);

	public static boolean disableAllArmor;
	public static boolean disableAllTools;

	// region ARMOR SET
	public enum ArmorSet {

		FLUX_INFUSED("flux_infused", "ingotFluxInfused", armorMaterialFluxInfused);

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

			comment = "This is the raw amount of protection for each piece of armor in this order: Boots, Leggings, Chestplate, Helmet. The maximum effective value here is 20 TOTAL.";
			material.damageReductionAmountArray = config.get(category, "DamageReduction", material.damageReductionAmountArray, comment, 0, 20, true, 4).getIntList();
			material.enchantability = config.get(category, "Enchantability", material.enchantability).getInt();

			comment = "Attribute which reduces damage from high damage attacks. Minecraft combat is weird and non-intuitive. Diamond Armor is 2.0, for reference.";
			material.toughness = config.getFloat("Toughness", category, material.toughness, 0.0F, 20.0F, comment);

			category = "Armor." + titleCase(name);
			boolean enableDefault = true;

			enable[0] = config.get(category, "Helmet", enableDefault).getBoolean() && !disableAllArmor;
			enable[1] = config.get(category, "Chestplate", enableDefault).getBoolean() && !disableAllArmor;
			enable[2] = config.get(category, "Legs", enableDefault).getBoolean() && !disableAllArmor;
			enable[3] = config.get(category, "Boots", enableDefault).getBoolean() && !disableAllArmor;
		}

		void registerItems() {

			config();
			boolean ingotExists = OreDictHelper.oreNameExists(ingot);
			String[] textures = { ID_REDSTONE_ARSENAL + ":textures/armor/" + name + "_1.png", ID_REDSTONE_ARSENAL + ":textures/armor/" + name + "_2.png" };

			armorHelmet = registerItem("helmet_" + name, new ItemArmorFlux(material, EntityEquipmentSlot.HEAD).setArmorTextures(textures).setRepairItem(ingot).showInCreativeTab(enable[0] && ingotExists));
			armorChestplate = registerItem("chestplate_" + name, new ItemArmorFlux(material, EntityEquipmentSlot.CHEST).setArmorTextures(textures).setRepairItem(ingot).showInCreativeTab(enable[1] && ingotExists));
			armorLegs = registerItem("leggings_" + name, new ItemArmorFlux(material, EntityEquipmentSlot.LEGS).setArmorTextures(textures).setRepairItem(ingot).showInCreativeTab(enable[2] && ingotExists));
			armorBoots = registerItem("boots_" + name, new ItemArmorFlux(material, EntityEquipmentSlot.FEET).setArmorTextures(textures).setRepairItem(ingot).showInCreativeTab(enable[3] && ingotExists));
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

			item.setRegistryName(ID_REDSTONE_ARSENAL, itemName);
			item.setUnlocalizedName(ID_REDSTONE_ARSENAL + "." + itemName);
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

	// region TOOL SET
	public enum ToolSet {

		FLUX_INFUSED("flux_infused", "ingotFluxInfused", toolMaterialFluxInfused);

		private final String name;
		private final String ingot;
		private final ToolMaterial material;

		public ArrayList<Item> itemList = new ArrayList<>();

		/* REFERENCES */
		public ItemStack toolSword;
		public ItemStack toolShovel;
		public ItemStack toolPickaxe;
		public ItemStack toolAxe;
		public ItemStack toolHoe;

		public ItemStack toolExcavator;
		public ItemStack toolHammer;
		public ItemStack toolSickle;

		public ItemStack toolBow;
		public ItemStack toolFishingRod;
		public ItemStack toolShield;

		public ItemStack toolWrench;
		public ItemStack toolBattleWrench;

		public boolean[] enable = new boolean[13];

		ToolSet(String name, String ingot, ToolMaterial material) {

			this.name = name.toLowerCase(Locale.ROOT);
			this.ingot = ingot;
			this.material = material;
		}

		void config() {

			String category = "Tools." + titleCase(name) + ".Material";

			material.harvestLevel = config.get(category, "HarvestLevel", material.harvestLevel).getInt();
			material.efficiency = (float) config.get(category, "Efficiency", material.efficiency).getDouble();
			material.enchantability = config.get(category, "Enchantability", material.enchantability).getInt();

			category = "Tools." + titleCase(name);
			boolean enableDefault = true;

			enable[0] = config.get(category, "Sword", enableDefault).getBoolean() && !disableAllTools;
			enable[1] = config.get(category, "Shovel", enableDefault).getBoolean() && !disableAllTools;
			enable[2] = config.get(category, "Pickaxe", enableDefault).getBoolean() && !disableAllTools;
			enable[3] = config.get(category, "Axe", enableDefault).getBoolean() && !disableAllTools;
			enable[4] = config.get(category, "Hoe", enableDefault).getBoolean() && !disableAllTools;

			enable[5] = config.get(category, "Excavator", enableDefault).getBoolean() && !disableAllTools;
			enable[6] = config.get(category, "Hammer", enableDefault).getBoolean() && !disableAllTools;
			enable[7] = config.get(category, "Sickle", enableDefault).getBoolean() && !disableAllTools;

			enable[9] = config.get(category, "Bow", enableDefault).getBoolean() && !disableAllTools;
			enable[10] = config.get(category, "FishingRod", enableDefault).getBoolean() && !disableAllTools;
			enable[11] = config.get(category, "Shield", enableDefault).getBoolean() && !disableAllTools;

			enable[8] = config.get(category, "Wrench", enableDefault).getBoolean() && !disableAllTools;
			enable[12] = config.get(category, "BattleWrench", enableDefault).getBoolean() && !disableAllTools;
		}

		void registerItems() {

			config();

			toolSword = registerItem("sword_" + name, new ItemSwordFlux(material).showInCreativeTab(enable[0]));
			toolShovel = registerItem("shovel_" + name, new ItemShovelFlux(material).showInCreativeTab(enable[1]));
			toolPickaxe = registerItem("pickaxe_" + name, new ItemPickaxeFlux(material).showInCreativeTab(enable[2]));
			toolAxe = registerItem("axe_" + name, new ItemAxeFlux(material).showInCreativeTab(enable[3]));
			// toolHoe = registerItem("hoe_" + name, new ItemHoeFlux(material).showInCreativeTab(enable[4]));

			toolExcavator = registerItem("excavator_" + name, new ItemExcavatorFlux(material).showInCreativeTab(enable[5]));
			toolHammer = registerItem("hammer_" + name, new ItemHammerFlux(material).showInCreativeTab(enable[6]));
			toolSickle = registerItem("sickle_" + name, new ItemSickleFlux(material).showInCreativeTab(enable[7]));

			toolBow = registerItem("bow_" + name, new ItemBowFlux(material).showInCreativeTab(enable[9]));
			toolFishingRod = registerItem("fishing_rod_" + name, new ItemFishingRodFlux(material).showInCreativeTab(enable[10]));
			toolShield = registerItem("shield_" + name, new ItemShieldFlux(material).showInCreativeTab(enable[11]));
		}

		public void registerRecipes() {

			if (enable[0]) {
				addShapedRecipe(toolSword, " I ", " I ", " S ", 'I', ingot, 'S', rodObsidianFlux);
			}
			if (enable[1]) {
				addShapedRecipe(toolShovel, " I ", " S ", " S ", 'I', ingot, 'S', rodObsidianFlux);
			}
			if (enable[2]) {
				addShapedRecipe(toolPickaxe, "III", " S ", " S ", 'I', ingot, 'S', rodObsidianFlux);
			}
			if (enable[3]) {
				addShapedRecipe(toolAxe, "II", "IS", " S", 'I', ingot, 'S', rodObsidianFlux);
			}
			//			if (enable[4]) {
			//				addShapedRecipe(toolHoe, "II", " S", " S", 'I', ingot, 'S', rodObsidianFlux);
			//			}

			if (enable[5]) {
				addShapedRecipe(toolExcavator, " I ", "ISI", " S ", 'I', ingot, 'S', rodObsidianFlux);
			}
			if (enable[6]) {
				addShapedRecipe(toolHammer, "III", "ISI", " S ", 'I', ingot, 'S', rodObsidianFlux);
			}
			if (enable[7]) {
				addShapedRecipe(toolSickle, " I ", "  I", "SI ", 'I', ingot, 'S', rodObsidianFlux);
			}

			//			if (enable[8]) {
			//				addShapedRecipe(toolShears, " I", "I ", 'I', ingot);
			//			}
			if (enable[9]) {
				addShapedRecipe(toolBow, " I ", "IB ", " I ", 'I', ingot, 'B', Items.BOW);
			}
			if (enable[10]) {
				addShapedRecipe(toolFishingRod, "I  ", " R ", "  I", 'I', ingot, 'R', Items.FISHING_ROD);
			}
			if (enable[11]) {
				addShapedRecipe(toolShield, "III", "ISI", " I ", 'I', ingot, 'S', Items.SHIELD);
			}
		}

		// region HELPERS
		private ItemStack registerItem(String itemName, Item item) {

			item.setRegistryName(ID_REDSTONE_ARSENAL, itemName);
			item.setUnlocalizedName(ID_REDSTONE_ARSENAL + "." + itemName);
			ForgeRegistries.ITEMS.register(item);

			itemList.add(item);

			return EnergyHelper.setDefaultEnergyTag(new ItemStack(item), 0);
		}

		@SideOnly (Side.CLIENT)
		public void registerModels() {

			if (GEN_JSON_FILES) {
				generateBaseFiles();
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

		private void generateBaseFiles() {

			String modelBow = "{\"parent\":\"builtin/generated\",\"display\":{\"thirdperson_righthand\":{\"rotation\":[-80,260,-40],\"translation\":[-1,-2,2.5],\"scale\":[0.9,0.9,0.9]},\"thirdperson_lefthand\":{\"rotation\":[-80,-280,40],\"translation\":[-1,-2,2.5],\"scale\":[0.9,0.9,0.9]},\"firstperson_righthand\":{\"rotation\":[0,-90,25],\"translation\":[1.13,3.2,1.13],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[0,90,-25],\"translation\":[1.13,3.2,1.13],\"scale\":[0.68,0.68,0.68]},\"ground\":{\"translation\":[0,2,0],\"scale\":[0.5,0.5,0.5]}}}";
			String modelShield = "{\"textures\":{\"all\":\"" + ID_REDSTONE_ARSENAL + ":items/tools/shield_flux_infused\"},\"elements\":[{\"from\":[0,0,11],\"to\":[12,22,12],\"faces\":{\"down\":{\"uv\":[3.25,0,6.25,0.25],\"texture\":\"#all\"},\"up\":{\"uv\":[0.25,0,3.25,0.25],\"texture\":\"#all\"},\"north\":{\"uv\":[3.5,0.25,6.5,5.75],\"texture\":\"#all\"},\"south\":{\"uv\":[0.25,0.25,3.25,5.75],\"texture\":\"#all\"},\"west\":{\"uv\":[0,0.25,0.25,5.75],\"texture\":\"#all\"},\"east\":{\"uv\":[3.25,0.25,3.5,5.75],\"texture\":\"#all\"}}},{\"from\":[5,8,5],\"to\":[7,14,11],\"faces\":{\"down\":{\"uv\":[8.5,0,9,1.5],\"texture\":\"#all\"},\"up\":{\"uv\":[8,0,8.5,1.5],\"texture\":\"#all\"},\"north\":{\"uv\":[8,1.5,8.5,3],\"texture\":\"#all\"},\"west\":{\"uv\":[8.5,1.5,10,3],\"texture\":\"#all\"},\"east\":{\"uv\":[6.5,1.5,8,3],\"texture\":\"#all\"}}}],\"display\":{\"thirdperson_righthand\":{\"rotation\":[0,90,0],\"translation\":[0.5,-5,2],\"scale\":[1,1,1]},\"thirdperson_lefthand\":{\"rotation\":[0,90,0],\"translation\":[0.5,-5,6],\"scale\":[1,1,1]},\"firstperson_righthand\":{\"rotation\":[0,180,5],\"translation\":[-2,-19,0],\"scale\":[1.65,1.65,1.65]},\"firstperson_lefthand\":{\"rotation\":[0,180,5],\"translation\":[4.5,-19.5,2],\"scale\":[1.65,1.65,1.65]},\"gui\":{\"rotation\":[15,-25,-5],\"translation\":[2,-2,0],\"scale\":[0.65,0.65,0.65]},\"head\":{\"rotation\":[0,0,0],\"translation\":[0,0,0],\"scale\":[1,1,1]},\"fixed\":{\"rotation\":[0,180,0],\"translation\":[1,-1.5,0],\"scale\":[0.5,0.5,0.5]},\"ground\":{\"rotation\":[0,0,0],\"translation\":[0,3,0],\"scale\":[0.25,0.25,0.25]}},\"overrides\":[{\"predicate\":{\"blocking\":1},\"model\":\"" + ID_REDSTONE_ARSENAL + ":item/tools/shield_blocking\"}]}";
			String modelShieldBlock = "{\"parent\":\"" + ID_REDSTONE_ARSENAL + ":item/tools/shield\",\"display\":{\"thirdperson_righthand\":{\"rotation\":[40,135,5],\"translation\":[-1,-4,-2],\"scale\":[1,1,1]},\"thirdperson_lefthand\":{\"rotation\":[45,135,0],\"translation\":[1.5,-5.5,0],\"scale\":[1,1,1]},\"firstperson_righthand\":{\"rotation\":[0,180,-5],\"translation\":[-6,-14,0],\"scale\":[1.65,1.65,1.65]},\"firstperson_lefthand\":{\"rotation\":[0,180,-5],\"translation\":[4,-14,0],\"scale\":[1.65,1.65,1.65]}}}";

			try {
				File itemModelBow = new File(configDir, "/dev/" + ID_REDSTONE_ARSENAL + "/models/item/tools/bow.json");
				FileUtils.writeStringToFile(itemModelBow, Utils.createPrettyJSON(modelBow), Charset.forName("UTF-8"));

				File itemModelShield = new File(configDir, "/dev/" + ID_REDSTONE_ARSENAL + "/models/item/tools/shield.json");
				FileUtils.writeStringToFile(itemModelShield, Utils.createPrettyJSON(modelShield), Charset.forName("UTF-8"));

				File itemModelShieldBlock = new File(configDir, "/dev/" + ID_REDSTONE_ARSENAL + "/models/item/tools/shield_blocking.json");
				FileUtils.writeStringToFile(itemModelShieldBlock, Utils.createPrettyJSON(modelShieldBlock), Charset.forName("UTF-8"));
			} catch (Throwable t) {
				// pokemon!
			}

		}
		// endregion
	}
	// endregion
}
