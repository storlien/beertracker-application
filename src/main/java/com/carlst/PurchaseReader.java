package com.carlst;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class PurchaseReader {

    // Henter alle kjøp fra og med en dato
    public static Multimap<String, Double> getPurchasesFromDate(String date) {
        String jsonString = CallAPI.getResponseBody("&startDate=" + date);

        return getNewerPurchases(jsonString);
    }

    // Henter alle nyere kjøp fra og med det eldste kjøpet på en spørring
    public static Multimap<String, Double> getNewerPurchases(String jsonString) {
        Multimap<String, Double> purchases = ArrayListMultimap.create();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Så lenge det er flere kjøp å lese av
            while (!objectMapper.readTree(jsonString).get("purchases").isEmpty()) {
                purchases.putAll(getPurchases(jsonString));

                // Henter de 1000 neste transaksjonene
                jsonString = CallAPI.getResponseBody("&lastPurchaseHash=" + CallAPI.getLastPurchaseHash(jsonString));
            }
        }

        catch (Exception e) {

        }
            
        return purchases;
    }

    public static Multimap<String, Double> getPurchasesQuery(String queryURLending) {
        return getPurchases(CallAPI.getResponseBody(queryURLending));
    }

    public static Multimap<String, Double> getPurchases(String jsonString) {
    
        ObjectMapper objectMapper = new ObjectMapper();

        Multimap<String, Double> purchases = ArrayListMultimap.create();
        String cardNumber;
        double amount;

        try {

            // Henter kjøpshistorikk
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode purchaseNode = rootNode.get("purchases");

            // Går gjennom kjøpshistorikken og mapper kortnummer til kortnumre til personer
            String paymentType;
            for (JsonNode purchase : purchaseNode) {
                paymentType = purchase.get("payments").get(0).get("type").asText();

                if (!paymentType.equals("IZETTLE_CARD")) {
                    continue;
                }

                cardNumber = purchase.get("payments").get(0).get("attributes").get("maskedPan").asText();
                cardNumber = cardNumber.replaceAll("\\*", ""); // Fjerner asteriskene fra kortnummeret

                amount = purchase.get("amount").asInt();
                amount = amount / 100; // Gjør om fra øre til kroner

                purchases.put(cardNumber, amount);
            }
        }

        catch (Exception e) {
            throw new IllegalStateException("Failed to read JSON string: " + e);
        }

        return purchases;
    }

    public static void main(String[] args) {
        // System.out.println(getPurchasesFromDate("2022-08-01"));
        System.out.println(getPurchasesFromDate("2022-08-01").size());
        // System.out.println(getPurchasesFromDate("2022-08-01").keySet().size());
    }
}