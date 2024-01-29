package storlien.beertracker.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Filehandler {

    /**
     * Leser Person-objektene fra persons.txt-filen til listen Person.objPersons
     */
    public static void readFile() {
        List<Person> objPersons = new ArrayList<>();

        try {
            BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/storlien/beertracker/core/persons.txt"));

            // Hver linje representerer ett Person-objekt
            int index = 0;
            String line;
            String[] lineList;
            while ((line = bf.readLine()) != null) {
                lineList = line.split("-");

                // Instansierer Person-objekt
                objPersons.add(new Person(lineList[0], lineList[1]));

                // Legger til summen
                double amountSpent = Double.parseDouble(lineList[2]);
                objPersons.get(index).setAmountSpent(amountSpent);

                List<String> cardsList = new ArrayList<>(Arrays.asList(lineList));
                cardsList = cardsList.subList(3, cardsList.size());

                for (String card : cardsList) {
                    String firstSix = card.substring(0, 6);
                    String lastFour = card.substring(6);

                    // Legger til kortet
                    objPersons.get(index).addCard(firstSix, lastFour);
                }

                index++;
            }

            bf.close();

            Person.setObjPersons(objPersons);
        }

        catch (Exception e) {
            throw new IllegalStateException("Failed to read persons file: " + e);
        }
    }

    /**
     * Lagrer Person-objektene fra listen Person.objPersons til persons.txt-filen
     */
    public static void saveFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/storlien/beertracker/core/persons.txt"));

            String line;

            for (Person person : Person.getObjPersons()) {
                line = person.getFirstName() + "-" + person.getLastName() + "-" + person.getAmountSpent();

                // Legger til alle kort på linjen
                line += person.getCards().stream().reduce("", (acc, c) -> {
                    acc += "-" + c;
                    return acc;
                });

                // Hvis personen ikke er siste i for-løkken (ikke siste element av listen)
                if (!(Person.getObjPersons().indexOf(person) == Person.getObjPersons().size() - 1)) {
                    line += "\n";
                }

                bw.write(line);
            }

            bw.close();
        }

        catch (Exception e) {
            throw new IllegalStateException("Failed to save to persons file");
        }
    }

    /**
     * Leser lastPurchaseHash.txt-filen og oppdaterer
     * PurchaseReader.lastPurchaseHash
     */
    public static void readHashFile() {
        String lastPurchaseHash;

        try {
            BufferedReader bw = new BufferedReader(new FileReader("src/main/resources/storlien/beertracker/core/lastPurchaseHash.txt"));
            lastPurchaseHash = bw.readLine();
            bw.close();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to read hash file");
        }

        PurchaseReader.setLastPurchaseHash(lastPurchaseHash);
    }

    /**
     * Skriver til lastPurchaseHash.txt-filen med hashen fra
     * PurchaseReader.lastPurchaseHash
     */
    public static void saveHashFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/storlien/beertracker/core/lastPurchaseHash.txt"));
            bw.write(PurchaseReader.getLastPurchaseHash());
            bw.close();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to save to hash file");
        }
    }
}
