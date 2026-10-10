package com.demo.framework.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.testng.annotations.DataProvider;

public final class CsvDataProvider {
    private static final String RESOURCE = "/testdata/sauce-users.csv";
    private static final List<CsvTestData> USERS = loadUsers();

    private CsvDataProvider() {
    }

    @DataProvider(name = "sauceCredentials")
    public static Object[][] sauceCredentials(Method testMethod) {
        String scenario = testMethod.getName();
        return USERS.stream()
                .filter(row -> row.scenario().equals(scenario))
                .map(row -> new Object[][]{{row.username(), row.password(), row.expectedError()}})
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No CSV row found for test scenario: " + scenario));
    }

    private static List<CsvTestData> loadUsers() {
        InputStream stream = CsvDataProvider.class.getResourceAsStream(RESOURCE);
        if (stream == null) {
            throw new IllegalStateException("Test data resource not found: " + RESOURCE);
        }
        List<CsvTestData> rows = new ArrayList<>();
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
             CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.builder()
                     .setHeader().setSkipHeaderRecord(true).setIgnoreEmptyLines(true).build())) {
            for (String header : List.of("scenario", "username", "password", "expectedError")) {
                if (!parser.getHeaderMap().containsKey(header)) {
                    throw new IllegalStateException("CSV " + RESOURCE + " is missing required column: " + header);
                }
            }
            for (CSVRecord record : parser) {
                String scenario = record.get("scenario").trim();
                String username = record.get("username").trim();
                String password = record.get("password").trim();
                String expectedError = record.get("expectedError").trim();
                if (scenario.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    throw new IllegalStateException("CSV " + RESOURCE + " contains a blank scenario or credential at record " + record.getRecordNumber());
                }
                rows.add(new CsvTestData(scenario, username, password, expectedError));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read CSV test data: " + RESOURCE, exception);
        }
        if (rows.isEmpty()) {
            throw new IllegalStateException("CSV test data contains no scenarios: " + RESOURCE);
        }
        return List.copyOf(rows);
    }
}
