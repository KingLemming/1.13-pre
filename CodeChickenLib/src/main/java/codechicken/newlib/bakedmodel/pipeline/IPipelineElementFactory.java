package codechicken.newlib.bakedmodel.pipeline;

/**
 * @author covers1624
 */
@FunctionalInterface
public interface IPipelineElementFactory<T extends IPipelineConsumer> {

    T create();
}
