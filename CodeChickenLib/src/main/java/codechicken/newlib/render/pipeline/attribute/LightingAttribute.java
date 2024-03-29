package codechicken.newlib.render.pipeline.attribute;

import codechicken.newlib.colour.ColourRGBA;
import codechicken.newlib.render.CCRenderState;
import codechicken.newlib.render.pipeline.VertexAttribute;

/**
 * Created by covers1624 on 10/10/2016.
 */
public class LightingAttribute extends VertexAttribute<int[]> {

    public static final AttributeKey<int[]> attributeKey = new AttributeKey<int[]>() {
        @Override
        public int[] newArray(int length) {
            return new int[length];
        }
    };

    private int[] colourRef;

    @Override
    public int[] newArray(int length) {
        return new int[length];
    }

    @Override
    public String getAttribName() {
        return "lightingAttrib";
    }

    @Override
    public boolean load(CCRenderState state) {
        if (!state.computeLighting || !state.fmt.hasColor() || !state.model.hasAttribute(LightingAttribute.attributeKey)) {
            return false;
        }

        colourRef = state.model.getAttributes(LightingAttribute.attributeKey);
        if (colourRef != null) {
            state.pipeline.addDependency(state.colourAttrib);
            return true;
        }
        return false;
    }

    @Override
    public void operate(CCRenderState state) {
        state.colour = ColourRGBA.multiply(state.colour, colourRef[state.vertexIndex]);
    }
}
