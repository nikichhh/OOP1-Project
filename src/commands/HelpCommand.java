package commands;

public class HelpCommand implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println("Available commands:");
        System.out.println("open <file> - Opens a calendar file");
        System.out.println("close - Closes the current calendar file");
        System.out.println("save - Saves changes to the current calendar file");
        System.out.println("saveas <file> - Saves changes to a new calendar file");
        System.out.println("book <date> <starttime> <endtime> <name> <note> - Books an event");
        System.out.println("unbook <date> <starttime> <endtime> - Removes an event");
        System.out.println("agenda <date> - Shows agenda for a date");
        System.out.println("change <date> <starttime> <option> <newvalue> - Changes an event");
        System.out.println("find <searchString> - Finds events containing a string");
        System.out.println("holiday <date> - Marks a holiday");
        System.out.println("busydays <from> <to> - Shows busy days in a range");
        System.out.println("findslot <fromdate> <hours> - Finds a free slot");
        System.out.println("findslotwith <fromdate> <hours> <calendar> - Finds a free slot with another calendar");
        System.out.println("merge <calendar> - Merges another calendar");
        System.out.println("help - Shows this help message");
        System.out.println("exit - Exits the application");
    }
}
