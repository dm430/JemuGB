package CPU;

/**
 * Created by devin on 4/15/16.
 */
public class Clock {
    private int clockCycles;

    public int getMachineCycles() {
        return clockCycles;
    }

    public void addMachineCycles(int clockCycles) {
        this.clockCycles += clockCycles;
    }

    public void reset() {
        clockCycles = 0;
    }
}
