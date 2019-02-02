package cofh.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Mostly a sneaky way to reduce some stupid but useful boilerplate. :)
 */
public interface ITileCallback {

	Block block();

	BlockPos pos();

	World world();

	int invSize();

	default void callBlockUpdate() {

		if (world() == null) {
			return;
		}
		IBlockState state = world().getBlockState(pos());
		world().notifyBlockUpdate(pos(), state, state, 3);
	}

	default void callNeighborStateChange() {

		if (world() == null) {
			return;
		}
		world().notifyNeighborsOfStateChange(pos(), block(), false);
	}

	default void updateComparatorOutputLevel() {

		if (world() == null) {
			return;
		}
		world().updateComparatorOutputLevel(pos(), block());
	}

	default void onInventoryChange(int slot) {

	}

	default void onControlUpdate() {

	}

	default void onRedstoneUpdate() {

	}

	default void onWrench() {

	}

}
