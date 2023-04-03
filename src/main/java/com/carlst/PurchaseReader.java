package com.carlst;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PurchaseReader {

    public static Map<String, Double> getPurchases() {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Double> purchases = new HashMap<>();
        String cardNumber;
        double amount;

        try {

            // Henter kjøpshistorikk gjennom API-et
            JsonNode rootNode = objectMapper.readTree(CallAPI.getResponseBody());
            JsonNode purchaseNode = rootNode.get("purchases");

            String paymentType;
            for (JsonNode purchase : purchaseNode) {
                paymentType = purchase.get("payments").get(0).get("type").asText();

                if (!paymentType.equals("IZETTLE_CARD")) {
                    continue;
                }

                cardNumber = purchase.get("payments").get(0).get("attributes").get("maskedPan").asText();
                cardNumber = cardNumber.replaceAll("\\*", "");

                amount = purchase.get("amount").asInt();
                amount = amount / 100; // Gjør om fra øre til kroner

                purchases.put(cardNumber, amount);
            }
        }

        catch (Exception e) {
            throw new IllegalStateException("Failed to call API correctly");
        }

        return purchases;
    }

    public static void main(String[] args) {
        System.out.println(getPurchases());
    }
}