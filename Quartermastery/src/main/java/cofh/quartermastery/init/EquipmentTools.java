package cofh.quartermastery.init;

import cofh.core.item.tool.*;
import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
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
import static cofh.lib.util.Constants.ID_QUARTERMASTERY;
import static cofh.lib.util.helpers.RecipeHelper.addShapedRecipe;
import static cofh.lib.util.helpers.StringHelper.titleCase;
import static cofh.quartermastery.Quartermastery.config;

public class EquipmentTools {

	private EquipmentTools() {

	}

	// region REGISTRATION
	public static void config() {

		String category = "Tools";

		disableAll[0] = config.get(category, "Disable All Swords", disableAll[0]).getBoolean();
		disableAll[1] = config.get(category, "Disable All Shovels", disableAll[1]).getBoolean();
		disableAll[2] = config.get(category, "Disable All Pickaxes", disableAll[2]).getBoolean();
		disableAll[3] = config.get(category, "Disable All Axes", disableAll[3]).getBoolean();
		disableAll[4] = config.get(category, "Disable All Hoes", disableAll[4]).getBoolean();
		disableAll[5] = config.get(category, "Disable All Excavators", disableAll[5]).getBoolean();
		disableAll[6] = config.get(category, "Disable All Hammers", disableAll[6]).getBoolean();
		disableAll[7] = config.get(category, "Disable All Sickles", disableAll[7]).getBoolean();
		disableAll[8] = config.get(category, "Disable All Shears", disableAll[8]).getBoolean();
		disableAll[9] = config.get(category, "Disable All Bows", disableAll[9]).getBoolean();
		disableAll[10] = config.get(category, "Disable All Fishing Rods", disableAll[10]).getBoolean();
		disableAll[11] = config.get(category, "Disable All Shields", disableAll[11]).getBoolean();
	}

	public static void registerItems() {

		config();

		for (ToolSet set : ToolSet.values()) {
			set.registerItems();
		}
	}

	public static void registerRecipes() {

		for (ToolSet set : ToolSet.values()) {
			set.registerRecipes();
		}
	}

	@SideOnly (Side.CLIENT)
	public static void registerModels() {

		for (ToolSet set : ToolSet.values()) {
			set.registerModels();
		}
	}
	// endregion

	public static ToolMaterial toolMaterialObsidian = EnumHelper.addToolMaterial("QM:OBSIDIAN", 1, 75, 8.0F, 3.0F, 12);
	public static ToolMaterial toolMaterialEmerald = EnumHelper.addToolMaterial("QM:EMERALD", 2, 150, 10.0F, 1.5F, 18);

	public static ToolMaterial toolMaterialCopper = EnumHelper.addToolMaterial("QM:COPPER", 1, 175, 4.0F, 1.0F, 7);
	public static ToolMaterial toolMaterialTin = EnumHelper.addToolMaterial("QM:TIN", 1, 150, 4.5F, 1.0F, 7);
	public static ToolMaterial toolMaterialLead = EnumHelper.addToolMaterial("QM:LEAD", 1, 100, 5.0F, 1.0F, 9);
	public static ToolMaterial toolMaterialSilver = EnumHelper.addToolMaterial("QM:SILVER", 1, 75, 6.0F, 1.0F, 25);
	public static ToolMaterial toolMaterialNickel = EnumHelper.addToolMaterial("QM:NICKEL", 2, 300, 6.5F, 2.5F, 18);
	public static ToolMaterial toolMaterialPlatinum = EnumHelper.addToolMaterial("QM:PLATINUM", 4, 1400, 9.0F, 3.5F, 16);

	public static ToolMaterial toolMaterialAluminum = EnumHelper.addToolMaterial("QM:ALUMINUM", 1, 225, 6.0F, 1.0F, 12);

	public static ToolMaterial toolMaterialSteel = EnumHelper.addToolMaterial("QM:STEEL", 2, 400, 6.5F, 2.5F, 10);
	public static ToolMaterial toolMaterialBronze = EnumHelper.addToolMaterial("QM:BRONZE", 2, 325, 6.0F, 2.0F, 10);
	public static ToolMaterial toolMaterialInvar = EnumHelper.addToolMaterial("QM:INVAR", 2, 425, 6.5F, 2.5F, 12);
	public static ToolMaterial toolMaterialElectrum = EnumHelper.addToolMaterial("QM:ELECTRUM", 0, 100, 14.0F, 0.5F, 30);
	public static ToolMaterial toolMaterialConstantan = EnumHelper.addToolMaterial("QM:CONSTANTAN", 2, 275, 6.0F, 1.5F, 12);

	public static boolean disableAll[] = new boolean[12];

	// TODO: Finish - Art
	// region TOOL SET
	public enum ToolSet {

		// @formatter:off
		WOOD("wood", "plankWood", ToolMaterial.WOOD, true),
		STONE("stone", "cobblestone", ToolMaterial.STONE, true),
		IRON("iron", "ingotIron", ToolMaterial.IRON, true),
		DIAMOND("diamond", "gemDiamond", ToolMaterial.DIAMOND, true),
		GOLD("gold", "ingotGold", ToolMaterial.GOLD, true),

//		OBSIDIAN("obsidian", "obsidian", toolMaterialObsidian),
//		EMERALD("emerald", "gemEmerald", toolMaterialEmerald),

		COPPER("copper", "ingotCopper", toolMaterialCopper),
		TIN("tin", "ingotTin", toolMaterialTin),
		LEAD("lead", "ingotLead", toolMaterialLead),
		SILVER("silver", "ingotSilver", toolMaterialSilver),
		NICKEL("nickel", "ingotNickel", toolMaterialNickel),
		PLATINUM("platinum", "ingotPlatinum", toolMaterialPlatinum),

//		ALUMINUM("aluminum", "ingotAluminum", toolMaterialAluminum),

		STEEL("steel", "ingotSteel", toolMaterialSteel),
		BRONZE("bronze", "ingotBronze", toolMaterialBronze),
		INVAR("invar", "ingotInvar", toolMaterialInvar),
		ELECTRUM("electrum", "ingotElectrum", toolMaterialElectrum),
		CONSTANTAN("constantan", "ingotConstantan", toolMaterialConstantan);
		// @formatter:on

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

		public ItemStack toolShears;
		public ItemStack toolBow;
		public ItemStack toolFishingRod;
		public ItemStack toolShield;

		public boolean[] enable = new boolean[12];
		public boolean vanilla = false;

		ToolSet(String name, String ingot, ToolMaterial material) {

			this.name = name.toLowerCase(Locale.ROOT);
			this.ingot = ingot;
			this.material = material;
		}

		ToolSet(String name, String ingot, ToolMaterial material, boolean vanilla) {

			this.name = name.toLowerCase(Locale.ROOT);
			this.ingot = ingot;
			this.material = material;
			this.vanilla = vanilla;
		}

		void config() {

			String category = "Tools." + titleCase(name) + ".Material";

			material.harvestLevel = config.get(category, "Harvest Level", material.harvestLevel).getInt();
			material.maxUses = config.get(category, "Max Uses", material.maxUses).getInt();
			material.efficiency = (float) config.get(category, "Efficiency", material.efficiency).getDouble();
			material.attackDamage = (float) config.get(category, "Attack Damage", material.attackDamage).getDouble();
			material.enchantability = config.get(category, "Enchantability", material.enchantability).getInt();

			boolean disableMaterial = this == WOOD || this == STONE;
			String comment = "If TRUE, all Tools for this Material will be disabled.";
			disableMaterial = config.get(category, "Disable All", disableMaterial, comment).getBoolean();

			category = "Tools." + titleCase(name);
			boolean enableDefault = true;

			if (!vanilla) {
				enable[0] = config.get(category, "Sword", enableDefault).getBoolean() && !disableMaterial && !disableAll[0];
				enable[1] = config.get(category, "Shovel", enableDefault).getBoolean() && !disableMaterial && !disableAll[1];
				enable[2] = config.get(category, "Pickaxe", enableDefault).getBoolean() && !disableMaterial && !disableAll[2];
				enable[3] = config.get(category, "Axe", enableDefault).getBoolean() && !disableMaterial && !disableAll[3];
				enable[4] = config.get(category, "Hoe", enableDefault).getBoolean() && !disableMaterial && !disableAll[4];
			}
			enable[5] = config.get(category, "Excavator", enableDefault).getBoolean() && !disableMaterial && !disableAll[5];
			enable[6] = config.get(category, "Hammer", enableDefault).getBoolean() && !disableMaterial && !disableAll[6];
			enable[7] = config.get(category, "Sickle", enableDefault).getBoolean() && !disableMaterial && !disableAll[7];

			if (!(this == IRON)) {
				enable[8] = config.get(category, "Shears", enableDefault).getBoolean() && !disableMaterial && !disableAll[8];
			}
			if (!(this == WOOD)) {
				enable[9] = config.get(category, "Bow", enableDefault).getBoolean() && !disableMaterial && !disableAll[9];
				enable[10] = config.get(category, "FishingRod", enableDefault).getBoolean() && !disableMaterial && !disableAll[10];
				enable[11] = config.get(category, "Shield", enableDefault).getBoolean() && !disableMaterial && !disableAll[11];
			}
		}

		void registerItems() {

			config();
			boolean ingotExists = OreDictHelper.oreNameExists(ingot);

			if (!vanilla) {
				toolSword = registerItem("sword_" + name, new ItemSwordCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[0] && ingotExists));
				toolShovel = registerItem("shovel_" + name, new ItemShovelCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[1] && ingotExists));
				toolPickaxe = registerItem("pickaxe_" + name, new ItemPickaxeCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[2] && ingotExists));
				toolAxe = registerItem("axe_" + name, new ItemAxeCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[3] && ingotExists));
				toolHoe = registerItem("hoe_" + name, new ItemHoeCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[4] && ingotExists));
			}
			toolExcavator = registerItem("excavator_" + name, new ItemExcavatorCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[5] && ingotExists));
			toolHammer = registerItem("hammer_" + name, new ItemHammerCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[6] && ingotExists));
			toolSickle = registerItem("sickle_" + name, new ItemSickleCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[7] && ingotExists));

			if (!(this == IRON)) {
				toolShears = registerItem("shears_" + name, new ItemShearsCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[8] && ingotExists));
			}
			if (!(this == WOOD)) {
				toolBow = registerItem("bow_" + name, new ItemBowCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[9] && ingotExists));
				toolFishingRod = registerItem("fishing_rod_" + name, new ItemFishingRodCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[10] && ingotExists));
				toolShield = registerItem("shield_" + name, new ItemShieldCoFH(material).setRepairItem(ingot).showInCreativeTab(enable[11] && ingotExists));
			}
		}

		void registerRecipes() {

			if (!vanilla) {
				if (enable[0]) {
					addShapedRecipe(toolSword, " I ", " I ", " R ", 'I', ingot, 'R', "stickWood");
				}
				if (enable[1]) {
					addShapedRecipe(toolShovel, " I ", " R ", " R ", 'I', ingot, 'R', "stickWood");
				}
				if (enable[2]) {
					addShapedRecipe(toolPickaxe, "III", " R ", " R ", 'I', ingot, 'R', "stickWood");
				}
				if (enable[3]) {
					addShapedRecipe(toolAxe, "II", "IS", " S", 'I', ingot, 'S', "stickWood");
				}
				if (enable[4]) {
					addShapedRecipe(toolHoe, "II", " S", " S", 'I', ingot, 'S', "stickWood");
				}
			}

			if (enable[5]) {
				addShapedRecipe(toolExcavator, " I ", "ISI", " S ", 'I', ingot, 'S', "stickWood");
			}
			if (enable[6]) {
				addShapedRecipe(toolHammer, "III", "ISI", " S ", 'I', ingot, 'S', "stickWood");
			}
			if (enable[7]) {
				addShapedRecipe(toolSickle, " I ", "  I", "SI ", 'I', ingot, 'S', "stickWood");
			}

			if (enable[8]) {
				addShapedRecipe(toolShears, " I", "I ", 'I', ingot);
			}
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

			item.setRegistryName(ID_QUARTERMASTERY, itemName);
			item.setUnlocalizedName(ID_QUARTERMASTERY + "." + itemName);
			ForgeRegistries.ITEMS.register(item);

			itemList.add(item);

			return new ItemStack(item);
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
			String modelShield = "{\"textures\":{\"all\":\"" + ID_QUARTERMASTERY + ":items/tools/shield_iron\"},\"elements\":[{\"from\":[0,0,11],\"to\":[12,22,12],\"faces\":{\"down\":{\"uv\":[3.25,0,6.25,0.25],\"texture\":\"#all\"},\"up\":{\"uv\":[0.25,0,3.25,0.25],\"texture\":\"#all\"},\"north\":{\"uv\":[3.5,0.25,6.5,5.75],\"texture\":\"#all\"},\"south\":{\"uv\":[0.25,0.25,3.25,5.75],\"texture\":\"#all\"},\"west\":{\"uv\":[0,0.25,0.25,5.75],\"texture\":\"#all\"},\"east\":{\"uv\":[3.25,0.25,3.5,5.75],\"texture\":\"#all\"}}},{\"from\":[5,8,5],\"to\":[7,14,11],\"faces\":{\"down\":{\"uv\":[8.5,0,9,1.5],\"texture\":\"#all\"},\"up\":{\"uv\":[8,0,8.5,1.5],\"texture\":\"#all\"},\"north\":{\"uv\":[8,1.5,8.5,3],\"texture\":\"#all\"},\"west\":{\"uv\":[8.5,1.5,10,3],\"texture\":\"#all\"},\"east\":{\"uv\":[6.5,1.5,8,3],\"texture\":\"#all\"}}}],\"display\":{\"thirdperson_righthand\":{\"rotation\":[0,90,0],\"translation\":[0.5,-5,2],\"scale\":[1,1,1]},\"thirdperson_lefthand\":{\"rotation\":[0,90,0],\"translation\":[0.5,-5,6],\"scale\":[1,1,1]},\"firstperson_righthand\":{\"rotation\":[0,180,5],\"translation\":[-2,-19,0],\"scale\":[1.65,1.65,1.65]},\"firstperson_lefthand\":{\"rotation\":[0,180,5],\"translation\":[4.5,-19.5,2],\"scale\":[1.65,1.65,1.65]},\"gui\":{\"rotation\":[15,-25,-5],\"translation\":[2,-2,0],\"scale\":[0.65,0.65,0.65]},\"head\":{\"rotation\":[0,0,0],\"translation\":[0,0,0],\"scale\":[1,1,1]},\"fixed\":{\"rotation\":[0,180,0],\"translation\":[1,-1.5,0],\"scale\":[0.5,0.5,0.5]},\"ground\":{\"rotation\":[0,0,0],\"translation\":[0,3,0],\"scale\":[0.25,0.25,0.25]}},\"overrides\":[{\"predicate\":{\"blocking\":1},\"model\":\"" + ID_QUARTERMASTERY + ":item/tools/shield_blocking\"}]}";
			String modelShieldBlock = "{\"parent\":\"" + ID_QUARTERMASTERY + ":item/tools/shield\",\"display\":{\"thirdperson_righthand\":{\"rotation\":[40,135,5],\"translation\":[-1,-4,-2],\"scale\":[1,1,1]},\"thirdperson_lefthand\":{\"rotation\":[45,135,0],\"translation\":[1.5,-5.5,0],\"scale\":[1,1,1]},\"firstperson_righthand\":{\"rotation\":[0,180,-5],\"translation\":[-6,-14,0],\"scale\":[1.65,1.65,1.65]},\"firstperson_lefthand\":{\"rotation\":[0,180,-5],\"translation\":[4,-14,0],\"scale\":[1.65,1.65,1.65]}}}";

			try {
				File itemModelBow = new File(configDir, "/dev/" + ID_QUARTERMASTERY + "/models/item/tools/bow.json");
				FileUtils.writeStringToFile(itemModelBow, Utils.createPrettyJSON(modelBow), Charset.forName("UTF-8"));

				File itemModelShield = new File(configDir, "/dev/" + ID_QUARTERMASTERY + "/models/item/tools/shield.json");
				FileUtils.writeStringToFile(itemModelShield, Utils.createPrettyJSON(modelShield), Charset.forName("UTF-8"));

				File itemModelShieldBlock = new File(configDir, "/dev/" + ID_QUARTERMASTERY + "/models/item/tools/shield_blocking.json");
				FileUtils.writeStringToFile(itemModelShieldBlock, Utils.createPrettyJSON(modelShieldBlock), Charset.forName("UTF-8"));
			} catch (Throwable t) {
				// pokemon!
			}

		}
		// endregion
	}
	// endregion
}
