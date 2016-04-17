package CPU;

/**
 * Created by devin on 4/17/16.
 */
public class UnknownOpCodeException extends Throwable {
    private int opCode;

    public UnknownOpCodeException(int opCode) {
        this.opCode = opCode;
    }

    @Override
    public String getMessage() {
        return "Unknown opcode: " + opCode;
    }
}
