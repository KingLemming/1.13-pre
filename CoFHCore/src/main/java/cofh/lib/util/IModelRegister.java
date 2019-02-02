package cofh.lib.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelRegister {

	@SideOnly (Side.CLIENT)
	void registerModel();

	@SideOnly (Side.CLIENT)
	void generateModelFiles();

}
