package cofh.core.item;

import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

import static cofh.core.util.CoreUtils.configDir;

public class ItemFoodCoFH extends ItemFood implements IModelRegister {

	protected EnumRarity rarity = EnumRarity.COMMON;
	protected String group;
	protected boolean showInCreativeTab = true;
	protected boolean creative;

	public ItemFoodCoFH(int amount, float saturation) {

		this(amount, saturation, "");
	}

	public ItemFoodCoFH(int amount, float saturation, String group) {

		super(amount, saturation, false);
		this.group = group;
	}

	public ItemFoodCoFH(int amount, float saturation, boolean isWolfFood) {

		this(amount, saturation, isWolfFood, "");
	}

	public ItemFoodCoFH(int amount, float saturation, boolean isWolfFood, String group) {

		super(amount, saturation, isWolfFood);
		this.group = group;
	}

	public ItemFoodCoFH setRarity(EnumRarity rarity) {

		this.rarity = rarity;
		return this;
	}

	public ItemFoodCoFH showInCreativeTab(boolean showInCreativeTab) {

		this.showInCreativeTab = showInCreativeTab;
		return this;
	}

	public ItemFoodCoFH setGroup(String group) {

		this.group = group;
		return this;
	}

	public ItemFoodCoFH setCreative(boolean creative) {

		this.creative = creative;
		return this;
	}

	public final String getGroup() {

		return group;
	}

	public final boolean isCreative() {

		return creative;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (showInCreativeTab) {
			super.getSubItems(tab, items);
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return rarity;
	}

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		if (group.isEmpty()) {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		} else {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().getResourceDomain() + ":" + group + "/" + getRegistryName().getResourcePath(), "inventory"));
		}
	}

	@Override
	public void generateModelFiles() {

		String group = getGroup().isEmpty() ? "" : getGroup() + "/";
		String model = "{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"" + getRegistryName().getResourceDomain() + ":items/" + group + getRegistryName().getResourcePath() + "\"}}";

		try {
			File itemModel = new File(configDir, "/dev/" + getRegistryName().getResourceDomain() + "/models/item/" + group + getRegistryName().getResourcePath() + ".json");
			FileUtils.writeStringToFile(itemModel, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
