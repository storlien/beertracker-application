package com.carlst;

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
            BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/persons.txt"));

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
            throw new IllegalArgumentException("Failed to read file");
        }
    }

    /**
     * Lagrer Person-objektene fra listen Person.objPersons til persons.txt-filen
     */
    public static void saveFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/persons.txt"));

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
            throw new IllegalArgumentException("Failed to write to file");
        }
    }

    /**
     * Leser lastPurchaseHash.txt-filen og oppdaterer PurchaseReader.lastPurchaseHash
     */
    public static void readHashFile() {
        String lastPurchaseHash;

        try {
            BufferedReader bw = new BufferedReader(new FileReader("src/main/resources/lastPurchaseHash.txt"));
            lastPurchaseHash = bw.readLine();
            bw.close();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to read hash file");
        }

        PurchaseReader.setLastPurchaseHash(lastPurchaseHash);
    }

    /**
     * Skriver til lastPurchaseHash.txt-filen med hashen fra PurchaseReader.lastPurchaseHash
     */
    public static void saveHashFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/lastPurchaseHash.txt"));
            bw.write(PurchaseReader.getLastPurchaseHash());
            bw.close();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to write to hash file");
        }
    }

    public static void main(String[] args) {
        Person edward = new Person("Edward", "Storlien", "537217", "3332");
        edward.addAmount(100.5);
        edward.addCard("696969", "6969");
        edward.addCard("989898", "4545");

        Person mamma = new Person("Mamma", "Storlien", "123456", "7890");
        mamma.addAmount(16.7);
        mamma.addCard("990023", "9200");

        Person fnedd = new Person("Fnedd", "Storlien", "123456", "7890");
        fnedd.addAmount(69.69);
        fnedd.addCard("545478", "8966");

        Person louise = new Person("Louise", "Storlien");

        List<Person> personer = new ArrayList<>();
        personer.add(edward);
        personer.add(mamma);
        personer.add(fnedd);
        personer.add(louise);

        // saveFile(personer);

        System.out.println("Ferdig med lagring av fil.");
    }
}
