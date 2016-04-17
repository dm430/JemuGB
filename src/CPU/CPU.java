package CPU;

import Memory.MemoryManagementUnit;

import java.util.HashMap;

/**
 * Created by devin on 4/15/16.
 *
 * CPU.CPU reference info
 * ------------------
 * http://marc.rawer.de/Gameboy/Docs/GBCPUman.pdf
 */
public class CPU {
    public static final int DEFAULT_CLOCK_CYCLES = 4;
    public static final int UNSIGNED_BYTE_MASK = 0xFF;
    public static final int UNSIGNED_SHORT_MASK = 0xFFFF;

    // Flags
    byte ZERO_FLAG = (byte)0x80;
    byte NEGITIVE_FLAG = 0x40;
    byte HALF_CARRY_FLAG = 0x20;
    byte CARRY_FLAG = 0x10;

    private boolean halt;

    // This is the clock
    private Clock clock = new Clock();

    // 8 bit registers
    private byte registerA;
    private byte registerB;
    private byte registerC;
    private byte registerD;
    private byte registerE;
    private byte registerF;
    private byte registerH;
    private byte registerL;

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
        halt = false;

        registerA = 0x00;
        registerB = 0x00;
        registerC = 0x00;
        registerD = 0x00;
        registerE = 0x00;
        registerF = 0x00;
        registerH = 0x00;
        registerL = 0x00;

        programCounter = 0x0000;
        stackPointer = 0x0000;
    }

    /***
     * This method causes the CPU to process the next instruction. It is the CPU's main logic control.
     *
     * @return This is the number of clock cycles(T states) that have past during execution.
     */
    public int process() throws UnknownOpCodeException {
        clock.reset();

        if (!halt) {
            checkInterrupts();

            int opCode = memoryManagementUnit.readByte(programCounter) & UNSIGNED_BYTE_MASK;
            Instruction currentInstruction = instructions.get(opCode);

            if (currentInstruction == null) {
                throw new UnknownOpCodeException(opCode);
            }

            int cyclesPast = currentInstruction.execute();

            clock.addMachineCycles(cyclesPast);
            programCounter += currentInstruction.getAddToProgramCounter();
        } else {
            clock.addMachineCycles(DEFAULT_CLOCK_CYCLES);
        }

        return clock.getMachineCycles();
    }

    private void checkInterrupts() {
        // TODO: Figure this out.
    }

    private void setFlag(byte flag) {
        registerF |= flag;
    }

    private final HashMap<Integer, Instruction> instructions = new HashMap<Integer, Instruction>() {{
        // Load a 16 bit immediate value into the stackPointer.
        put(0x31, new Instruction("LD_SP_n", 12, 3, () -> stackPointer = memoryManagementUnit.readWord((short)(programCounter + 1))));

        // Xor A
        put(0xAF, new Instruction("Xor_A", 4, 1, () -> {
            registerA ^= registerA;

            // Set Z flag
            if (registerA == 0) {
                setFlag(ZERO_FLAG);
            }
        }));
    }};
}
