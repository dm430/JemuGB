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
    private byte[] memory = new byte[MEMORY_RANGE];

    public byte readByte(short address) {
        return memory[address];
    }

    public void writeByte(short address, byte value) {
        memory[address] = value;
    }

    public short readWord(short address) {
        /* This magically widens and reads the next byte.
         * It also reverses the endianness. I didn't know
         * Java did that but im not complaining.
         */
        return memory[address];
    }

    public void writeWord(short address, short value) {
        byte highOrderBits = (byte) ((value >> 8) & 0xFF);
        byte lowOrderBits = (byte) (value & 0xFF);

        memory[address] = lowOrderBits;
        memory[address] = highOrderBits;
    }

    public void loadImage(byte[] binaryImage, int memoryLocation) {
        for (int index = 0; index < binaryImage.length; index++) {
            int memoryAddress = index + memoryLocation;

            if (memoryAddress > MEMORY_RANGE) {
                throw new IndexOutOfBoundsException("The address that was attempted to be written to is out of the memory range.");
            }

            memory[memoryAddress] = binaryImage[index];
        }
    }
}
