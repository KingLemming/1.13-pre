package cofh.core.item.tool;

import cofh.lib.capabilities.EnchantableItemWrapper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.ArrayList;

import static cofh.lib.util.Constants.TOOL_SICKLE;
import static cofh.lib.util.modhelpers.EnsorcellmentHelper.FARMER;

public class ItemSickleCoFH extends ItemToolCoFH {

	protected int radius = 3;

	public ItemSickleCoFH(ToolMaterial toolMaterial) {

		super(2.5F, -2.6F, toolMaterial);

		setHarvestLevel(TOOL_SICKLE, toolMaterial.getHarvestLevel());
		setMaxDamage(toolMaterial.getMaxUses() * 4);

		effectiveMaterials.add(Material.LEAVES);
		effectiveMaterials.add(Material.PLANTS);
		effectiveMaterials.add(Material.VINE);
		effectiveMaterials.add(Material.WEB);
	}

	public ItemSickleCoFH setRadius(int radius) {

		this.radius = radius;
		return this;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.canApply(new ItemStack(Items.IRON_AXE));
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

		World world = player.world;
		IBlockState state = world.getBlockState(pos);

		if (!canHarvestBlock(state, stack)) {
			if (!player.capabilities.isCreativeMode) {
				stack.damageItem(1, player);
			}
			return false;
		}
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int used = 0;
		if (player.isSneaking()) {
			if (!player.capabilities.isCreativeMode) {
				stack.damageItem(1, player);
			}
			return false;
		}
		for (int i = x - radius; i <= x + radius; i++) {
			for (int k = z - radius; k <= z + radius; k++) {
				if (harvestBlock(world, new BlockPos(i, y, k), player)) {
					used++;
				}
			}
		}
		if (used > 0 && !player.capabilities.isCreativeMode) {
			stack.damageItem(used, player);
		}
		return false;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

		ArrayList<ResourceLocation> enchants = new ArrayList<>();

		if (FARMER != null) {
			enchants.add(FARMER.getRegistryName());
		}
		return new EnchantableItemWrapper(enchants);
	}

}
