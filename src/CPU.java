/**
 * Created by devin on 4/15/16.
 */
public class CPU {
    // Clock speed in hertz
    private static final int CLOCK_SPEED = 4190000;

    private static final byte REGISTER_A = 0;
    private static final byte REGISTER_B = 1;
    private static final byte REGISTER_C = 2;
    private static final byte REGISTER_D = 3;
    private static final byte REGISTER_E = 4;
    private static final byte REGISTER_H = 5;
    private static final byte REGISTER_L = 6;
    private static final byte REGISTER_F = 7;

    // 8 bit registers
    private byte [] registers = new byte[8];

    // 16 bit registers
    private short programCounter;
    private short stackPointer;

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

    public void run() {
        // TODO: Figure this out and write it.

    }
}
