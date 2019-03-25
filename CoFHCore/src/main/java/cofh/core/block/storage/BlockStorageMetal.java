package cofh.core.block.storage;

import cofh.core.block.BlockCoFH;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static cofh.lib.util.Constants.TOOL_PICKAXE;

public class BlockStorageMetal extends BlockCoFH {

	public BlockStorageMetal() {

		this(Material.IRON.getMaterialMapColor());
	}

	public BlockStorageMetal(MapColor color) {

		super(Material.IRON, color);

		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);

		setHarvestParams(TOOL_PICKAXE, 2);
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {

		return false;
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {

		return true;
	}

}
