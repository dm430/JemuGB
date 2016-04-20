package JemuGB;

/**
 * Created by devin on 4/18/16.
 */
public class Utilities {
    private static final int BYTE_MASK = 0xFF;

    public static byte getHighBitsForWord(short word) {
        return (byte) ((word >> 8) & BYTE_MASK);
    }

    public static byte getLowBitsForWord(short word) {
        return (byte) (word & BYTE_MASK);
    }

    public static short buildWord(byte highOrderBits, byte lowOrderBits) {
        return (short) ((highOrderBits & BYTE_MASK) << 8 | lowOrderBits & BYTE_MASK);
    }
}
