package codechicken.newlib.bakedmodel;

import net.minecraft.client.renderer.block.model.ItemOverrideList;

/**
 * Created by covers1624 on 19/01/19.
 */
public class OverideListModel extends DummyModel {

    private final ItemOverrideList overrideList;

    public OverideListModel(ItemOverrideList overrideList) {
        this.overrideList = overrideList;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrideList;
    }
}
