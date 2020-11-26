package uhk.fim.actions;

public interface FileManager {
    void saveJson(String to);
    void saveCsv(String to);
    void loadCsv(String from);
    void loadJson(String from);
    void saveToStorage();
    void loadFromStorage();
}
