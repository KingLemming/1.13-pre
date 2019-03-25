package cofh.core.block;

import cofh.lib.util.RandomDrop;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class BlockCoFH extends Block {

	protected int minXP = 0;
	protected int maxXP = 0;
	protected ArrayList<RandomDrop> dropList = new ArrayList<>();

	public BlockCoFH(Material blockMaterial, MapColor color) {

		super(blockMaterial, color);
	}

	public BlockCoFH setLightLevel(int value) {

		this.lightValue = value;
		return this;
	}

	public BlockCoFH setSoundType(SoundType sound) {

		this.blockSoundType = sound;
		return this;
	}

	public BlockCoFH setHarvestParams(String toolClass, int level) {

		setHarvestLevel(toolClass, level);
		return this;
	}

	public BlockCoFH setXPDrop(int minXP, int maxXP) {

		this.minXP = minXP;
		this.maxXP = maxXP;
		return this;
	}

	public BlockCoFH addDrop(RandomDrop drop) {

		this.dropList.add(drop);
		return this;
	}

	public BlockCoFH addDrops(ArrayList<RandomDrop> drops) {

		this.dropList.addAll(drops);
		return this;
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {

		if (maxXP <= 0) {
			return 0;
		}
		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		return MathHelper.getInt(rand, minXP, maxXP);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		if (!this.dropList.isEmpty()) {
			for (RandomDrop drop : this.dropList) {
				drops.add(drop.getDrop(rand, fortune));
			}
			return;
		}
		int count = quantityDropped(state, fortune, rand);
		for (int i = 0; i < count; i++) {
			Item item = this.getItemDropped(state, rand, fortune);
			if (item != Items.AIR) {
				drops.add(new ItemStack(item, 1, this.damageDropped(state)));
			}
		}
	}

}
