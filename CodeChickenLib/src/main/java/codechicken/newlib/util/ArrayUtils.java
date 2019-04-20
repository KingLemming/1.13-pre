package codechicken.newlib.util;

/**
 * Created by covers1624 on 3/27/2016.
 */
public class ArrayUtils {

    /**
     * Basically a wrapper for System.arraycopy with support for CCL Copyable's
     *
     * @param src     The source array.
     * @param srcPos  Starting position in the source array.
     * @param dst     The destination array.
     * @param destPos Starting position in the destination array.
     * @param length  The number of elements to copy.
     */
    public static void arrayCopy(Object src, int srcPos, Object dst, int destPos, int length) {
        System.arraycopy(src, srcPos, dst, destPos, length);
        if (dst instanceof Copyable[]) {
            Object[] oa = (Object[]) dst;
            Copyable<Object>[] c = (Copyable[]) dst;
            for (int i = destPos; i < destPos + length; i++) {
                if (c[i] != null) {
                    oa[i] = c[i].copy();
                }
            }
        }
    }
}
