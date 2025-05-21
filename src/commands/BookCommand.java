package commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import filehandler.FileHandler;

public class BookCommand implements Command {

       private final FileHandler fileHandler; 
       private final Map<String, List<String>> events;


    public BookCommand(FileHandler fileHandler, Map<String, List<String>> events) {
        this.fileHandler = fileHandler;
        this.events = events;
        loadEventsFromFile();
    }

    private void loadEventsFromFile() {
        List<String> allEvents = fileHandler.load();
        for (String event : allEvents) {
            String[] parts = event.split(" ", 2);
            if (parts.length < 2) continue;
            String date = parts[0];
            String eventDetails = parts[1];
            List<String> dateEvents = events.get(date);
            if (dateEvents == null) {
                dateEvents = new ArrayList<>();
                events.put(date, dateEvents);
            }
            dateEvents.add(eventDetails);
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: book <date> <starttime> <endtime> <name> [note]");
            return;
        }

        String[] params = args[1].split(" ", 5);
        if (params.length < 4) {
            System.out.println("Usage: book <date> <starttime> <endtime> <name> [note]");
            return;
        }

        String date = params[0];
        String startTime = params[1];
        String endTime = params[2];
        String name = params[3];
        String note = params.length == 5 ? params[4] : "";

        // Validate time range
        if (!isValidTimeRange(startTime, endTime)) {
            System.out.println("Invalid time range. Ensure start time is before end time.");
            return;
        }

        // Check if the slot is available
        List<String> dayEvents = events.get(date);
        if (dayEvents == null) {
            dayEvents = new ArrayList<>();
            events.put(date, dayEvents);
        }
        if (isSlotOccupied(dayEvents, startTime, endTime)) {
            System.out.println("Time slot is already booked.");
            return;
        }

        // Book the slot
        String event = date + " " + startTime + " " + endTime + " " + name + (note.isEmpty() ? "" : " " + note);
        dayEvents.add(event);
        System.out.println("Booking successful for " + name + " on " + date + " from " + startTime + " to " + endTime);
    }

    private boolean isValidTimeRange(String startTime, String endTime) {
        return startTime.compareTo(endTime) < 0;
    }

    private boolean isSlotOccupied(List<String> dayEvents, String startTime, String endTime) {
        for (String event : dayEvents) {
            String[] parts = event.split(" ", 4);
            String bookedStartTime = parts[1];
            String bookedEndTime = parts[2];
            if (startTime.compareTo(bookedEndTime) < 0 && endTime.compareTo(bookedStartTime) > 0) {
                System.out.println("Conflict: There is already a booking from " + bookedStartTime + " to " + bookedEndTime);
                return true;
            }
        }
        return false;
    }
}
