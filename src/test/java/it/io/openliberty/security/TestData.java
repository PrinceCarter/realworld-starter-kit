package it.io.openliberty.security;

import java.util.HashMap;
import java.util.Map;

public class TestData {

    public static String REQUEST_HEADER_ORIGIN = "Origin";
    public static String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    public static String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    public static String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static String RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static String RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE = "Access-Control-Allow-Max-Age";
    public static String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static Map<String, String> simpleRequestHeaders = new HashMap<String, String>();
    public static Map<String, String> simpleResponseHeaders = new HashMap<String, String>();

    public static Map<String, String> preflightRequestHeaders = new HashMap<String, String>();
    public static Map<String, String> preflightResponseHeaders = new HashMap<String, String>();

    static {
        simpleRequestHeaders.put(REQUEST_HEADER_ORIGIN, "*");
        simpleResponseHeaders.put(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        simpleResponseHeaders.put(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
//        simpleResponseHeaders.put(RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, "MyHeader");

        preflightRequestHeaders.put(REQUEST_HEADER_ORIGIN, "anywebsiteyoulike.com");
        preflightRequestHeaders.put(REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD, "DELETE");
        preflightRequestHeaders.put(REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS, "MyOwnHeader2");

        preflightResponseHeaders.put(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        preflightResponseHeaders.put(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        preflightResponseHeaders.put(RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE, "10");
        preflightResponseHeaders.put(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS, DELETE");
        preflightResponseHeaders.put(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS, "MyOwnHeader1, MyOwnHeader2");
    }

}
