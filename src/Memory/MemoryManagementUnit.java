package Memory;

/**
 * Created by devin on 4/15/16.
 *
 *          General memory map
 *      ===========================
 *      Interrupt Enable Register
 *      --------------------------- FFFF
 *      Internal RAM
 *      --------------------------- FF80
 *      Empty but unusable for I/O
 *      --------------------------- FF4C
 *      I/O ports
 *      --------------------------- FF00
 *      Empty but unusable for I/O
 *      --------------------------- FEA0
 *      Sprite Attrib Memory (OAM)
 *      --------------------------- FE00
 *      Echo of 8kB Internal RAM
 *      --------------------------- E000
 *      8kB Internal RAM
 *      --------------------------- C000
 *      8kB switchable RAM bank
 *      --------------------------- A000
 *      8kB Video RAM
 *      --------------------------- 8000 --
 *      16kB switchable ROM bank           |
 *      --------------------------- 4000   |= 32kB Cartrigbe
 *      16kB ROM bank #0                   |
 *      --------------------------- 0000 --
 */
public class MemoryManagementUnit {
    // Super simple memory representation just to get this working.
    private static final int MEMORY_RANGE = 65535;
    public static final int UNSIGNED_BYTE_MASK = 0xFF;
    public static final int UNSIGNED_SHORT_MASK = 0xFFFF;

    private final byte[] memory = new byte[MEMORY_RANGE];

    public byte readByte(short address) {
        return (byte) (memory[address & UNSIGNED_SHORT_MASK] & UNSIGNED_BYTE_MASK);
    }

    public void writeByte(short address, byte value) {
        memory[address & UNSIGNED_SHORT_MASK] = value;
    }

    public short readWord(short address) {
        int memoryAddress = address & UNSIGNED_SHORT_MASK;

        byte lowOrderBits = memory[memoryAddress];
        byte highOrderBits = memory[memoryAddress + 1];

        return (short) ((highOrderBits & UNSIGNED_BYTE_MASK) << 8 | lowOrderBits & UNSIGNED_BYTE_MASK);
    }

    public void writeWord(short address, short value) {
        int memoryAddress = address & UNSIGNED_SHORT_MASK;

        byte highOrderBits = (byte) (value >> 8 & UNSIGNED_BYTE_MASK);
        byte lowOrderBits = (byte) (value & UNSIGNED_BYTE_MASK);

        memory[memoryAddress] = lowOrderBits;
        memory[memoryAddress + 1] = highOrderBits;
    }

    public void loadImage(byte[] binaryImage, int memoryLocation) {
        for (int index = 0; index < binaryImage.length; index++) {
            int memoryAddress = index + memoryLocation;

            if (memoryAddress > MEMORY_RANGE) {
                throw new IndexOutOfBoundsException("The address that was attempted to be written to is out of the memory range.");
            }

            memory[memoryAddress & UNSIGNED_SHORT_MASK] = binaryImage[index];
        }
    }
}
