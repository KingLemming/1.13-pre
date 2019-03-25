package cofh.core.block.ores;

import cofh.core.block.BlockCoFH;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import static cofh.lib.util.Constants.TOOL_PICKAXE;

public class BlockOre extends BlockCoFH {

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

}
