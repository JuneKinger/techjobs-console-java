package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    // final means the field cannot be changed after initialization
    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */

    /*  findall() returns a string object because for eg if Location is chosen as user
        input: arrayList values will have:
        [Saint Louis, Portland, South Florida, Rhode Island, Kansas City, Seattle, ...]
    */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        /* eg if Location is chosen as user input: arrayList values will have:
        [Saint Louis, Portland, South Florida, Rhode Island, Kansas City, Seattle, ...]
         */
        return values;

    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();
        /*
        Notice that it's returning the allJobs property, which is a static property of the JobData class.
        In general, this is not a great thing to do, since the person calling our findAll method could then
        mess with the static database data that allJobs contains. Fix this by creating a copy of allJobs
         */
        // ArrayList<HashMap<String, String>> allJobs = new ArrayList<HashMap<String, String>>(); //
        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of the field to search for
     * @return List of all jobs matching the criteria
     */
    /* findByColumnAndValue() finds by column and value (value = search term input)
      and column is the column that should be searched */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded //
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column.toLowerCase());
            if (aValue.toLowerCase().contains(value)) {
                jobs.add(row);

            }
        }

        return jobs;
    }

    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded //
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            for (String aValue : row.values()) {

                if (aValue.toLowerCase().contains(value.toLowerCase())) {
                    jobs.add(row);
                    break;
                }
            }
        }
        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once. isDataLoaded variable is set to true after data is loaded //
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);

            // RFC4180 is a comma separated format //
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            // get the number of columns from the 0th element which is the header in the csv file
            Integer numberOfColumns = records.get(0).size();

            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {

                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {

                    newJob.put(headerLabel, record.get(headerLabel));

                }
                allJobs.add(newJob);

            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
