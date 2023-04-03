package com.carlst;

import java.util.ArrayList;
import java.util.List;

public class Person {
    public static List<Person> objPersons = new ArrayList<>();

    private String firstName;
    private String lastName;
    private double amountSpent;
    private List<String> cards = new ArrayList<>();

    // Constructors

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public Person(String firstName, String lastName, int cardFirstSix, int cardLastFour) {
        this(firstName, lastName);

        cards.add("" + cardFirstSix + cardLastFour);
    }

    // Setters

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
    }

    // Getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public List<String> getCards() {
        return cards;
    }

    // Andre metoder

    public void depositAmount(double amount) {
        amountSpent += amount;
    }

    public void withdrawAmount(double amount) {
        amountSpent -= amount;
    }

    public void addCard(int cardFirstSix, int cardLastFour) {
        cards.add("" + cardFirstSix + cardLastFour);
    }

    public void removeCard(int cardFirstSix, int cardLastFour) {
        if (!cards.contains("" + cardFirstSix + cardLastFour)) {
            throw new IllegalArgumentException("Person does not have this card");
        }

        cards.remove("" + cardFirstSix + cardLastFour);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ": " + amountSpent + ", " + cards;
    }

    public static void main(String[] args) {

    }
}
