package filehandler;

import java.util.List;

public interface FileOperations {
    List<String> load();
    void save(String event);
}