package cofh.redstonearsenal.block;

import cofh.core.block.storage.BlockStorageMetal;
import cofh.lib.util.helpers.BaublesHelper;
import cofh.lib.util.helpers.DamageHelper;
import cofh.lib.util.helpers.EnergyHelper;
import com.google.common.collect.Iterables;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStorageFlux extends BlockStorageMetal {

	private static final AxisAlignedBB COLLISION_AABB = new AxisAlignedBB(0.00390625D, 0.00390625D, 0.00390625D, 0.99609375D, 0.99609375D, 0.99609375D);

	private int charge = 100;
	private float damage = 1.0F;

	public BlockStorageFlux setCharge(int charge) {

		this.charge = charge;
		return this;
	}

	public BlockStorageFlux setDamage(float damage) {

		this.damage = damage;
		return this;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		entity.attackEntityFrom(DamageHelper.FLUX, damage);

		if (charge > 0 && entity instanceof EntityPlayerMP) {
			Iterable<ItemStack> equipment = Iterables.concat(entity.getEquipmentAndArmor(), BaublesHelper.getBaubles(entity));
			for (ItemStack stack : equipment) {
				EnergyHelper.attemptItemCharge(stack, charge, false);
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {

		return COLLISION_AABB;
	}

}
