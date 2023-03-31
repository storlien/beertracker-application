package com.carlst;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Transaction {
    private String queryURL = "https://finance.izettle.com/v2/accounts/LIQUID/transactions?start=2023-01-01T00:00:00.0%2B01:00&end=2023-03-01T00:00:00.0%2B01:00&includeTransactionType=PAYMENT&limit=1000&offset=0";
    // private String queryURLpurchase =
    // "https://purchase.izettle.com/purchases/v2?startDate=2023-01-01T00:00:00.0%2B01:00";
    private String queryURLpurchase = "https://purchase.izettle.com/purchases/v2?limit=50&descending=true";
    private String bearerToken;

    private HttpResponse<String> getResponse() {
        HttpResponse<String> response;

        try {
            response = Unirest.get(queryURLpurchase)
                    .header("Authorization", bearerToken)
                    .asString();
        }

        catch (Exception e) {
            System.out.println("Failed to get reponse from Zettle API.");
            throw new IllegalStateException();
        }

        // if (response.getStatus() == 401) {
        // System.out.println("Invalid token.");
        // }

        // return response;
    }

    public String getResponseBody() {
        return getResponse().getBody();
    }

    public int getResponseStatus() {
        return getResponse().getStatus();
    }

    public static void main(String[] args) {
        System.out.println("Transaksjonsliste");

        Transaction trans = new Transaction();

        System.out.println(trans.getResponse());

        System.out.println(trans.getResponseBody());
        System.out.println(trans.getResponseStatus());
    }
}