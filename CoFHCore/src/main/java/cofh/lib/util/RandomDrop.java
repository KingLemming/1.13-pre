package cofh.lib.util;

import cofh.lib.util.helpers.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;

public class RandomDrop {

	final ItemStack stack;
	final int min;
	final int max;
	final int range;
	final boolean allowFortune;

	public RandomDrop(Block block, int min, int max) {

		this(new ItemStack(block), min, max, true);
	}

	public RandomDrop(Item item, int min, int max) {

		this(new ItemStack(item), min, max, true);
	}

	public RandomDrop(ItemStack stack, int min, int max) {

		this(stack, min, max, true);
	}

	public RandomDrop(Block block, int min, int max, boolean allowFortune) {

		this(new ItemStack(block), min, max, allowFortune);
	}

	public RandomDrop(Item item, int min, int max, boolean allowFortune) {

		this(new ItemStack(item), min, max, allowFortune);
	}

	public RandomDrop(ItemStack stack, int min, int max, boolean allowFortune) {

		this.stack = cloneStack(stack);
		this.min = MathHelper.clamp(min, 0, max);
		this.max = Math.max(this.min, max);
		this.range = 1 + this.max - this.min;
		this.allowFortune = allowFortune;
	}

	public ItemStack getDrop(Random rand, int fortune) {

		int amount = min + (allowFortune ? rand.nextInt(fortune + range) : rand.nextInt(range));
		return cloneStack(stack, amount);
	}
}
