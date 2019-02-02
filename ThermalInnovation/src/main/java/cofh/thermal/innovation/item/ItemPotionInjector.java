package cofh.thermal.innovation.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.ColorHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermal.core.item.ItemPotionContainer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.TINT_INDEX_2;
import static cofh.lib.util.helpers.StringHelper.*;

@Optional.Interface (iface = "baubles.api.IBauble", modid = "baubles")
public class ItemPotionInjector extends ItemPotionContainer implements IMultiModeItem, IBauble {

	public static final int MB_PER_CYCLE = 50;
	public static final int MB_PER_USE = 250;
	public static final int TIME_CONSTANT = 32;

	protected static boolean enableEnchantEffect = true;

	public ItemPotionInjector(int fluidCapacity) {

		super(fluidCapacity);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		tooltip.add(getInfoText("info.thermal.potion_injector.0"));
		tooltip.add(localize("info.thermal.potion_injector.1"));
		tooltip.add(getNoticeText("info.thermal.potion_injector.2"));
		tooltip.add(localizeFormat("info.thermal.potion_injector.a." + getMode(stack), getKeyName(KeyMultiModeItem.INSTANCE.getKey())));

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {

		if (Utils.isClientWorld(world) || world.getTotalWorldTime() % TIME_CONSTANT != 0) {
			return;
		}
		if (!(entity instanceof EntityLivingBase) || Utils.isFakePlayer(entity) || getMode(stack) == 0) {
			return;
		}
		EntityLivingBase living = (EntityLivingBase) entity;
		FluidStack fluid = getFluid(stack);
		if (fluid != null && fluid.amount >= MB_PER_CYCLE) {
			boolean used = false;
			for (PotionEffect effect : PotionUtils.getEffectsFromTag(fluid.tag)) {
				PotionEffect active = living.getActivePotionMap().get(effect.getPotion());

				if (active != null && active.getDuration() >= 40) {
					continue;
				}
				if (effect.getPotion().isInstant()) {
					effect.getPotion().affectEntity(null, null, (EntityLivingBase) entity, effect.getAmplifier(), 0.5D);
				} else {
					PotionEffect potion = new PotionEffect(effect.getPotion(), effect.getDuration() / 4, effect.getAmplifier(), effect.getIsAmbient(), false);
					living.addPotionEffect(potion);
				}
				used = true;
			}
			if (used) {
				drain(stack, MB_PER_CYCLE, true);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking()) {
			if (Utils.isServerWorld(world)) {
				FluidStack fluid = getFluid(stack);
				if (fluid != null && fluid.amount >= MB_PER_USE) {
					for (PotionEffect effect : PotionUtils.getEffectsFromTag(fluid.tag)) {
						if (effect.getPotion().isInstant()) {
							effect.getPotion().affectEntity(null, null, player, effect.getAmplifier(), 1.0D);
						} else {
							PotionEffect potion = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), false);
							player.addPotionEffect(potion);
						}
					}
					drain(stack, MB_PER_USE, true);
				}
			}
			player.swingArm(hand);
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {

		FluidStack fluid = getFluid(stack);
		if (fluid != null && fluid.amount >= MB_PER_USE) {
			if (Utils.isServerWorld(entity.world)) {
				for (PotionEffect effect : PotionUtils.getEffectsFromTag(fluid.tag)) {
					if (effect.getPotion().isInstant()) {
						effect.getPotion().affectEntity(player, player, entity, effect.getAmplifier(), 0.5D);
					} else {
						PotionEffect potion = new PotionEffect(effect.getPotion(), effect.getDuration() / 2, effect.getAmplifier(), effect.getIsAmbient(), true);
						entity.addPotionEffect(potion);
					}
				}
				drain(stack, MB_PER_USE, true);
			}
			player.swingArm(hand);
			return true;
		}
		return false;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {

		return enableEnchantEffect && stack.isItemEnchanted();
	}

	// region IBauble
	@Override
	public BaubleType getBaubleType(ItemStack stack) {

		return BaubleType.TRINKET;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {

		World world = player.world;
		if (Utils.isClientWorld(world) || world.getTotalWorldTime() % TIME_CONSTANT != 0) {
			return;
		}
		if (Utils.isFakePlayer(player) || getMode(stack) == 0) {
			return;
		}
		FluidStack fluid = getFluid(stack);
		if (fluid != null && fluid.amount >= MB_PER_CYCLE) {
			boolean used = false;
			for (PotionEffect effect : PotionUtils.getEffectsFromTag(fluid.tag)) {
				PotionEffect active = player.getActivePotionMap().get(effect.getPotion());
				if (active != null && active.getDuration() >= 40) {
					continue;
				}
				if (effect.getPotion().isInstant()) {
					effect.getPotion().affectEntity(null, null, player, effect.getAmplifier(), 0.5D);
				} else {
					PotionEffect potion = new PotionEffect(effect.getPotion(), effect.getDuration() / 4, effect.getAmplifier(), effect.getIsAmbient(), false);
					player.addPotionEffect(potion);
				}
				used = true;
			}
			if (used) {
				drain(stack, MB_PER_CYCLE, true);
			}
		}
	}

	@Override
	public boolean willAutoSync(ItemStack stack, EntityLivingBase player) {

		return true;
	}
	// endregion

	// region IMultiModeItem
	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		int mode = getMode(stack);
		if (mode == 1) {
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 0.4F, 1.0F);
		} else {
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.6F);
		}
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.thermal.potion_injector.b." + mode));
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("color=%s,fill=%s", ColorHelper.hasColor(stack, TINT_INDEX_2) ? 1 : 0, MathHelper.clamp(getFluidAmount(stack) > 0 ? 1 + getScaledFluidStored(stack, 7) : 0, 0, 7))));

		for (int color = 0; color < 2; color++) {
			for (int fill = 0; fill < 8; fill++) {
				ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("color=%s,fill=%s", color, fill)));
			}
		}
	}

	@Override
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/utils/potion_injector/" + path + "\",\"layer1\":\"" + domain + ":items/blank\",\"layer2\":\"" + domain + ":items/blank\"},\"transform\":{\"thirdperson_righthand\":{\"rotation\":[{\"y\":90},{\"z\":30}],\"translation\":[0,-0.15,0],\"scale\":[0.85,0.85,0.85]},\"thirdperson_lefthand\":{\"rotation\":[{\"y\":-90},{\"z\":-30}],\"translation\":[0,-0.15,0],\"scale\":[0.85,0.85,0.85]},\"firstperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":55}],\"translation\":[0,0.25,0.1],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":-55}],\"translation\":[0,0.25,0.1],\"scale\":[0.68,0.68,0.68]},\"ground\":{\"translation\":[0,0.15,0],\"scale\":[0.5,0.5,0.5]}}},\"variants\":{\"color\":{\"0\":{},\"1\":{\"textures\":{\"layer2\":\"" + domain + ":items/utils/potion_injector/potion_injector_color\"}}},\"fill\":{\"0\":{},\"1\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_injector/potion_injector_fill_1\"}},\"2\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_injector/potion_injector_fill_2\"}},\"3\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_injector/potion_injector_fill_3\"}},\"4\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_injector/potion_injector_fill_4\"}},\"5\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_injector/potion_injector_fill_5\"}},\"6\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_injector/potion_injector_fill_6\"}},\"7\":{\"textures\":{\"layer1\":\"" + domain + ":items/utils/potion_injector/potion_injector_fill_7\"}}}}}";

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
