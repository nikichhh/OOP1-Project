package commands;

import filehandler.FileHandler;

import java.util.Scanner;

public class BaseCommands {
    private FileHandler fileHandler;
    private String currentFilePath = "data/calendar.txt";

    public BaseCommands() {
        this.fileHandler = new FileHandler(currentFilePath);
    }

    public void open(String filePath) {
        this.currentFilePath = filePath;
        this.fileHandler = new FileHandler(currentFilePath);
        System.out.println("Opened file: " + filePath);
    }

    public void close() {
        this.fileHandler = null;
        System.out.println("Closed the current file.");
    }

    public void save() {
        if (fileHandler != null) {
            fileHandler.save(fileHandler.load());
            System.out.println("Saved changes to: " + currentFilePath);
        } else {
            System.out.println("No file is currently open.");
        }
    }

    public void saveAs(String filePath) {
        if (fileHandler != null) {
            FileHandler newFileHandler = new FileHandler(filePath);
            newFileHandler.save(fileHandler.load());
            this.currentFilePath = filePath;
            this.fileHandler = newFileHandler;
            System.out.println("Saved changes to: " + filePath);
        } else {
            System.out.println("No file is currently open.");
        }
    }

    public void addEvent(String event) {
        if (fileHandler != null) {
            fileHandler.save(event);
            System.out.println("Event added: " + event);
        } else {
            System.out.println("No file is currently open.");
        }
    }

    public void showEvents() {
        if (fileHandler != null) {
            System.out.println("Events:");
            for (String event : fileHandler.load()) {
                System.out.println(event);
            }
        } else {
            System.out.println("No file is currently open.");
        }
    }

    public void help() {
        System.out.println("Available commands:");
        System.out.println("open <file> - Opens a calendar file");
        System.out.println("close - Closes the current calendar file");
        System.out.println("save - Saves changes to the current calendar file");
        System.out.println("save as <file> - Saves changes to a new calendar file");
        System.out.println("add [event] - Adds a new event");
        System.out.println("show - Shows all events");
        System.out.println("exit - Exits the application");
    }
}