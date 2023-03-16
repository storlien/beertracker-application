// curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&client_id=be3dbda8-c24d-11ed-a4c2-3ff3847929a1&assertion=eyJraWQiOiIwIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoyNjI1NDk1MjQ0LCJzdWIiOiJmNDU1NjRmOC0zZmNhLTExZWQtOGI0ZC03NzY3M2JjYmQ5NGIiLCJpYXQiOjE2Nzg3ODc0NjgsInJlbmV3ZWQiOmZhbHNlLCJjbGllbnRfaWQiOiJiZTNkYmRhOC1jMjRkLTExZWQtYTRjMi0zZmYzODQ3OTI5YTEiLCJ0eXBlIjoidXNlci1hc3NlcnRpb24iLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiJmNDU1NjRmOC0zZmNhLTExZWQtOGI0ZC03NzY3M2JjYmQ5NGIiLCJvcmdVdWlkIjoiZjQ1MWM4MGMtM2ZjYS0xMWVkLWFmMDktNTAxYzczNWJiNzVkIiwidXNlclJvbGUiOiJPV05FUiJ9LCJzY29wZSI6WyJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIl19.hTrDytTlfeHhFSvvTR2JOaSGoAGuE9i4UWEbQdQ3BM831nSnXRh2BYSz0SNh8s_Mk0A2MthcUTb8lAgVUwbOwhDbHSXWgw-jnxuUl8p3HP3p6uwIWyaK4l2GrOpY6YAULQwF1CPNosFv3H0cc5GBh-aywLApjT7l57D96KkOETLEi6uD5D5kVw09ffD6CpGSp9S4VjpNlVjPlKLy1U-4WJhK5V9LU1WRrEezh3be05Uw6N0Zk9qyFM9W5Ct03WrPp9FUlBC7kaE64TUSc9I3O3TFQF4KNR83mssALDClL3e9wsc3h1WbYM8puUYryGjTQYscs17ASm4TsH4GuY8DsQ" https://oauth.zettle.com/token

package com.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Transaction {
    private String queryURL = "https://finance.izettle.com/v2/accounts/LIQUID/transactions?start=2023-01-01T00:00:00.0%2B01:00&end=2023-03-01T00:00:00.0%2B01:00&includeTransactionType=PAYMENT&limit=1000&offset=0";
    // private String queryURLpurchase =
    // "https://purchase.izettle.com/purchases/v2?startDate=2023-01-01T00:00:00.0%2B01:00";
    private String queryURLpurchase = "https://purchase.izettle.com/purchases/v2?limit=50&descending=true";
    private String bearerToken = "Bearer eyJraWQiOiIxNjc4Nzg4MDUxMTAyIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoxNjc4ODM2MDEyLCJzdWIiOiJmNDU1NjRmOC0zZmNhLTExZWQtOGI0ZC03NzY3M2JjYmQ5NGIiLCJpYXQiOjE2Nzg4Mjg4MTIsInVzZXIiOnsidXNlclR5cGUiOiJVU0VSIiwidXVpZCI6ImY0NTU2NGY4LTNmY2EtMTFlZC04YjRkLTc3NjczYmNiZDk0YiIsIm9yZ1V1aWQiOiJmNDUxYzgwYy0zZmNhLTExZWQtYWYwOS01MDFjNzM1YmI3NWQiLCJ1c2VyUm9sZSI6Ik9XTkVSIn0sInNjb3BlIjpbIlJFQUQ6RklOQU5DRSIsIlJFQUQ6UFJPRFVDVCIsIlJFQUQ6UFVSQ0hBU0UiXSwicmVuZXdlZCI6ZmFsc2UsImNsaWVudF9pZCI6ImJlM2RiZGE4LWMyNGQtMTFlZC1hNGMyLTNmZjM4NDc5MjlhMSJ9.nS2qx-P8Lqn-dJHuP1Vj-BpHnch2HIXuPDCiWLClkqljifJuB6kyn9im_Hum_aqaCJaPp3rJPVB0RCEHwRFp6TuerDuliSEIuJc3qtfqDTb80LqMd7m1qFZEvWmF2O_fds0yrj2bspmHg-ErK-DkUe_ee3gdHcuamW8JvVCMY70XrTi0opFdellrGS3idc44SJNllai-aZyzvw2jXEHfg3NJ0JHSsXX7RZ8eA3xEaheysmdktUhrw3toK4uf5AyID9ZuuyonkEf1ZnXXPhbJG9JbR-86xc_OijQ2KFgPAIvPRejdlv0aikX_iLH8YrxCmQLlYudlKkzVH-UydJtkhA";

    private HttpResponse<String> getResponse() throws UnirestException {
        HttpResponse<String> response = Unirest.get(queryURLpurchase)
                .header("Authorization", bearerToken)
                .asString();

        return response;
    }

    public String getResponseBody() throws UnirestException {
        return getResponse().getBody();
    }

    public int getResponseStatus() throws UnirestException {
        return getResponse().getStatus();
    }

    public static void main(String[] args) throws UnirestException {
        System.out.println("Transaksjonsliste");

        Transaction trans = new Transaction();

        System.out.println(trans.getResponseBody());
        System.out.println(trans.getResponseStatus());
    }
}