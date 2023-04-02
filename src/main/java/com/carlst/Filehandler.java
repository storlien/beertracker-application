package com.carlst;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Filehandler {
    List<Person> objPersons = new ArrayList<>();

    public void readFile() {

        try {
            BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/json/persons.txt"));

            String line;
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
            }

            bf.close();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Could not find file or something lol");
        }
    }

    public void saveFile(List<Person> objPersons) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/json/persons.txt"));

            String line;

            for (Person person : objPersons) {
                line = person.getFirstName() + "-" + person.getLastName();

                line += person.getCards().stream().reduce("", (acc, c) -> {
                    acc += "-" + c;
                    return acc;
                });

                System.out.println(line);

                // Hvis personen ikke er siste i for-løkken (ikke siste element av listen)
                if (!(objPersons.indexOf(person) == objPersons.size() - 1)) {
                    line += "\n";
                }

                bw.write(line);
            }

            bw.close();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Could not find file or something lol");
        }
    }

    public static void main(String[] args) {
        Filehandler fh = new Filehandler();

        Person edward = new Person("Edward", "Storlien", 123456, 7890);
        edward.addCard(696969, 6969);
        edward.addCard(989898, 4545);

        Person mamma = new Person("Mamma", "Storlien", 123456, 7890);
        mamma.addCard(990023, 9200);

        Person fnedd = new Person("Fnedd", "Storlien", 123456, 7890);
        fnedd.addCard(666666, 6666);

        List<Person> personer = new ArrayList<>();
        personer.add(edward);
        personer.add(mamma);
        personer.add(fnedd);

        fh.saveFile(personer);
    }
}
