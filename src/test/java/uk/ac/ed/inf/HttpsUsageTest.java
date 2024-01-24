package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class HttpsUsageTest {

    private static final int CONNECTION_TIMEOUT_MS = 5000; // Set a timeout of 5 seconds

    @Test
    public void testHttpsUsageAndCertificateValidity() {
        String restServiceUrl = "https://ilp-rest.azurewebsites.net"; // Replace with the actual REST service URL

        try {
            URL url = new URL(restServiceUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // Verify that the connection is using HTTPS
            assertTrue(connection.getURL().getProtocol().equalsIgnoreCase("https"), "Connection is not using HTTPS");

            // Set a connection timeout
            connection.setConnectTimeout(CONNECTION_TIMEOUT_MS);

            // Verify the SSL/TLS certificate validity
            try {
                connection.connect();
                assertNotNull(connection.getServerCertificates(), "No server certificates found");
                assertTrue(connection.getServerCertificates().length > 0, "No server certificates found");
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}
