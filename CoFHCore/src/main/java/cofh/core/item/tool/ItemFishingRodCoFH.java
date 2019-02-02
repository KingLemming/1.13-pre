package cofh.core.item.tool;

import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.oredict.OreDictHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;

import static cofh.core.util.CoreUtils.configDir;

public class ItemFishingRodCoFH extends ItemFishingRod implements IModelRegister {

	protected String repairItem = "";
	protected int enchantability;
	protected int luckModifier;
	protected int speedModifier;
	protected boolean showInCreativeTab = true;

	public ItemFishingRodCoFH(ToolMaterial toolMaterial) {

		setMaxStackSize(1);
		setMaxDamage(toolMaterial.getMaxUses() + 5);
		setCreativeTab(CreativeTabs.TOOLS);

		this.enchantability = toolMaterial.getEnchantability();
		this.luckModifier = toolMaterial.getHarvestLevel() / 2;
		this.speedModifier = (int) toolMaterial.getEfficiency() / 3;

		addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter() {

			@SideOnly (Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {

				return entityIn == null ? 0.0F : (entityIn.getHeldItemMainhand() == stack || entityIn.getHeldItemOffhand() == stack) && entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).fishEntity != null ? 1.0F : 0.0F;
			}
		});
	}

	public ItemFishingRodCoFH setRepairItem(String repairItem) {

		this.repairItem = repairItem;
		return this;
	}

	public ItemFishingRodCoFH showInCreativeTab(boolean showInCreativeTab) {

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

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		if (player.fishEntity != null) {
			int i = player.fishEntity.handleHookRetraction();
			stack.damageItem(i, player);
			player.swingArm(hand);
		} else {
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (Utils.isServerWorld(world)) {
				EntityFishHook hook = new EntityFishHook(world, player);

				int enchantSpeed = EnchantmentHelper.getFishingSpeedBonus(stack);
				hook.setLureSpeed(Math.min(speedModifier + enchantSpeed, 5));

				int enchantLuck = EnchantmentHelper.getFishingLuckBonus(stack);
				hook.setLuck(luckModifier + enchantLuck);

				world.spawnEntity(hook);
			}
			player.swingArm(hand);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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

		String modelBase = "{\"parent\":\"item/handheld_rod\",\"textures\":{\"layer0\":\"" + domain + ":items/tools/" + path + "\"},\"overrides\":[{\"predicate\":{\"cast\":1},\"model\":\"" + domain + ":item/tools/" + path + "_cast\"}]}";
		String modelCast = "{\"parent\":\"" + domain + ":item/tools/" + path + "\",\"textures\":{\"layer0\":\"" + domain + ":items/tools/" + path + "_cast\"}}";

		try {
			File itemModelBase = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + ".json");
			FileUtils.writeStringToFile(itemModelBase, Utils.createPrettyJSON(modelBase), Charset.forName("UTF-8"));

			File itemModelCast = new File(configDir, "/dev/" + domain + "/models/item/tools/" + path + "_cast.json");
			FileUtils.writeStringToFile(itemModelCast, Utils.createPrettyJSON(modelCast), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
