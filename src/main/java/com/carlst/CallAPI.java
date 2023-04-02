package com.carlst;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class CallAPI {
    // private static String queryURL =
    // "https://finance.izettle.com/v2/accounts/LIQUID/transactions?start=2023-01-01T00:00:00.0%2B01:00&end=2023-03-01T00:00:00.0%2B01:00&includeTransactionType=PAYMENT&limit=1000&offset=0";
    // private String queryURLpurchase =
    // "https://purchase.izettle.com/purchases/v2?startDate=2023-01-01T00:00:00.0%2B01:00";
    private static String queryURLpurchase = "https://purchase.izettle.com/purchases/v2?limit=50&descending=true";

    private static String bearerToken;

    private static HttpResponse<String> getResponse() {
        HttpResponse<String> response;

        bearerToken = TokenGetter.retrieveGetToken(); // Denne må gjøres om på når TokenGetter bare skal stå og gå.
                                                      // Altså i ferdig program.

        try {
            response = Unirest.get(queryURLpurchase)
                    .header("Authorization", "Bearer " + bearerToken)
                    .asString();
        }

        catch (Exception e) {
            throw new IllegalStateException("Failed to get reponse from Zettle API.");
        }

        return response;

    }

    public static String getResponseBody() {
        return getResponse().getBody();
    }

    public static int getResponseStatus() {
        return getResponse().getStatus();
    }

    public static void main(String[] args) {
        System.out.println(getResponseBody());
    }
}