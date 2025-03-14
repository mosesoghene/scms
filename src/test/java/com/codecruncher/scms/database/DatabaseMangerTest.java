package com.codecruncher.scms.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseMangerTest {
    private DatabaseManager databaseManager;

    @BeforeEach
    public void setup(){
        databaseManager = new CSVDatabaseManager();
    }

    private void initializeDatabases(){
        databaseManager.initialize();
    }


    private boolean databaseExists(String databaseName){
        File file = new File(databaseName);
        return file.exists();
    }

    private void deleteDatabases(){
        String[] tables = new String[] {"students.csv", "instructors.csv", "courses.csv", "enrollments.csv"};
        for(String table : tables){
            File file = new File(table);
            file.delete();
        }
    }

    @Test
    public void databaseCanBeInitialised_DatabaseManagerTest(){
        deleteDatabases();
        String[] tables = new String[] {"students.csv", "instructors.csv", "courses.csv", "enrollments.csv"};
        for(String table : tables) assertFalse(databaseExists(table));
        initializeDatabases();
        for(String table : tables) assertTrue(databaseExists(table));
    }

    @Test
    public void coursesCanBeSavedToCoursesTable_DatabaseManagerTest(){
        String[] tables = new String[] {"students.csv", "instructors.csv", "courses.csv", "enrollments.csv"};
        deleteDatabases();
        for(String table : tables) assertFalse(databaseExists(table));
        initializeDatabases();
        for(String table : tables) assertTrue(databaseExists(table));
        databaseManager.save("courses", new String[] {"1", "Java 101", "1"});
        List<String[]> records = databaseManager.filterRecords("courses", "instructor_id", "1");
        int actualSize = records.size();
        assertEquals(1, actualSize);

        databaseManager.save("courses", new String[] {"1", "Java 201", "1"});
        records = databaseManager.filterRecords("courses", "instructor_id", "1");
        actualSize = records.size();
        assertEquals(2, actualSize);

        records.forEach(record -> System.out.println(Arrays.toString(record)));

    }
}
