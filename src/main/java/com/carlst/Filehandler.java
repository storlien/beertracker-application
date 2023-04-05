package com.carlst;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Filehandler {

    public void readFile() {
        List<Person> objPersons = new ArrayList<>();

        try {
            BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/json/persons.txt"));

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

            Person.objPersons = objPersons;
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to read file");
        }
    }

    public void saveFile() {
        saveFile(Person.objPersons);
    }

    public void saveFile(List<Person> objPersons) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/json/persons.txt"));

            String line;

            for (Person person : objPersons) {
                line = person.getFirstName() + "-" + person.getLastName() + "-" + person.getAmountSpent();

                // Legger til alle kort på linjen
                line += person.getCards().stream().reduce("", (acc, c) -> {
                    acc += "-" + c;
                    return acc;
                });

                // Hvis personen ikke er siste i for-løkken (ikke siste element av listen)
                if (!(objPersons.indexOf(person) == objPersons.size() - 1)) {
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

    public static void main(String[] args) {
        Filehandler fh = new Filehandler();

        Person edward = new Person("Edward", "Storlien", "537217", "3332");
        edward.depositAmount(100.5);
        edward.addCard("696969", "6969");
        edward.addCard("989898", "4545");

        Person mamma = new Person("Mamma", "Storlien", "123456", "7890");
        mamma.depositAmount(16.7);
        mamma.addCard("990023", "9200");

        Person fnedd = new Person("Fnedd", "Storlien", "123456", "7890");
        fnedd.depositAmount(69.69);
        fnedd.addCard("545478", "8966");

        Person louise = new Person("Louise", "Storlien");

        List<Person> personer = new ArrayList<>();
        personer.add(edward);
        personer.add(mamma);
        personer.add(fnedd);
        personer.add(louise);

        fh.saveFile(personer);

        System.out.println("Ferdig med lagring av fil.");
    }
}
