package commands;

import java.util.*;

public class FindCommand implements Command {
    private final Map<String, List<String>> events;

    public FindCommand(Map<String, List<String>> events) {
        this.events = events;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: find <searchString>");
            return;
        }
        String searchString = args[1];
        boolean found = false;
        for (List<String> eventList : events.values()) {
            for (String event : eventList) {
                if (event.contains(searchString)) {
                    System.out.println(event);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No events found containing '" + searchString + "'");
        }
    }
}
