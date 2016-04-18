package CPU;

/**
 * Created by devin on 4/16/16.
 */
interface Command {
    void execute();
}

public class Instruction {
    private final int cyclesPast;
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
        this.addToProgramCounter = addToProgramCounter;
        this.description = description;
        this.cyclesPast = cyclesPast;
        command = null;
    }

    public int execute() {
        if (command != null) {
            command.execute();
        }

        return cyclesPast;
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


