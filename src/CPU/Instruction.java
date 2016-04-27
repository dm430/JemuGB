package CPU;

/**
 * Created by devin on 4/16/16.
 */
interface Command {
    void execute();
}

class NullCommand implements Command {

    @Override
    public void execute() {}
}

public class Instruction {
    protected final int cyclesPast;
    private final int addToProgramCounter;
    private final String description;
    private final Command command;

    public Instruction(String description, int cyclesPast, int addToProgramCounter, Command command) {
        this.addToProgramCounter = addToProgramCounter;
        this.description = description;
        this.cyclesPast = cyclesPast;
        this.command = command;
    }

    public Instruction(String description, int cyclesPast, int addToProgramCounter) {
        this(description, cyclesPast, addToProgramCounter, new NullCommand());
    }

    public void execute() {
        command.execute();
    }

    public int getCyclesPast() {
        return cyclesPast;
    }

    public int getAddToProgramCounter() {
        return addToProgramCounter;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "cyclesPast=" + cyclesPast +
                ", addToProgramCounter=" + addToProgramCounter +
                ", description='" + description + '\'' +
                '}';
    }
}


