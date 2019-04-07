package cofh.thermal.expansion.util.managers;

import cofh.lib.util.comparison.BlockWrapper;
import cofh.lib.util.comparison.ComparableItemStack;
import cofh.lib.util.comparison.ItemWrapper;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermal.core.init.FluidsTSeries;
import cofh.thermal.core.init.ItemsTSeries;
import cofh.thermal.core.util.managers.IManager;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.block.BlockHugeMushroom.EnumType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// TODO: Finish / Overhaul
public class TapperManager implements IManager {

	private static final TapperManager INSTANCE = new TapperManager();
	public static FluidStack defaultFluidStack = new FluidStack(FluidsTSeries.fluidResin, 0);

	protected Map<BlockWrapper, FluidStack> blockMap = new Object2ObjectOpenHashMap<>();
	protected Map<ItemWrapper, FluidStack> itemMap = new Object2ObjectOpenHashMap<>();
	protected SetMultimap<BlockWrapper, BlockWrapper> leafMap = HashMultimap.create();
	protected TObjectIntHashMap<ComparableItemStack> fertilizerMap = new TObjectIntHashMap<>();

	public final int ITEM_FLUID_FACTOR = 5;

	public static TapperManager instance() {

		return INSTANCE;
	}

	// region IManager
	public void config() {

	}

	public void initialize() {

		FluidStack sap = new FluidStack(FluidsTSeries.fluidSap, 50);
		FluidStack resin = new FluidStack(FluidsTSeries.fluidResin, 50);
		FluidStack mushroom_stew = new FluidStack(FluidsTSeries.fluidMushroomStew, 50);

		/* FERTILIZER */
		{
			addFertilizer(ItemsTSeries.itemPhytoGro, 2);
			addFertilizer(ItemsTSeries.itemPhytoGroRich, 3);
			addFertilizer(ItemsTSeries.itemPhytoGroFlux, 4);
		}

		/* FLUIDS */
		{
			addStandardMapping(new ItemStack(Blocks.LOG, 1, 0), new FluidStack(sap, 50));
			addStandardMapping(new ItemStack(Blocks.LOG, 1, 1), new FluidStack(resin, 100));
			addStandardMapping(new ItemStack(Blocks.LOG, 1, 2), new FluidStack(resin, 50));
			addStandardMapping(new ItemStack(Blocks.LOG, 1, 3), new FluidStack(resin, 50));     // rubber
			addStandardMapping(new ItemStack(Blocks.LOG2, 1, 0), new FluidStack(resin, 50));    // rubber
			addStandardMapping(new ItemStack(Blocks.LOG2, 1, 1), new FluidStack(sap, 100));

			addBlockStateMapping(new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK, 1, 10), new FluidStack(mushroom_stew, 50));
			addBlockStateMapping(new ItemStack(Blocks.RED_MUSHROOM_BLOCK, 1, 10), new FluidStack(mushroom_stew, 50));
		}

		/* LEAVES */
		{
			addVanillaLeafMappings(Blocks.LOG, BlockOldLog.VARIANT, Blocks.LEAVES, BlockOldLeaf.VARIANT);
			addVanillaLeafMappings(Blocks.LOG2, BlockNewLog.VARIANT, Blocks.LEAVES2, BlockNewLeaf.VARIANT);

			addLeafMapping(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.CENTER));

			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_WEST));
			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH));
			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.NORTH_EAST));
			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.WEST));
			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.CENTER));
			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.EAST));
			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_WEST));
			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH));
			addLeafMapping(Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.STEM), Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, EnumType.SOUTH_EAST));
		}
	}

	public void refresh() {

		Map<BlockWrapper, FluidStack> tempBlockMap = new Object2ObjectOpenHashMap<>(blockMap.size());
		Map<ItemWrapper, FluidStack> tempItemMap = new Object2ObjectOpenHashMap<>(itemMap.size());
		HashMultimap<BlockWrapper, BlockWrapper> tempLeafMap = HashMultimap.create(leafMap.keySet().size(), leafMap.size() / leafMap.keySet().size());
		TObjectIntHashMap<ComparableItemStack> tempFertilizerMap = new TObjectIntHashMap<>(fertilizerMap.size());

		for (Entry<BlockWrapper, FluidStack> entry : blockMap.entrySet()) {
			BlockWrapper tempBlock = new BlockWrapper(entry.getKey().block, entry.getKey().metadata);
			tempBlockMap.put(tempBlock, entry.getValue());
		}
		for (Entry<ItemWrapper, FluidStack> entry : itemMap.entrySet()) {
			ItemWrapper tempItem = new ItemWrapper(entry.getKey().item, entry.getKey().metadata);
			tempItemMap.put(tempItem, entry.getValue());
		}
		for (Entry<BlockWrapper, BlockWrapper> entry : leafMap.entries()) {
			BlockWrapper tempLeaf = new BlockWrapper(entry.getKey().block, entry.getKey().metadata);
			tempLeafMap.put(tempLeaf, entry.getValue());
		}
		for (TObjectIntIterator<ComparableItemStack> it = fertilizerMap.iterator(); it.hasNext(); ) {
			it.advance();
			tempFertilizerMap.put(new ComparableItemStack(it.key().toItemStack()), it.value());
		}
		blockMap.clear();
		itemMap.clear();
		leafMap.clear();
		fertilizerMap.clear();

		blockMap = tempBlockMap;
		itemMap = tempItemMap;
		leafMap = tempLeafMap;
		fertilizerMap = tempFertilizerMap;
	}
	// endregion

	public FluidStack getFluid(IBlockState state) {

		FluidStack ret = blockMap.get(new BlockWrapper(state.getBlock(), state.getBlock().getMetaFromState(state)));
		return ret != null ? ret : defaultFluidStack;
	}

	public boolean mappingExists(IBlockState state) {

		return getFluid(state) != defaultFluidStack;
	}

	public FluidStack getFluid(ItemStack stack) {

		FluidStack ret = itemMap.get(new ItemWrapper(stack.getItem(), ItemHelper.getItemDamage(stack)));
		return ret != null ? ret : defaultFluidStack;
	}

	public boolean mappingExists(ItemStack stack) {

		return getFluid(stack) != defaultFluidStack;
	}

	public Set<BlockWrapper> getLeaf(IBlockState state) {

		return leafMap.get(new BlockWrapper(state.getBlock(), state.getBlock().getMetaFromState(state)));
	}

	public int getFertilizerMultiplier(ItemStack stack) {

		return stack.isEmpty() ? 0 : fertilizerMap.get(new ComparableItemStack(stack));
	}

	/* ADD MAPPING */
	public boolean addStandardMapping(ItemStack item, FluidStack fluid) {

		if (item.isEmpty() || fluid == null) {
			return false;
		}
		addBlockStateMapping(item, fluid);
		addItemMapping(item, new FluidStack(fluid, fluid.amount / ITEM_FLUID_FACTOR));
		return true;
	}

	public boolean addBlockStateMapping(ItemStack item, FluidStack fluid) {

		if (item.isEmpty() || fluid == null) {
			return false;
		}
		blockMap.put(new BlockWrapper(((ItemBlock) item.getItem()).getBlock(), ItemHelper.getItemDamage(item)), fluid.copy());
		return true;
	}

	public boolean addBlockStateMapping(IBlockState state, FluidStack fluid) {

		if (fluid == null) {
			return false;
		}
		blockMap.put(new BlockWrapper(state.getBlock(), state.getBlock().getMetaFromState(state)), fluid.copy());
		return true;
	}

	public boolean addItemMapping(ItemStack item, FluidStack fluid) {

		if (item.isEmpty() || fluid == null) {
			return false;
		}
		itemMap.put(new ItemWrapper(item.getItem(), ItemHelper.getItemDamage(item)), fluid.copy());
		return true;
	}

	public boolean addLeafMapping(IBlockState logState, IBlockState leafState) {

		if (logState == null || leafState == null) {
			return false;
		}
		leafMap.put(new BlockWrapper(logState), new BlockWrapper(leafState));
		return true;
	}

	/* HELPERS */
	protected void addVanillaLeafMappings(Block logBlock, PropertyEnum<BlockPlanks.EnumType> logVariantProperty, Block leavesBlock, PropertyEnum<BlockPlanks.EnumType> leafVariantProperty) {

		for (BlockPlanks.EnumType variant : logVariantProperty.getAllowedValues()) {
			IBlockState logState = logBlock.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(logVariantProperty, variant);

			for (Boolean check_decay : BlockLeaves.CHECK_DECAY.getAllowedValues()) {
				IBlockState leafState = leavesBlock.getDefaultState().withProperty(leafVariantProperty, variant).withProperty(BlockLeaves.DECAYABLE, Boolean.TRUE).withProperty(BlockLeaves.CHECK_DECAY, check_decay);
				leafMap.put(new BlockWrapper(logState), new BlockWrapper(leafState));
			}
		}
	}

	protected void addFertilizer(ItemStack fertilizer, int multiplier) {

		fertilizerMap.put(new ComparableItemStack(fertilizer), multiplier);
	}

}
