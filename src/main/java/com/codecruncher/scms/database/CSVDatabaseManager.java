package com.codecruncher.scms.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVDatabaseManager implements DatabaseManager {

    public static void main(String[] args) {
        DatabaseManager databaseManager = new CSVDatabaseManager();
        databaseManager.initialize();
        databaseManager.save("students", new String[] {"1", "Moses", "moses@email.com", "password"});
    }


    @Override
    public void initialize() {
        Map<String, String[]> tables = new HashMap<>();
        tables.put("students", new String[] {"student_id", "name", "email", "password"});
        tables.put("courses", new String[]  {"instructor_id", "name", "email", "password"});
        tables.put("enrollments", new String[]  {"course_id", "title", "instructor_id"});
        tables.put("instructors", new String[]  {"student_id", "course_id", "grade"});

        for (Map.Entry<String, String[]> entry : tables.entrySet()) {
            String tableName = entry.getKey();
            String[] headers = entry.getValue();
            String path = getFilePath(tableName);

            File table = new File(path);
            if (table.exists() == false) createTable(table, headers);
        }
    }

    private void createTable(File table, String[] headers) {
        try (FileWriter csvWriter = new FileWriter(table)) {
            csvWriter.write(String.join(",", headers) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFilePath(String tableName){
        return  tableName + ".csv";
    }

    @Override
    public int getNextId(String tableName){
        List<String[]> records = fetchAll(tableName);
        if (records.isEmpty()) return 1;
        String[] lastRow = records.get(records.size() - 1);
        String lastIdStr = lastRow[0];
        int lastId = Integer.parseInt(lastIdStr);
        return lastId + 1;
    }

    @Override
    public void save(String tableName, String[] record){
        String filename = getFilePath(tableName);
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filename, true))) {
            csvWriter.writeNext(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String tableName, String[] newRecord, String key, String value){
        String filename = getFilePath(tableName);
        List<String[]> allRecords = fetchAll(tableName);
        String[] header = allRecords.get(0);
        List<Map<String, String>> records = new ArrayList<>();
        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, String> record = new HashMap<>();
            for (int j = 0; j < header.length; j++) {
                record.put(header[j], allRecords.get(i)[j]);
            }
            records.add(record);
        }

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filename))) {
            csvWriter.writeNext(header);
            for (Map<String, String> record : records) {
                if (record.get(key).equals(value)) {
                    csvWriter.writeNext(newRecord);
                } else {
                    csvWriter.writeNext(record.values().toArray(new String[0]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String tableName, String key, String value){

    }

    @Override
    public List<String[]> fetchAll(String tableName){
        File file = new File(getFilePath(tableName));
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            return csvReader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String[]> filterRecords(String tableName, String key, String value){
        return List.of(new String[] {"Hello", "world"}, new String[] {"Hy", "mal"});
    }

    @Override
    public boolean recordExists(String value) {
        return false;
    }

    @Override
    public boolean recordExists(String tableName, String key, String value){
        return false;
    }

}
