package com.carlst;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Person {
    private String firstName;
    private String lastName;
    private Map<Integer, Integer> cards = new HashMap<>();

    public Person(String firstName, String lastName, int cardFirstSix, int cardLastFour) {
        this.firstName = firstName;
        this.lastName = lastName;
        cards.put(cardFirstSix, cardLastFour);
    }

    // Setters

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCard(int cardFirstSix, int cardLastFour) {
        cards.put(cardFirstSix, cardLastFour);
    }

    // Getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Map<Integer, Integer> getCards() {
        return cards;
    }

    // Andre metoder

    public void removeCard(int cardFirstSix, int cardLastFour) {
        if (!cards.containsKey(cardFirstSix) || cards.get(cardFirstSix) != cardLastFour) {
            throw new IllegalArgumentException("Card does not exist");
        }

        cards.remove(cardFirstSix, cardLastFour);
    }

    public static void main(String[] args) throws IOException {
        Person edward = new Person("Edward", "Storlien", 123456, 7890);
        edward.setCard(112233, 4455);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("src/main/resources/json/edward.json"), edward);

    }
}
