package com.carlst;

import java.util.HashMap;
import java.util.Map;

public class PurchaseMapper {
    
    // Mapper kjøp og legger til summen på tidligere sum
    public void mapPurchases(Map<String, Double> purchases) {
        Map<String, Person> cardHolders = getCardHolders();

        for (String cardNumberPurchase : purchases.keySet()) {

            for (String cardNumberHolder : cardHolders.keySet()) {

                // Legger til summen på kortholders tidligere brukte sum dersom kortnumrene samsvarer
                if (cardNumberPurchase.equals(cardNumberHolder)) {
                    cardHolders.get(cardNumberHolder).depositAmount(purchases.get(cardNumberPurchase));
                    break; // Et kort er kun mappet til én person. Hopper til neste kjøp
                }
            }
        }
    }

    // Lager HashMap med kortnummer mappet til Person-objekt
    public Map<String, Person> getCardHolders() {
        Map<String, Person> cardHolders = new HashMap<>();

        for (Person person : Person.objPersons) {
            for (String card : person.getCards()) {
                cardHolders.put(card, person);
            }
        }

        return cardHolders;
    }

    private void resetAmountSpent() {
        Person.objPersons.stream().forEach(p -> p.setAmountSpent(0));
    }

    public static void main(String[] args) {
        PurchaseMapper mapper = new PurchaseMapper();
        Filehandler fh = new Filehandler();

        fh.readFile();
        mapper.resetAmountSpent();

        System.out.println("Kjøpshistorikk:");
        Map<String, Double> purchaseList = PurchaseReader.getPurchases();
        System.out.println(purchaseList);

        System.out.println("Personer før");
        System.out.println(Person.objPersons);

        mapper.mapPurchases(purchaseList);

        System.out.println("Personer etter");
        System.out.println(Person.objPersons);

        fh.saveFile();

        // System.out.println("Reset amount spent:");
        // mapper.resetAmountSpent();
    }
}