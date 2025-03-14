package com.codecruncher.scms.auth;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Authentication {
    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean isValidPassword(String password, Map<String, String> row) {
        return BCrypt.checkpw(password, row.get("password"));
    }

    public boolean authenticate(String userType, String email, String password) {
        String filename = userType + "s.csv";

        try (CSVReader csvReader = new CSVReader(new FileReader(filename))) {
            // Read all lines at once
            List<String[]> records = csvReader.readAll();
            if (records.isEmpty()) {
                return false;
            }

            String[] headers = records.get(0);

            // Process each line
            for (int i = 1; i < records.size(); i++) {
                String[] values = records.get(i);
                Map<String, String> record = new HashMap<>();

                // Create a map of column name to value
                for (int j = 0; j < headers.length && j < values.length; j++) {
                    record.put(headers[j], values[j]);
                }

                // Check if email matches and password is valid
                if (email.equals(record.get("email")) && Authentication.isValidPassword(password, record)) {
                    return true;
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return false;
    }
}