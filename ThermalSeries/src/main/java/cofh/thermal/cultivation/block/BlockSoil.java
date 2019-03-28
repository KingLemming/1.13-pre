package cofh.thermal.cultivation.block;

import cofh.core.block.BlockCoFH;
import cofh.lib.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static cofh.lib.util.Constants.TILLED;
import static net.minecraftforge.common.EnumPlantType.Crop;
import static net.minecraftforge.common.EnumPlantType.Water;

public class BlockSoil extends BlockCoFH {

	public static final PropertyInteger INFUSED = PropertyInteger.create("infused", 0, 3);

	protected static final AxisAlignedBB FARMLAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);

	public BlockSoil() {

		this(Material.GROUND.getMaterialMapColor());
	}

	public BlockSoil(MapColor color) {

		this(Material.GROUND, color);
	}

	public BlockSoil(Material blockMaterial, MapColor color) {

		super(blockMaterial, color);

		setLightOpacity(255);
		setSoundType(SoundType.GROUND);
		setTickRandomly(true);

		MinecraftForge.EVENT_BUS.register(this);
	}

	protected void turnToDirt(World world, BlockPos pos) {

		IBlockState state = world.getBlockState(pos).withProperty(TILLED, false);
		world.setBlockState(pos, state);
	}

	protected PropertyInteger getBoostProperty() {

		return INFUSED;
	}

	protected boolean hasCrop(World world, BlockPos pos) {

		Block block = world.getBlockState(pos.up()).getBlock();
		return block instanceof IPlantable && canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, (IPlantable) block);
	}

	protected int getBoost(IBlockState state) {

		return state.getValue(getBoostProperty());
	}

	protected boolean isTilled(IBlockState state) {

		return state.getValue(TILLED);
	}

	@Override
	protected BlockStateContainer createBlockState() {

		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		builder.add(getBoostProperty());
		builder.add(TILLED);
		return builder.build();
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

		if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
			turnToDirt(world, pos);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (Utils.isClientWorld(world)) {
			return;
		}
		if (hasCrop(world, pos)) {
			IBlockState plantState;
			Block plantBlock;
			BlockPos above = pos.up();

			for (int i = 0; i < getBoost(state); i++) {
				plantState = world.getBlockState(above);
				plantBlock = plantState.getBlock();

				if (plantBlock instanceof IPlantable) {
					plantBlock.updateTick(world, above, plantState, rand);
				}
			}
		}
	}

	@Override
	public boolean isFertile(World world, BlockPos pos) {

		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		return isTilled(state) ? FARMLAND_AABB : FULL_BLOCK_AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state) {

		return !isTilled(state);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {

		return !isTilled(state);
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {

		EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));
		return plantType == Crop ? isTilled(state) : plantType != Water;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

	}

	// region EVENT HANDLING
	@SubscribeEvent
	public void handleUseHoeEvent(UseHoeEvent event) {

		World world = event.getWorld();
		IBlockState state = world.getBlockState(event.getPos());

		if (state.getBlock() == this) {
			if (!isTilled(state)) {
				world.setBlockState(event.getPos(), state.withProperty(TILLED, true));
				event.setResult(Result.ALLOW);
			} else {
				event.setCanceled(true);
			}
		}
	}
	// endregion

	// region 1.12
	@Override
	public int getMetaFromState(IBlockState state) {

		return state.getValue(getBoostProperty()) + (isTilled(state) ? 8 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		return getDefaultState().withProperty(getBoostProperty(), meta & 7).withProperty(TILLED, meta >= 8);
	}
	// endregion
}
