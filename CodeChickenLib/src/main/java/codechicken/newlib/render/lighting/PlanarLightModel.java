package codechicken.newlib.render.lighting;

import codechicken.newlib.colour.ColourRGBA;
import codechicken.newlib.render.CCRenderState;
import codechicken.newlib.render.pipeline.IVertexOperation;

/**
 * Faster precomputed version of LightModel that only works for axis planar sides
 */
public class PlanarLightModel implements IVertexOperation {

    public static PlanarLightModel standardLightModel = LightModel.standardLightModel.reducePlanar();

    public int[] colours;

    public PlanarLightModel(int[] colours) {
        this.colours = colours;
    }

    @Override
    public boolean load(CCRenderState state) {
        if (!state.computeLighting) {
            return false;
        }

        state.pipeline.addDependency(state.sideAttrib);
        state.pipeline.addDependency(state.colourAttrib);
        return true;
    }

    @Override
    public void operate(CCRenderState state) {
        state.colour = ColourRGBA.multiply(state.colour, colours[state.side]);
    }

    @Override
    public int operationID() {
        return LightModel.operationIndex;
    }
}
