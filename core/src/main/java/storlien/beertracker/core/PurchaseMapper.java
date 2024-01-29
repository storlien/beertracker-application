package storlien.beertracker.core;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Multimap;

public class PurchaseMapper {

    /**
     * Mapper kjøp og legger til summen på tidligere sum til Person-objektet
     *
     * @param purchases Multimap med kortnummer mappet til liste av summer brukt på
     *                  det kortnummeret
     */
    public static void mapPurchases(Multimap<String, Double> purchases) {

        Map<String, Person> cardHolders = getCardHolders();

        for (String cardNumberPurchase : purchases.keySet()) {

            for (String cardNumberHolder : cardHolders.keySet()) {

                // Legger til summen på kortholders tidligere brukte sum dersom kortnumrene
                // samsvarer
                if (cardNumberPurchase.equals(cardNumberHolder)) {

                    // Summerer alle kjøp gjort med det kortet og legger til summen hos personen
                    double amount = purchases.get(cardNumberPurchase).stream().reduce(0.0, (acc, b) -> acc + b);
                    cardHolders.get(cardNumberHolder).addAmount(amount);

                    break; // Et kort er kun mappet til én person. Hopper til neste kjøp
                }
            }
        }
    }

    /**
     * Lager HashMap med kortnummer mappet til Person-objekt. Henter Person-objekter
     * fra Person.objPersons
     *
     * @return HashMap med kortnummer mappet til Person-objekt
     */
    private static Map<String, Person> getCardHolders() {
        Map<String, Person> cardHolders = new HashMap<>();

        for (Person person : Person.getObjPersons()) {
            for (String card : person.getCards()) {
                cardHolders.put(card, person);
            }
        }

        return cardHolders;
    }

    /**
     * Setter alle personers sum til 0
     */
    public static void resetAmountSpent() {
        Person.getObjPersons().stream().forEach(p -> p.setAmountSpent(0));
    }
}