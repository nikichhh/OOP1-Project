package commands;

import filehandler.FileHandler;

public class BaseCommands {
    private FileHandler fileHandler;

    public BaseCommands(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public void addEvent(String event) {
        fileHandler.save(event);
        System.out.println("Event added: " + event);
    }

    public void showEvents() {
        System.out.println("Events:");
        for (String event : fileHandler.load()) {
            System.out.println(event);
        }
    }
}