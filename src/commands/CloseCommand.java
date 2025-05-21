package commands;

import filehandler.FileHandler;
import java.util.List;
import java.util.Map;

public class CloseCommand implements Command {
    private FileHandler fileHandler;
    private Map<String, List<String>> events;

    public CloseCommand(FileHandler fileHandler, Map<String, List<String>> events) {
        this.fileHandler = fileHandler;
        this.events = events;
    }

    @Override
    public void execute(String[] args) {
        if (events != null) {
            events.clear();
        }
        fileHandler = null;
        System.out.println("Closed the current file.");
    }
}

