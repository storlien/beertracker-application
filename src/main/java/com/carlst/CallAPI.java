package com.carlst;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class CallAPI {

    private static String bearerToken;
    private static String queryURL = "https://purchase.izettle.com/purchases/v2?limit=1000"; // ha med &descending=true eller &startDate=2022-08-01

    private static HttpResponse<String> getResponse(String queryURLending) {
        HttpResponse<String> response;

        bearerToken = TokenGetter.retrieveGetToken(); // Denne må gjøres om på når TokenGetter bare skal stå og gå. Altså i ferdig program.

        try {
            response = Unirest.get(queryURL + queryURLending)
                    .header("Authorization", "Bearer " + bearerToken)
                    .asString();
        }

        catch (Exception e) {
            throw new IllegalStateException("Failed to get response from Zettle API.");
        }

        return response;

    }

    public static String getLastPurchaseHash(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String lastPurchaseHash = "";

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            lastPurchaseHash = rootNode.get("lastPurchaseHash").asText();
        }

        catch (Exception e) {

        }

        return lastPurchaseHash;
    }

    public static String getResponseBody(String queryURLending) {
        return getResponse(queryURLending).getBody();
    }

    // public static int getResponseStatus() {
    //     return getResponseStatus("&descending=true");
    // }

    // public static int getResponseStatus(String lastPurchaseHash) {
    //     return getResponse(lastPurchaseHash).getStatus();
    // }

    public static void main(String[] args) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/json/labambaAugust.json"));

            bw.write(getResponseBody("&startDate=2022-08-01"));

            bw.close();
        }

        catch (Exception e) {
            
        }

    }
}