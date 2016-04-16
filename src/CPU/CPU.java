package CPU;

import Memory.MemoryManagementUnit;

/**
 * Created by devin on 4/15/16.
 *
 * CPU.CPU reference info
 * ------------------
 * http://marc.rawer.de/Gameboy/Docs/GBCPUman.pdf
 */
public class CPU {
    private static final byte REGISTER_A = 0;
    private static final byte REGISTER_B = 1;
    private static final byte REGISTER_C = 2;
    private static final byte REGISTER_D = 3;
    private static final byte REGISTER_E = 4;
    private static final byte REGISTER_H = 5;
    private static final byte REGISTER_L = 6;
    private static final byte REGISTER_F = 7;

    // This is the clock
    private Clock clock = new Clock();

    // 8 bit registers
    private byte [] registers = new byte[8];

    // 16 bit registers
    private short programCounter;
    private short stackPointer;

    // This is the Game boy's MMU
    private MemoryManagementUnit memoryManagementUnit;

    public CPU(MemoryManagementUnit memoryManagementUnit) {
        this.memoryManagementUnit = memoryManagementUnit;
    }

    /***
     * This method resets the processor back to its initial state.
     */
    public void reset() {
        for (byte register : registers) {
            register = 0x00;
        }

        programCounter = 0x0000;
        stackPointer = 0x0000;
    }

    public int tick() {
        // TODO: Figure this out and write it.
        return 0;
    }
}
