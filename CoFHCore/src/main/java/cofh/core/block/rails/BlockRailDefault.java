package cofh.core.block.rails;

import cofh.lib.block.IDismantleable;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;

import static cofh.lib.util.Constants.RAIL_DEFAULT;

public class BlockRailDefault extends BlockRailBase implements IDismantleable {

	protected float maxSpeed = 0.4F;

	public BlockRailDefault() {

		this(false);
	}

	public BlockRailDefault(boolean isPowered) {

		super(isPowered);
		setSoundType(SoundType.METAL);
	}

	public BlockRailDefault setLightLevel(int value) {

		this.lightValue = value;
		return this;
	}

	public BlockRailDefault setSoundType(SoundType sound) {

		this.blockSoundType = sound;
		return this;
	}

	public BlockRailDefault setMaxSpeed(float maxSpeed) {

		this.maxSpeed = MathHelper.clamp(maxSpeed, 0F, 1F);
		return this;
	}

	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {

		return RAIL_DEFAULT;
	}

	@Override
	protected BlockStateContainer createBlockState() {

		BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
		builder.add(getShapeProperty());
		return builder.build();
	}

	@Override
	public boolean isFlexibleRail(IBlockAccess world, BlockPos pos) {

		return !this.isPowered;
	}

	@Override
	public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {

		return true;
	}

	@Override
	public EnumRailDirection getRailDirection(IBlockAccess world, BlockPos pos, IBlockState state, @Nullable EntityMinecart cart) {

		return state.getValue(getShapeProperty());
	}

	@Override
	public float getRailMaxSpeed(World world, EntityMinecart cart, BlockPos pos) {

		return maxSpeed;
	}

	@Override
	public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {

	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {

		IProperty<EnumRailDirection> shape = getShapeProperty();

		switch (rot) {
			case CLOCKWISE_180:
				switch (state.getValue(shape)) {
					case ASCENDING_EAST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_WEST);
					case ASCENDING_WEST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_EAST);
					case ASCENDING_NORTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_SOUTH);
					case ASCENDING_SOUTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_NORTH);
					case SOUTH_EAST:
						return state.withProperty(shape, EnumRailDirection.NORTH_WEST);
					case SOUTH_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_EAST);
					case NORTH_WEST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_EAST);
					case NORTH_EAST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_WEST);
				}
			case COUNTERCLOCKWISE_90:
				switch (state.getValue(shape)) {
					case ASCENDING_EAST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_NORTH);
					case ASCENDING_WEST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_SOUTH);
					case ASCENDING_NORTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_WEST);
					case ASCENDING_SOUTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_EAST);
					case SOUTH_EAST:
						return state.withProperty(shape, EnumRailDirection.NORTH_EAST);
					case SOUTH_WEST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_EAST);
					case NORTH_WEST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_WEST);
					case NORTH_EAST:
						return state.withProperty(shape, EnumRailDirection.NORTH_WEST);
					case NORTH_SOUTH:
						return state.withProperty(shape, EnumRailDirection.EAST_WEST);
					case EAST_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_SOUTH);
				}
			case CLOCKWISE_90:
				switch (state.getValue(shape)) {
					case ASCENDING_EAST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_SOUTH);
					case ASCENDING_WEST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_NORTH);
					case ASCENDING_NORTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_EAST);
					case ASCENDING_SOUTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_WEST);
					case SOUTH_EAST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_WEST);
					case SOUTH_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_WEST);
					case NORTH_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_EAST);
					case NORTH_EAST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_EAST);
					case NORTH_SOUTH:
						return state.withProperty(shape, EnumRailDirection.EAST_WEST);
					case EAST_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_SOUTH);
				}
			default:
				return state;
		}
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {

		IProperty<EnumRailDirection> shape = getShapeProperty();
		EnumRailDirection direction = state.getValue(shape);

		switch (mirrorIn) {
			case LEFT_RIGHT:
				switch (direction) {
					case ASCENDING_NORTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_SOUTH);
					case ASCENDING_SOUTH:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_NORTH);
					case SOUTH_EAST:
						return state.withProperty(shape, EnumRailDirection.NORTH_EAST);
					case SOUTH_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_WEST);
					case NORTH_WEST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_WEST);
					case NORTH_EAST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_EAST);
					default:
						return super.withMirror(state, mirrorIn);
				}

			case FRONT_BACK:
				switch (direction) {
					case ASCENDING_EAST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_WEST);
					case ASCENDING_WEST:
						return state.withProperty(shape, EnumRailDirection.ASCENDING_EAST);
					case ASCENDING_NORTH:
					case ASCENDING_SOUTH:
					default:
						break;
					case SOUTH_EAST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_WEST);
					case SOUTH_WEST:
						return state.withProperty(shape, EnumRailDirection.SOUTH_EAST);
					case NORTH_WEST:
						return state.withProperty(shape, EnumRailDirection.NORTH_EAST);
					case NORTH_EAST:
						return state.withProperty(shape, EnumRailDirection.NORTH_WEST);
				}
		}
		return super.withMirror(state, mirrorIn);
	}

	// region 1.12
	@Override
	public int getMetaFromState(IBlockState state) {

		return (state.getValue(getShapeProperty())).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		return this.getDefaultState().withProperty(getShapeProperty(), EnumRailDirection.byMetadata(meta));
	}
	// endregion

	// region IDismantleable
	@Override
	public ArrayList<ItemStack> dismantleBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player, boolean returnDrops) {

		ItemStack dropBlock = new ItemStack(this);
		world.setBlockToAir(pos);

		if (!returnDrops) {
			Utils.dropDismantleStackIntoWorld(dropBlock, world, pos);
		}
		ArrayList<ItemStack> ret = new ArrayList<>();
		ret.add(dropBlock);
		return ret;
	}

	@Override
	public boolean canDismantle(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		return true;
	}
	// endregion
}
