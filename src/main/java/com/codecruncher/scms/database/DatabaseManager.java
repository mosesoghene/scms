package com.codecruncher.scms.database;

import java.util.List;
import java.util.Map;

public interface DatabaseManager {
    void initialize();
    String getFilePath(String tableName);
    int getNextId(String tableName);

    void save(String tableName, String[] record);

    void update(String tableName, String[] record, String key, String value);

    void delete(String tableName, String key, String value);
    List<String[]> fetchAll(String tableName);
    List<String[]> filterRecords(String tableName, String key, String value);

    boolean recordExists(String value);

    boolean recordExists(String tableName, String key, String value);

}
