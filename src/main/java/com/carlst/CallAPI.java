package com.carlst;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class CallAPI {

    private static String bearerToken;
    private static String queryURL = "https://purchase.izettle.com/purchases/v2?limit=1000";

    /**
     * Kaller på Zettle API-et og returnerer svaret. Henter access token fra TokenGetter.getToken().
     * 
     * @param queryURLending Slutten på URL-en for å spesifisere API-kallet. F.eks. &descending=true eller &startDate=2022-08-01
     * @return HttpReponse<String> fra API. Må bruke .getBody() for å få JSON-strengen.
     */
    private static HttpResponse<String> getResponse(String queryURLending) {
        HttpResponse<String> response;

        bearerToken = TokenGetter.getToken();
        
        try {
            response = Unirest.get(queryURL + queryURLending)
                    .header("Authorization", "Bearer " + bearerToken)
                    .asString();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to get response from Zettle API.");
        }

        return response;

    }

    /**
     * Returnerer lastPurchaseHash fra en JSON-streng med transaksjoner.
     * 
     * @param jsonResponse JSON-strengen med transaksjoner
     * @return lastPurchaseHash fra JSON-strengen
     */
    public static String getLastPurchaseHash(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String lastPurchaseHash = "";

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            lastPurchaseHash = rootNode.get("lastPurchaseHash").asText();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to get lastPurchaseHash from JSON string");
        }

        return lastPurchaseHash;
    }

    /**
     * Returnerer JSON-strengen fra et API-kall spesifisert med queryURLending
     * 
     * @param queryURLending Slutten på URL-en for å spesifisere API-kallet. F.eks. &descending=true eller &startDate=2022-08-01
     * @return JSON-streng med transaksjoner
     */
    public static String getResponseBody(String queryURLending) {
        return getResponse(queryURLending).getBody();
    }

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