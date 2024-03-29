package cofh.core.item;

import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.SecurityHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.TAG_ACTIVE;
import static cofh.lib.util.helpers.StringHelper.canLocalize;
import static cofh.lib.util.helpers.StringHelper.getInfoText;

public class ItemCoFH extends Item implements IModelRegister {

	public static int ACTIVE_DURATION = 40;

	protected EnumRarity rarity = EnumRarity.COMMON;
	protected String group;
	protected String info;
	protected boolean showInCreativeTab = true;
	protected boolean creative;

	public ItemCoFH() {

		this("");
	}

	public ItemCoFH(String group) {

		this.group = group;
	}

	public ItemCoFH setRarity(EnumRarity rarity) {

		this.rarity = rarity;
		return this;
	}

	public ItemCoFH showInCreativeTab(boolean showInCreativeTab) {

		this.showInCreativeTab = showInCreativeTab;
		return this;
	}

	public ItemCoFH setGroup(String group) {

		this.group = group;
		return this;
	}

	public ItemCoFH setCreative(boolean creative) {

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
	@SideOnly (Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (info == null && this.getRegistryName() != null) {
			info = "info." + this.getRegistryName().getResourceDomain() + "." + this.getRegistryName().getResourcePath();
		}
		if (canLocalize(info)) {
			tooltip.add(getInfoText(info));
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (showInCreativeTab) {
			super.getSubItems(tab, items);
		}
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {

		return SecurityHelper.hasSecurity(stack);
	}

	@Override
	public String getHighlightTip(ItemStack stack, String displayName) {

		if (isActive(stack)) {
			return "";
		}
		return displayName;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return rarity;
	}

	// region HELPERS
	public boolean isActive(ItemStack stack) {

		return stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_ACTIVE);
	}

	public void setActive(ItemStack stack, EntityLivingBase living) {

		if (stack.hasTagCompound()) {
			stack.getTagCompound().setLong(TAG_ACTIVE, living.world.getTotalWorldTime() + ACTIVE_DURATION);
		}
	}

	public void clearActive(ItemStack stack) {

		if (stack.hasTagCompound()) {
			stack.getTagCompound().removeTag(TAG_ACTIVE);
		}
	}

	public boolean isCreative(ItemStack stack) {

		if (stack.getItem() instanceof ItemCoFH) {
			return ((ItemCoFH) stack.getItem()).isCreative();
		}
		return false;
	}
	// endregion

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
