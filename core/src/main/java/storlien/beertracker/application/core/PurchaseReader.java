package storlien.beertracker.application.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class PurchaseReader {

    private static String lastPurchaseHash;

    /**
     * Henter alle kjøp fra og med en dato og returnerer Multimap
     *
     * @param date Dato i formatet "YYYY-MM-DD".
     * @return Multimap med kortnummer som key og liste med summer som value.
     */
    public static Multimap<String, Double> getPurchasesFromDate(String date) {
        String jsonString = CallAPI.getResponseBody("&startDate=" + date);

        return getNewerPurchases(jsonString);
    }

    /**
     * Henter alle nyere kjøp fra og med det eldste kjøpet på en spørring (JSON
     * string).
     * Fortsetter å hente til det ikke finnes flere nye kjøp.
     * Hvis det ikke var flere nye kjøp fra starten av metodekallet, returneres en
     * tom Multimap.
     * Synchronized metode for at metoden ikke skal kunne kjøres flere av i
     * parallell.
     * Det kan bli tull med variabelen PurchaseReader.lastPurchaseHash dersom
     * metoden kjøres flere ganger samtidig.
     *
     * @param jsonString JSON string. Henter alle nye kjøp fra og med den eldste på
     *                   denne strengen.
     * @return Multimap med kortnummer som key og liste med summer som value.
     */
    public static synchronized Multimap<String, Double> getNewerPurchases(String jsonString) {

        Multimap<String, Double> purchases = ArrayListMultimap.create();
        ObjectMapper objectMapper = new ObjectMapper();
        String lastPurchaseHash;

        try {
            // Så lenge det er flere kjøp å lese av
            while (!objectMapper.readTree(jsonString).get("purchases").isEmpty()) {
                purchases.putAll(getPurchases(jsonString));

                // Henter de 1000 neste transaksjonene
                lastPurchaseHash = readLastPurchaseHash(jsonString);
                jsonString = getJsonStringLastHash(lastPurchaseHash);
                setLastPurchaseHash(lastPurchaseHash);
            }
        }

        catch (Exception e) {
            throw new IllegalStateException("Failed to get newer purchases");
        }

        return purchases;
    }

    /**
     * Returnerer de neste (opp til) 1000 transaksjoner etter (ikke inkludert)
     * lastPurchaseHash.
     * Dersom det ikke finnes nyere transaksjoner etter lastPurchaseHash, blir
     * "purchases"-listen i JSON-strengen tom.
     *
     * @param lastPurchaseHash Nyere transaksjoner etter denne lastPurchaseHash
     * @return JSON-streng med transaksjoner etter (ikke inkludert) lastPurchaseHash
     */
    public static String getJsonStringLastHash(String lastPurchaseHash) {
        return CallAPI.getResponseBody("&lastPurchaseHash=" + lastPurchaseHash);
    }

    /**
     * Leser JSON-streng og returnerer Multimap med kortnummer mappet til liste med
     * summer brukt på det kortnummeret i den JSON-strengen.
     *
     * @param jsonString JSON-streng med transaksjoner
     * @return Multimap med kortnummer mappet til liste med summer brukt på det
     *         kortnummeret
     */
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
            throw new IllegalArgumentException("Failed to read purchases from JSON string");
        }

        return purchases;
    }

    // Setters

    /**
     * Setter ny lastPurchaseHash
     *
     * @param lastPurchaseHash Ny lastPurchaseHash
     */
    public static void setLastPurchaseHash(String lastPurchaseHash) {
        PurchaseReader.lastPurchaseHash = lastPurchaseHash;
    }

    // Getters

    /**
     * Returnerer lastPurchaseHash fra PurchaseReader.lastPurchaseHash
     *
     * @return lastPurchaseHash
     */
    public static String getLastPurchaseHash() {
        return lastPurchaseHash;
    }

    /**
     * Returnerer lastPurchaseHash fra en JSON-streng med transaksjoner.
     *
     * @param jsonResponse JSON-strengen med transaksjoner
     * @return lastPurchaseHash fra JSON-strengen
     */
    public static String readLastPurchaseHash(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String lastPurchaseHash = "";

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            lastPurchaseHash = rootNode.get("lastPurchaseHash").asText();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to read lastPurchaseHash from JSON string");
        }

        return lastPurchaseHash;
    }
}