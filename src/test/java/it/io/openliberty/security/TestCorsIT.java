//package it.io.openliberty.security;
//
//import static org.junit.Assert.assertEquals;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class TestCorsIT {
//
//    String pathToHost = "http://localhost:5050/";
//
//    @Before
//    public void setUp() {
//        // JVM does not allow restricted headers by default
//        // Set to true for CORS testing
//        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
//    }
//
//    @Test
//    public void testSimpleCorsRequest() throws IOException  {
//        HttpURLConnection connection = HttpUtils.sendRequest(
//        	pathToHost + "user",
//            "GET",
//            TestData.simpleRequestHeaders);
//        checkCorsResponse(connection, TestData.simpleResponseHeaders);
//        printResponseHeaders(connection, "Simple CORS Request");
//    }
//
//
//    public void checkCorsResponse(HttpURLConnection connection,
//                                Map<String, String> expectedHeaders) throws IOException {
//        assertEquals("Invalid HTTP response code", 200, connection.getResponseCode());
//        expectedHeaders.forEach((responseHeader, value) -> {
//            assertEquals("Unexpected value for " + responseHeader + " header", value,
//                                            connection.getHeaderField(responseHeader));
//        });
//    }
//
//    public static void printResponseHeaders(HttpURLConnection connection, String label) {
//        System.out.println("--- " + label + " ---");
//        Map<String, java.util.List<String>> map = connection.getHeaderFields();
//        for (Entry<String, java.util.List<String>> entry : map.entrySet()) {
//            System.out.println("Header " + entry.getKey() + " = " + entry.getValue());
//        }
//        System.out.println();
//    }
//
//}