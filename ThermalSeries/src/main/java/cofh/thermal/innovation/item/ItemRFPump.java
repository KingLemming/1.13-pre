package cofh.thermal.innovation.item;

import cofh.core.key.KeyMultiModeItem;
import cofh.core.util.ChatHelper;
import cofh.lib.item.IAreaEffectItem;
import cofh.lib.item.IMultiModeItem;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.ColorHelper;
import cofh.thermal.core.item.ItemRFContainer;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.TAG_ACTIVE;
import static cofh.lib.util.Constants.TINT_INDEX_3;
import static cofh.lib.util.helpers.StringHelper.*;

// TODO: Finish
public class ItemRFPump extends ItemRFContainer implements IAreaEffectItem, IMultiModeItem {

	public ItemRFPump(int maxEnergy, int maxReceive) {

		super(maxEnergy, maxReceive);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		tooltip.add(getInfoText("info.thermal.rf_pump.0"));
		if (displayShiftForDetail && !isShiftKeyDown()) {
			tooltip.add(shiftForDetails());
		}
		if (!isShiftKeyDown()) {
			return;
		}
		tooltip.add(localize("info.thermal.rf_pump.b." + getMode(stack)));
		tooltip.add(localizeFormat("info.thermal.rf_pump.a.0", getKeyName(KeyMultiModeItem.INSTANCE.getKey())));

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {

		if (!isActive(stack)) {
			return;
		}
		long activeTime = stack.getTagCompound().getLong(TAG_ACTIVE);
		if (entity.world.getTotalWorldTime() > activeTime) {
			clearActive(stack);
		}
	}

	// region IAreaEffectItem
	@Override
	public ImmutableList<BlockPos> getAreaEffectBlocks(ItemStack stack, BlockPos pos, EntityPlayer player) {

		ArrayList<BlockPos> area = new ArrayList<>();
		World world = player.getEntityWorld();
		int mode = getMode(stack);

		return ImmutableList.copyOf(area);
	}
	// endregion

	// region IMultimodeItem
	// TODO: Fix
	//	@Override
	//	public int getNumModes(ItemStack stack) {
	//
	//		if (!typeMap.containsKey(ItemHelper.getItemDamage(stack))) {
	//			return 0;
	//		}
	//		return typeMap.get(ItemHelper.getItemDamage(stack)).numModes;
	//	}

	@Override
	public void onModeChange(EntityPlayer player, ItemStack stack) {

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 0.6F, 1.0F - 0.1F * getMode(stack));
		ChatHelper.sendIndexedChatMessageToPlayer(player, new TextComponentTranslation("info.thermal.rf_pump.b." + getMode(stack)));
	}
	// endregion

	// region IModelRegister
	@SideOnly (Side.CLIENT)
	public void registerModel() {

		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s", ColorHelper.hasColor(stack, TINT_INDEX_3) ? 1 : 0, this.getEnergyStored(stack) > 0 ? isActive(stack) ? "active" : "charged" : "drained")));
		String[] states = { "charged", "active", "drained" };

		for (int color = 0; color < 2; color++) {
			for (int state = 0; state < 3; state++) {
				ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), String.format("color=%s,state=%s", color, states[state])));
			}
		}
	}

	@Override
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();
		String model = "{\"forge_marker\":1,\"defaults\":{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + domain + ":items/tools/rf_pump/" + path + "\",\"layer1\":\"" + domain + ":items/tools/rf_pump/rf_pump_drained\",\"layer2\":\"" + domain + ":items/blank\",\"layer3\":\"" + domain + ":items/blank\"},\"transform\":{\"thirdperson_righthand\":{\"rotation\":[{\"x\":90},{\"y\":-90},{\"z\":45}],\"translation\":[0,0.35,0.1],\"scale\":[0.85,0.85,0.85]},\"thirdperson_lefthand\":{\"rotation\":[{\"x\":90},{\"y\":90},{\"z\":-45}],\"translation\":[0,0.35,0.1],\"scale\":[0.85,0.85,0.85]},\"firstperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":25}],\"translation\":[0,0.15,0.12],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":-25}],\"translation\":[0,0.15,0.12],\"scale\":[0.68,0.68,0.68]},\"ground\":{\"translation\":[0,0.15,0],\"scale\":[0.5,0.5,0.5]}}},\"variants\":{\"color\":{\"0\":{},\"1\":{\"textures\":{\"layer3\":\"" + domain + ":items/tools/rf_pump/rf_pump_color\"}}},\"state\":{\"charged\":{\"textures\":{\"layer1\":\"" + domain + ":items/tools/rf_pump/rf_pump_charged\"}},\"active\":{\"textures\":{\"layer1\":\"" + domain + ":items/tools/rf_pump/rf_pump_active_0\",\"layer2\":\"" + domain + ":items/tools/rf_pump/rf_pump_active_1\"},\"transform\":{\"thirdperson_righthand\":{\"rotation\":[{\"x\":90},{\"y\":-90},{\"z\":60}],\"translation\":[0,0.35,0],\"scale\":[0.85,0.85,0.85]},\"thirdperson_lefthand\":{\"rotation\":[{\"x\":90},{\"y\":90},{\"z\":-60}],\"translation\":[0,0.35,0],\"scale\":[0.85,0.85,0.85]},\"firstperson_righthand\":{\"rotation\":[{\"y\":-90},{\"z\":45}],\"translation\":[0,0.1,0.1],\"scale\":[0.68,0.68,0.68]},\"firstperson_lefthand\":{\"rotation\":[{\"y\":90},{\"z\":-45}],\"translation\":[0,0.1,0.1],\"scale\":[0.68,0.68,0.68]}}},\"drained\":{}}}}";

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(model), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}
	// endregion
}
