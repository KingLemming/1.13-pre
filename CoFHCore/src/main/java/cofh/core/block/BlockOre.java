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

import static cofh.lib.util.Constants.TOOL_PICKAXE;

public class BlockOre extends Block {

	protected int minXP = 0;
	protected int maxXP = 0;
	protected ArrayList<RandomDrop> dropList = new ArrayList<>();

	public BlockOre() {

		this(Material.ROCK.getMaterialMapColor());
	}

	public BlockOre(MapColor color) {

		this(Material.ROCK, color);
	}

	public BlockOre(Material blockMaterial, MapColor color) {

		super(blockMaterial, color);

		setHardness(3.0F);
		setResistance(5.0F);
		setSoundType(SoundType.STONE);

		setHarvestParams(TOOL_PICKAXE, 2);
	}

	public BlockOre setHarvestParams(String toolClass, int level) {

		setHarvestLevel(toolClass, level);
		return this;
	}

	public BlockOre setXPDrop(int minXP, int maxXP) {

		this.minXP = minXP;
		this.maxXP = maxXP;
		return this;
	}

	public BlockOre addDrop(RandomDrop drop) {

		this.dropList.add(drop);
		return this;
	}

	public BlockOre addDrops(ArrayList<RandomDrop> drops) {

		this.dropList.addAll(drops);
		return this;
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {

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
