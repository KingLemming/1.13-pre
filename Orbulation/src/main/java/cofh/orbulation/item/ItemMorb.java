package cofh.orbulation.item;

import cofh.core.item.ItemCoFH;
import cofh.lib.util.Utils;
import cofh.orbulation.entity.projectile.EntityMorb;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.helpers.StringHelper.*;
import static cofh.orbulation.util.MorbUtils.GENERIC;
import static cofh.orbulation.util.MorbUtils.MORB_LIST;

public class ItemMorb extends ItemCoFH {

	public static final int TINT_INDEX_1 = 1;
	public static final int TINT_INDEX_2 = 2;

	private final boolean reusable;

	public ItemMorb(boolean reusable) {

		this.reusable = reusable;
	}

	public boolean reusable() {

		return reusable;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		tooltip.add(getInfoText("info.orbulation.morb.0"));
		tooltip.add(localize("info.orbulation.morb.a." + (reusable ? 1 : 0)));

		if (stack.getTagCompound() == null) {
			tooltip.add(localize("info.orbulation.morb.b.0"));
		} else {
			if (stack.getTagCompound().hasKey(GENERIC)) {
				tooltip.add(getNoticeText("info.orbulation.morb.1"));
			}
			tooltip.add(localize("info.orbulation.morb.b.1"));
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (isInCreativeTab(tab)) {
			items.add(new ItemStack(this));
			if (reusable) {
				items.addAll(MORB_LIST);
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {

		String entityName = "info.cofh.empty";
		String openParen = " (";
		String closeParen = END + ")";

		if (stack.getTagCompound() != null) {
			String entityId = stack.getTagCompound().getString("id");
			EntityEntry entry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityId));
			if (entry != null) {
				entityName = "entity." + entry.getName() + ".name";
			}
		}
		return super.getItemStackDisplayName(stack) + openParen + localize(entityName) + closeParen;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		NBTTagCompound nbt = new NBTTagCompound();
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		}
		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}
		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (Utils.isServerWorld(world)) {
			EntityMorb morb = new EntityMorb(world, player, reusable, nbt);
			morb.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
			world.spawnEntity(morb);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	// region IItemColor
	public int colorMultiplier(ItemStack stack, int tintIndex) {

		EntityList.EntityEggInfo info = null;

		if (stack.hasTagCompound()) {
			ResourceLocation id = new ResourceLocation(stack.getTagCompound().getString("id"));
			info = EntityList.ENTITY_EGGS.get(id);
		}
		if (info != null) {
			switch (tintIndex) {
				case TINT_INDEX_1:
					return info.primaryColor;
				case TINT_INDEX_2:
					return info.secondaryColor;
			}
		}
		return 0xFFFFFF;
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("full=%s", stack.getTagCompound() != null && stack.getTagCompound().hasKey("id") ? 1 : 0)));

		ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("full=%s", 0)));
		ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("full=%s", 1)));
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/" + path + "\"},\"transform\":\"forge:default-item\"},\"variants\":{\"full\":{\"0\":{},\"1\":{\"textures\":{\"layer1\":\"" + domain + ":items/morb_mask_1\",\"layer2\":\"" + domain + ":items/morb_mask_2\"}}}}}";

		try {
			File itemModel = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(itemModel, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
