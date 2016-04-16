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

    public static final int DEFAULT_CLOCK_CYCLES = 4;
    public static final int UNSIGNED_BYTE_MASK = 0xFF;

    private boolean halt;

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

        programCounter = 0x0100;
        stackPointer = (short)0xFFFF;
    }

    /***
     * This method causes the CPU to process the next instruction. It is the CPU's main logic control.
     *
     * @return This is the number of clock cycles(T states) that have past during execution.
     */
    public int process() {
        clock.reset();

        if (!halt) {
            checkInterrupts();

            short opCode = memoryManagementUnit.readByte(programCounter++);
            int cyclesPast = decode(opCode);

            clock.addMachineCycles(cyclesPast);
        } else {
            clock.addMachineCycles(DEFAULT_CLOCK_CYCLES);
        }

        return clock.getMachineCycles();
    }

    private int decode(short opCode) {
        int cyclesPast = 0;

        // TODO: Add op codes here.
        switch (opCode & UNSIGNED_BYTE_MASK) {
            case 0x00: cyclesPast += DEFAULT_CLOCK_CYCLES;
        }

        return cyclesPast;
    }

    private void checkInterrupts() {
        // TODO: Figure this out.
    }
}
