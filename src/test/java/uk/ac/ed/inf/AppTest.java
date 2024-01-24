package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class AppTest extends TestCase {
    public AppTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void testAllMonths()  {
        testMonth("2023-09", 30); // September
        testMonth("2023-10", 31); // October
        testMonth("2023-11", 30); // November
        testMonth("2023-12", 31); // December
        testMonth("2024-01", 31); // January
        testMonth("2024-02", 29); // February (Leap Year)
        // Add more months as needed
    }

    private void testMonth(String yearMonth, int daysInMonth) {
        String date = String.format("%s-%02d", yearMonth, ThreadLocalRandom.current().nextInt(1, daysInMonth + 1));
        String[] args = {date, "https://ilp-rest.azurewebsites.net"};
        App.main(args);
        String path = System.getProperty("user.dir");
        // Define the expected files to be created
        File deliveryFile = new File(path + "/resultfiles/deliveries-" + date + ".json");
        File flightpathFile = new File(path + "/resultfiles/flightpath-" + date + ".json");
        File geojsonFile = new File(path + "/resultfiles/drone-" + date + ".geojson");
        // Assert that all files were created
        assertTrue("File should exist", deliveryFile.exists());
        assertTrue("File should exist", flightpathFile.exists());
        assertTrue("File should exist", geojsonFile.exists());
    }


    /**
     * Performance test case,
     */
    public void testPerformance()  {
        String date = String.format("2023-%02d-%02d", ThreadLocalRandom.current().nextInt(9, 12 + 1), ThreadLocalRandom.current().nextInt(1, 29));
        String[] args = {date, "https://ilp-rest.azurewebsites.net"};
        long startTime = System.currentTimeMillis();
        App.main(args);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        assertTrue("Total time taken should be less than 60s", totalTime < 60000);
    }

}
