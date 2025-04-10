package filehandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler implements FileOperations {
    private String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<String> load() {
        List<String> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                events.add(line);
            }
        } catch (IOException e) {
            System.out.println("No events found.");
        }
        return events;
    }

    @Override
    public void save(String event) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(event);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving event.");
        }
    }

    @Override
    public void save(List<String> events) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String event : events) {
                writer.write(event);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving events.");
        }
    }

    @Override
    public void deleteEvent(String event) {
        List<String> events = load();
        events.remove(event);
        save(events);
    }
}