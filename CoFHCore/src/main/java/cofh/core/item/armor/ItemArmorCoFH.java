package cofh.core.item.armor;

import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.oredict.OreDictHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

import static cofh.core.util.CoreUtils.configDir;

public class ItemArmorCoFH extends ItemArmor implements IModelRegister {

	protected String repairItem = "";
	protected boolean showInCreativeTab = true;

	protected String[] textures = new String[2];
	protected Multimap<String, AttributeModifier> properties = HashMultimap.create();

	public ItemArmorCoFH(ArmorMaterial material, EntityEquipmentSlot type) {

		super(material, 0, type);
	}

	public ItemArmorCoFH setRepairItem(String repairItem) {

		this.repairItem = repairItem;
		return this;
	}

	public ItemArmorCoFH showInCreativeTab(boolean showInCreativeTab) {

		this.showInCreativeTab = showInCreativeTab;
		return this;
	}

	public ItemArmorCoFH setArmorTextures(String[] textures) {

		this.textures = textures;
		return this;
	}

	public ItemArmorCoFH putAttribute(String attribute, AttributeModifier modifier) {

		properties.put(attribute, modifier);
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
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {

		if (slot == EntityEquipmentSlot.LEGS) {
			return textures[1];
		}
		return textures[0];
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

		if (slot == armorType) {
			Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
			map.putAll(properties);
			return map;
		}
		return super.getAttributeModifiers(slot, stack);
	}

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().getResourceDomain() + ":armor/" + getRegistryName().getResourcePath(), "inventory"));
	}

	@Override
	public void generateModelFiles() {

		String model = "{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"" + getRegistryName().getResourceDomain() + ":items/armor/" + getRegistryName().getResourcePath() + "\"}}";

		try {
			File itemModel = new File(configDir, "/dev/" + getRegistryName().getResourceDomain() + "/models/item/armor/" + getRegistryName().getResourcePath() + ".json");
			FileUtils.writeStringToFile(itemModel, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
