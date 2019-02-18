package codechicken.lib.bakedmodel.pipeline.transformers;

import codechicken.lib.bakedmodel.pipeline.IPipelineElementFactory;
import codechicken.lib.bakedmodel.Quad;
import codechicken.lib.bakedmodel.pipeline.QuadTransformer;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;

/**
 * This transformer simply overrides the alpha of the quad.
 * Only operates if the format has color.
 *
 * @author covers1624
 */
public class QuadAlphaOverride extends QuadTransformer {

    public static final IPipelineElementFactory<QuadAlphaOverride> FACTORY = QuadAlphaOverride::new;

    private float alphaOverride;

    QuadAlphaOverride() {
        super();
    }

    public QuadAlphaOverride(IVertexConsumer consumer, float alphaOverride) {
        super(consumer);
        this.alphaOverride = alphaOverride;
    }

    public QuadAlphaOverride setAlphaOverride(float alphaOverride) {
        this.alphaOverride = alphaOverride;
        return this;
    }

    @Override
    public boolean transform() {
        if (format.hasColor) {
            for (Quad.Vertex v : quad.vertices) {
                v.color[3] = alphaOverride;
            }
        }
        return true;
    }
}
