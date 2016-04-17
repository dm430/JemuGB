package CPU;

/**
 * Created by devin on 4/16/16.
 */
public class Instruction {
    private final int cyclesPast;
    private final int addToProgramCounter;
    private final Command command;
    private final String description;

    public Instruction(String description, int cyclesPast, int addToProgramCounter, Command command) {
        this.addToProgramCounter = addToProgramCounter;
        this.description = description;
        this.cyclesPast = cyclesPast;
        this.command = command;
    }

    public Instruction(int cyclesPast, int addToProgramCounter, Command command) {
        this("N/A", cyclesPast, addToProgramCounter, command);
    }

    public int execute() {
        command.execute();

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

interface Command {
    void execute();
}