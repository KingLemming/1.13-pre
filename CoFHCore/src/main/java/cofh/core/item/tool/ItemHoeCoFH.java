package cofh.core.item.tool;

import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

import static cofh.core.util.CoreUtils.configDir;

public class ItemHoeCoFH extends ItemHoe implements IModelRegister {

	protected String repairItem = "";
	protected int enchantability;
	protected boolean showInCreativeTab = true;

	public ItemHoeCoFH(ToolMaterial toolMaterial) {

		super(toolMaterial);

		this.enchantability = toolMaterial.getEnchantability();
	}

	public ItemHoeCoFH setRepairItem(String repairItem) {

		this.repairItem = repairItem;
		return this;
	}

	public ItemHoeCoFH showInCreativeTab(boolean showInCreativeTab) {

		this.showInCreativeTab = showInCreativeTab;
		return this;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (showInCreativeTab) {
			super.getSubItems(tab, items);
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {

		return OreDictHelper.isOreNameEqual(stack, repairItem);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {

		return true;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {

		return enchantability;
	}

	// region IModelRegister
	@Override
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().getResourceDomain() + ":tools/" + getRegistryName().getResourcePath(), "inventory"));
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void generateModelFiles() {

		String model = "{\"parent\":\"item/handheld\",\"textures\":{\"layer0\":\"" + getRegistryName().getResourceDomain() + ":items/tools/" + getRegistryName().getResourcePath() + "\"}}";

		try {
			File itemModel = new File(configDir, "/dev/" + getRegistryName().getResourceDomain() + "/models/item/tools/" + getRegistryName().getResourcePath() + ".json");
			FileUtils.writeStringToFile(itemModel, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
