package codechicken.newlib.render.pipeline.attribute;

import codechicken.newlib.render.lighting.LC;
import codechicken.newlib.render.CCRenderState;
import codechicken.newlib.render.pipeline.VertexAttribute;
import codechicken.newlib.vec.Transformation;
import codechicken.newlib.vec.Vector3;

/**
 * Uses the position of the lightmatrix to compute LC if not provided
 */
public class LightCoordAttribute extends VertexAttribute<LC[]> {

    public static final AttributeKey<LC[]> attributeKey = new AttributeKey<LC[]>() {
        @Override
        public LC[] newArray(int length) {
            return new LC[length];
        }
    };

    private LC[] lcRef;
    private Vector3 vec = new Vector3();//for computation
    private Vector3 pos = new Vector3();

    @Override
    public LC[] newArray(int length) {
        return new LC[length];
    }

    @Override
    public String getAttribName() {
        return "lightCoordAttrib";
    }

    @Override
    public boolean load(CCRenderState state) {
        lcRef = state.model.getAttributes(LightCoordAttribute.attributeKey);
        if (state.model.hasAttribute(LightCoordAttribute.attributeKey)) {
            return lcRef != null;
        }

        pos.set(state.lightMatrix.pos);
        state.pipeline.addDependency(state.sideAttrib);
        state.pipeline.addRequirement(Transformation.operationIndex);
        return true;
    }

    @Override
    public void operate(CCRenderState state) {
        if (lcRef != null) {
            state.lc.set(lcRef[state.vertexIndex]);
        } else {
            state.lc.compute(vec.set(state.vert.vec).subtract(pos), state.side);
        }
    }
}
