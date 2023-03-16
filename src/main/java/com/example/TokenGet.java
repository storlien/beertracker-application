package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TokenGet {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://oauth.zettle.com/token");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write(
                "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&client_id=be3dbda8-c24d-11ed-a4c2-3ff3847929a1&assertion=eyJraWQiOiIwIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoyNjI1NDk1MjQ0LCJzdWIiOiJmNDU1NjRmOC0zZmNhLTExZWQtOGI0ZC03NzY3M2JjYmQ5NGIiLCJpYXQiOjE2Nzg3ODc0NjgsInJlbmV3ZWQiOmZhbHNlLCJjbGllbnRfaWQiOiJiZTNkYmRhOC1jMjRkLTExZWQtYTRjMi0zZmYzODQ3OTI5YTEiLCJ0eXBlIjoidXNlci1hc3NlcnRpb24iLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiJmNDU1NjRmOC0zZmNhLTExZWQtOGI0ZC03NzY3M2JjYmQ5NGIiLCJvcmdVdWlkIjoiZjQ1MWM4MGMtM2ZjYS0xMWVkLWFmMDktNTAxYzczNWJiNzVkIiwidXNlclJvbGUiOiJPV05FUiJ9LCJzY29wZSI6WyJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIl19.hTrDytTlfeHhFSvvTR2JOaSGoAGuE9i4UWEbQdQ3BM831nSnXRh2BYSz0SNh8s_Mk0A2MthcUTb8lAgVUwbOwhDbHSXWgw-jnxuUl8p3HP3p6uwIWyaK4l2GrOpY6YAULQwF1CPNosFv3H0cc5GBh-aywLApjT7l57D96KkOETLEi6uD5D5kVw09ffD6CpGSp9S4VjpNlVjPlKLy1U-4WJhK5V9LU1WRrEezh3be05Uw6N0Zk9qyFM9W5Ct03WrPp9FUlBC7kaE64TUSc9I3O3TFQF4KNR83mssALDClL3e9wsc3h1WbYM8puUYryGjTQYscs17ASm4TsH4GuY8DsQ");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
    }
}
