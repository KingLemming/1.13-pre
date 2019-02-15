package cofh.lib.util;

public interface IResourceStorage {

	void modifyAmount(int amount);

	boolean isEmpty();

	int getSpace();

	int getStored();

	int getMaxStored();

	String getUnit();
}
