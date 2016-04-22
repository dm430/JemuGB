package CPU;

import JemuGB.Utilities;
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
    private static final int DEFAULT_CLOCK_CYCLES = 4;
    private static final int UNSIGNED_BYTE_MASK = 0xFF;
    private static final int UNSIGNED_SHORT_MASK = 0xFFFF;
    private static final int SWITCH_INSTRUCTION_SET = 0xCB;

    // Flags
    private final byte ZERO_FLAG = (byte)0x80;
    private final byte NEGATIVE_FLAG = 0x40;
    private final byte HALF_CARRY_FLAG = 0x20;
    private final byte CARRY_FLAG = 0x10;

    private boolean halt;

    // This is the clock
    private final Clock clock = new Clock();

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

            Instruction currentInstruction;
            int opCode = memoryManagementUnit.readByte(programCounter) & UNSIGNED_BYTE_MASK;

            if (opCode == SWITCH_INSTRUCTION_SET) {
                opCode = memoryManagementUnit.readByte(++programCounter) & UNSIGNED_BYTE_MASK;
                currentInstruction = alternateInstructions.get(opCode);
            } else {
                currentInstruction = instructions.get(opCode);
            }

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

    private void clearFlag(byte flag) {
        registerF &= ~flag;
    }

    private boolean isFlagSet(byte flag) {
        return (registerF & flag) != 0;
    }

    private final HashMap<Integer, Instruction> alternateInstructions = new HashMap<Integer, Instruction>() {{
        // Test the most significant bit in H.
        put(0x7C, new Instruction("BIT_7_H", 8, 1, () -> {
            boolean isHighBitSet = (registerH & ZERO_FLAG) != 0;

            if (isHighBitSet) {
                clearFlag(ZERO_FLAG);
            } else {
                setFlag(ZERO_FLAG);
            }
        }));
    }};

    private final HashMap<Integer, Instruction> instructions = new HashMap<Integer, Instruction>() {{
        // No operation.
        put(0x00, new Instruction("NOP", 4, 1));

        // Load a 16 bit immediate value into the stackPointer.
        put(0x31, new Instruction("LD_SP_n", 12, 3, () -> {
            stackPointer = memoryManagementUnit.readWord((short) (programCounter + 1));
        }));

        // Xor A
        put(0xAF, new Instruction("Xor_A", 4, 1, () -> {
            registerA ^= registerA;

            // Set Z flag
            if (registerA == 0) {
                setFlag(ZERO_FLAG);
            }
        }));

        // Load a 16 bit immediate value into HL.
        put(0x21, new Instruction("LD_HL_nn", 12, 3, () -> {
            short value = memoryManagementUnit.readWord((short) (programCounter + 1));

            registerH = Utilities.getHighBitsForWord(value);
            registerL = Utilities.getLowBitsForWord(value);
        }));

        // Put A into memory address HL. Decrement HL.
        put(0x32, new Instruction("LD_HL-_A", 8, 1, () -> {
            short address = Utilities.buildWord(registerH, registerL);

            memoryManagementUnit.writeByte(address--, registerA);
            registerH = Utilities.getHighBitsForWord(address);
            registerL = Utilities.getLowBitsForWord(address);
        }));

        /* If the condition is not zero, add a signed byte to the
         * current address.
         */
        put(0x20, new Instruction("JR_NZ_n", 8, 2, () -> {
            if (!isFlagSet(ZERO_FLAG)) {
                byte value = memoryManagementUnit.readByte((short) (programCounter + 1));
                // I don't need to add two here because it is added after this finishes executing.
                programCounter = (short) (programCounter + value);
            }
        }));

        // Load a 8 bit immediate value into C.
        put(0x0E, new Instruction("LD_C_n", 8, 2, () -> {
            registerC = memoryManagementUnit.readByte((short) (programCounter + 1));
        }));

        // Load a 8 bit immediate value into A.
        put(0x3E, new Instruction("LD_A_n", 8, 2, () -> {
            registerA = memoryManagementUnit.readByte((short) (programCounter + 1));
        }));

        /* Put A into the address (0xFF00 + C)
         * The documentation says this increments the PC by 2. However it seems
         * that others implementations agree that it increments the PC by one.
         */
        put(0xE2, new Instruction("LD_FF00+C_A", 8, 1, () -> {
            short address = (short) (0xFF00 + registerC);
            memoryManagementUnit.writeByte(address, registerA);
        }));
    }};
}
