package cofh.core.item.tool;

import cofh.lib.util.IModelRegister;
import cofh.lib.util.Utils;
import cofh.lib.util.oredict.OreDictHelper;
import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashSet;

import static cofh.core.util.CoreUtils.configDir;

public abstract class ItemToolCoFH extends ItemTool implements IModelRegister {

	protected String repairItem = "";

	protected THashSet<Material> effectiveMaterials = new THashSet<>();
	protected int harvestLevel;
	protected boolean showInCreativeTab = true;

	public ItemToolCoFH(float baseDamage, float attackSpeed, ToolMaterial toolMaterial) {

		super(baseDamage, attackSpeed, toolMaterial, new HashSet<>());
		harvestLevel = toolMaterial.getHarvestLevel();
	}

	public ItemToolCoFH setRepairItem(String repairItem) {

		this.repairItem = repairItem;
		return this;
	}

	public ItemToolCoFH showInCreativeTab(boolean showInCreativeTab) {

		this.showInCreativeTab = showInCreativeTab;
		return this;
	}

	protected boolean harvestBlock(World world, BlockPos pos, EntityPlayer player) {

		if (world.isAirBlock(pos)) {
			return false;
		}
		EntityPlayerMP playerMP = null;
		if (player instanceof EntityPlayerMP) {
			playerMP = (EntityPlayerMP) player;
		}
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (!effectiveMaterials.contains(state.getMaterial()) || !ForgeHooks.canHarvestBlock(block, player, world, pos)) {
			return false;
		}
		// Send the Break Event
		int xpToDrop = 0;
		if (playerMP != null) {
			xpToDrop = ForgeHooks.onBlockBreakEvent(world, playerMP.interactionManager.getGameType(), playerMP, pos);
			if (xpToDrop == -1) {
				return false;
			}
		}
		if (Utils.isServerWorld(world)) {
			if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
				if (!player.capabilities.isCreativeMode) {
					block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
					if (xpToDrop > 0) {
						block.dropXpOnBlockBreak(world, pos, xpToDrop);
					}
				}
			}
			if (playerMP != null) {
				playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
			}
		} else {
			if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
			}
			NetHandlerPlayClient client = Minecraft.getMinecraft().getConnection();
			if (client != null) {
				Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
			}
		}
		return true;
	}

	protected float getEfficiency(ItemStack stack) {

		return efficiency;
	}

	protected THashSet<Material> getEffectiveMaterials(ItemStack stack) {

		return effectiveMaterials;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (showInCreativeTab) {
			super.getSubItems(tab, items);
		}
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {

		return harvestLevel >= state.getBlock().getHarvestLevel(state) && getDestroySpeed(stack, state) > 1.0F;
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
	public float getDestroySpeed(ItemStack stack, IBlockState state) {

		for (String type : getToolClasses(stack)) {
			if (state.getBlock().isToolEffective(type, state)) {
				return getEfficiency(stack);
			}
		}
		return getEffectiveMaterials(stack).contains(state.getMaterial()) ? getEfficiency(stack) : 1.0F;
	}

	@Override
	public String getToolMaterialName() {

		return super.getToolMaterialName().contains(":") ? super.getToolMaterialName().split(":", 2)[1] : super.getToolMaterialName();
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
