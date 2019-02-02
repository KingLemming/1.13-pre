package cofh.core.item;

import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

import static cofh.core.util.CoreUtils.configDir;

public class ItemBlockCoFH extends ItemBlock implements IModelRegister {

	protected EnumRarity rarity = EnumRarity.COMMON;
	protected String group;
	protected boolean showInCreativeTab = true;

	public ItemBlockCoFH(Block block) {

		super(block);
		this.group = "";
	}

	public ItemBlockCoFH(Block block, String group) {

		super(block);
		this.group = group;
	}

	public ItemBlockCoFH setRarity(EnumRarity rarity) {

		this.rarity = rarity;
		return this;
	}

	public ItemBlockCoFH showInCreativeTab(boolean showInCreativeTab) {

		this.showInCreativeTab = showInCreativeTab;
		return this;
	}

	public final String getGroup() {

		return group;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (showInCreativeTab) {
			super.getSubItems(tab, items);
		}
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {

		// return SecurityHelper.isSecure(stack);
		return false;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return rarity;
	}

	// region IModelRegister
	@Override
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		if (group.isEmpty()) {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.block.getRegistryName(), "normal"));
		} else {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.block.getRegistryName().getResourceDomain() + ":" + group + "/" + this.block.getRegistryName().getResourcePath(), "normal"));
		}
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(getBlockstateString()), Charset.forName("UTF-8"));

			File blockModel = new File(configDir, "/dev/" + domain + "/models/block/" + getGroup() + "/" + path + ".json");
			FileUtils.writeStringToFile(blockModel, Utils.createPrettyJSON(getBlockModelString()), Charset.forName("UTF-8"));

			File itemModel = new File(configDir, "/dev/" + domain + "/models/item/" + getGroup() + "/" + path + ".json");
			FileUtils.writeStringToFile(itemModel, Utils.createPrettyJSON(getItemModelString()), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}

	@SideOnly (Side.CLIENT)
	public String getBlockstateString() {

		String group = getGroup().isEmpty() ? "" : getGroup() + "/";
		return "{\"variants\":{\"normal\":{\"model\":\"" + getRegistryName().getResourceDomain() + ":" + group + getRegistryName().getResourcePath() + "\"}}}";
	}

	@SideOnly (Side.CLIENT)
	public String getBlockModelString() {

		String group = getGroup().isEmpty() ? "" : getGroup() + "/";
		return "{\"parent\":\"block/cube_all\",\"textures\":{\"all\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "\"}}";
	}

	@SideOnly (Side.CLIENT)
	public String getItemModelString() {

		String group = getGroup().isEmpty() ? "" : getGroup() + "/";
		return "{\"parent\":\"" + getRegistryName().getResourceDomain() + ":block/" + group + getRegistryName().getResourcePath() + "\"}";
	}
	// endregion
}
