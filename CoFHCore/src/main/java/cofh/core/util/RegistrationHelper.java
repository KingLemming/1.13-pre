package cofh.core.util;

import cofh.core.fluid.BlockFluidCoFH;
import cofh.lib.util.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

import static cofh.lib.util.Constants.GEN_JSON_FILES;

public class RegistrationHelper {

	final String modId;
	final CreativeTabs defaultBlockTab;
	final CreativeTabs defaultItemTab;

	public RegistrationHelper(String modId, CreativeTabs blockTab, CreativeTabs itemTab) {

		this.modId = modId;
		this.defaultBlockTab = blockTab;
		this.defaultItemTab = itemTab;
	}

	// region BLOCKS
	public Block registerBlock(String blockName, Block block, CreativeTabs tab) {

		block.setCreativeTab(tab);
		block.setRegistryName(blockName);
		block.setUnlocalizedName(modId + "." + blockName);
		ForgeRegistries.BLOCKS.register(block);

		if (block instanceof BlockFluidCoFH) {
			fluidList.add((BlockFluidCoFH) block);
		}
		return block;
	}

	public Block registerBlock(String blockName, Block block) {

		return registerBlock(blockName, block, defaultBlockTab);
	}

	public ItemStack registerBlock(String blockName, ItemBlock itemBlock, CreativeTabs tab) {

		Block block = itemBlock.getBlock();

		block.setCreativeTab(tab);
		block.setRegistryName(blockName);
		block.setUnlocalizedName(modId + "." + blockName);
		ForgeRegistries.BLOCKS.register(block);

		itemBlock.setRegistryName(blockName);
		ForgeRegistries.ITEMS.register(itemBlock);

		if (itemBlock instanceof IModelRegister) {
			blockList.add((IModelRegister) itemBlock);
		}
		return new ItemStack(block);
	}

	public ItemStack registerBlock(String blockName, ItemBlock itemBlock) {

		return registerBlock(blockName, itemBlock, defaultBlockTab);
	}

	public ItemStack registerBlock(String blockName, String oreName, ItemBlock itemBlock, CreativeTabs tab) {

		ItemStack retStack = registerBlock(blockName, itemBlock, tab);
		OreDictionary.registerOre(oreName, retStack);
		return retStack;
	}

	public ItemStack registerBlock(String blockName, String oreName, ItemBlock itemBlock) {

		return registerBlock(blockName, oreName, itemBlock, defaultBlockTab);
	}

	public void registerBlockModels() {

		if (GEN_JSON_FILES) {
			for (IModelRegister block : blockList) {
				block.generateModelFiles();
			}
		}
		for (IModelRegister block : blockList) {
			block.registerModel();
		}
	}

	public void registerFluidModels() {

		for (BlockFluidCoFH fluid : fluidList) {
			fluid.registerModel();
		}
	}
	// endregion

	// region ITEMS
	public ItemStack registerItem(String itemName, Item item, CreativeTabs tab) {

		item.setCreativeTab(tab);
		item.setRegistryName(modId, itemName);
		item.setUnlocalizedName(modId + "." + itemName);
		ForgeRegistries.ITEMS.register(item);

		if (item instanceof IModelRegister) {
			itemList.add((IModelRegister) item);
		}
		return new ItemStack(item);
	}

	public ItemStack registerItem(String itemName, Item item) {

		return registerItem(itemName, item, defaultItemTab);
	}

	public ItemStack registerItem(String itemName, String oreName, Item item, CreativeTabs tab) {

		ItemStack retStack = registerItem(itemName, item, tab);
		OreDictionary.registerOre(oreName, retStack);
		return retStack;
	}

	public ItemStack registerItem(String itemName, String oreName, Item item) {

		return registerItem(itemName, oreName, item, defaultItemTab);
	}

	public void registerItemModels() {

		if (GEN_JSON_FILES) {
			for (IModelRegister item : itemList) {
				item.generateModelFiles();
			}
		}
		for (IModelRegister item : itemList) {
			item.registerModel();
		}
	}
	// endregion

	public ArrayList<IModelRegister> blockList = new ArrayList<>();
	public ArrayList<IModelRegister> itemList = new ArrayList<>();
	public ArrayList<BlockFluidCoFH> fluidList = new ArrayList<>();

}
