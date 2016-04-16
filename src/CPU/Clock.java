package CPU;

/**
 * Created by devin on 4/15/16.
 */
public class Clock {
    private int machineCycles;

    public int getMachineCycles() {
        return machineCycles;
    }

    public void addMachineCycles(int machineCycles) {
        this.machineCycles += machineCycles;
    }

    public void reset() {
        machineCycles = 0;
    }
}
