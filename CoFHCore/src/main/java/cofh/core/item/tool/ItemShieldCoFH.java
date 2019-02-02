package cofh.core.item.tool;

import cofh.core.item.ItemCoFH;
import cofh.lib.item.IToolShield;
import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;

import static cofh.core.util.CoreUtils.configDir;

public class ItemShieldCoFH extends ItemCoFH implements IToolShield, IModelRegister {

	protected String repairItem = "";
	protected int enchantability;
	protected boolean showInCreativeTab = true;

	public ItemShieldCoFH(ToolMaterial toolMaterial) {

		setCreativeTab(CreativeTabs.COMBAT);
		setMaxStackSize(1);
		setMaxDamage(toolMaterial.getMaxUses() + 275);

		this.enchantability = toolMaterial.getEnchantability();

		addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
			@SideOnly (Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {

				return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
	}

	public ItemShieldCoFH setRepairItem(String repairItem) {

		this.repairItem = repairItem;
		return this;
	}

	public ItemShieldCoFH showInCreativeTab(boolean showInCreativeTab) {

		this.showInCreativeTab = showInCreativeTab;
		return this;
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
	public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {

		return true;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {

		return enchantability;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {

		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {

		return EnumAction.BLOCK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {

		playerIn.setActiveHand(hand);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
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

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();

		String modelBase = "{\"parent\":\"" + domain + ":item/tools/shield\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "\"},\"overrides\":[{\"predicate\":{\"blocking\":1},\"model\":\"" + domain + ":item/tools/" + path + "_blocking\"}]}";
		String modelBlock = "{\"parent\":\"" + domain + ":item/tools/shield_blocking\",\"textures\":{\"all\":\"" + domain + ":items/tools/" + path + "\"}}";

		try {
			File itemModelBase = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + ".json");
			FileUtils.writeStringToFile(itemModelBase, Utils.createPrettyJSON(modelBase), Charset.forName("UTF-8"));

			File itemModelBlock = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + "_blocking.json");
			FileUtils.writeStringToFile(itemModelBlock, Utils.createPrettyJSON(modelBlock), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
