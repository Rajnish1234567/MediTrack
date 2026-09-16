package com.airtribe.meditrack.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class CSVUtil {

    private CSVUtil() {
    }

    public static List<String[]> readCSV(String filePath) throws IOException {

        List<String[]> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (!line.isBlank()) {
                    records.add(line.split(","));
                }
            }
        }
        return records;
    }

    public static void writeCSV(String filePath, List<String> lines) throws IOException {

        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}