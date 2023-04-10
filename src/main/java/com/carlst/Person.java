package com.carlst;

import java.util.ArrayList;
import java.util.List;

public class Person implements Comparable<Person> {
    private static List<Person> objPersons = new ArrayList<>();

    private String firstName;
    private String lastName;
    private double amountSpent;
    private List<String> cards = new ArrayList<>();

    // Constructors

    public Person(String firstName, String lastName) {
        validateName(firstName, lastName);

        this.firstName = firstName;
        this.lastName = lastName;

    }

    public Person(String firstName, String lastName, String cardFirstSix, String cardLastFour) {
        this(firstName, lastName);

        cards.add(cardFirstSix + cardLastFour);
    }

    // Setters

    public void setAmountSpent(double amountSpent) {
        validateAmount(amountSpent);

        this.amountSpent = amountSpent;
    }

    public static void setObjPersons(List<Person> objPersons) {
        Person.objPersons = objPersons;
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

    public static List<Person> getObjPersons() {
        return Person.objPersons;
    }

    // Validators

    public void validateName(String firstName, String lastName) {
        // for (String name : objPersons.stream().map(p -> p.getFirstName()).collect(Collectors.toList())) {
            
        // }
    }

    public void validateAmount(double amount) {

    }

    public void validateCard(String cardFirstSix, String cardLastFour) {

    }

    // Andre metoder

    /**
     * Legger til sum på tidligere sum brukt.
     * 
     * @param amount Sum å legge til tidligere sum brukt
     */
    public void addAmount(double amount) {
        validateAmount(amount);

        amountSpent += amount;
    }

    /**
     * Legger til kort.
     * 
     * @param cardFirstSix Første seks siffer av kortnummeret
     * @param cardLastFour Siste fire siffer av kortnummeret
     */
    public void addCard(String cardFirstSix, String cardLastFour) {
        validateCard(cardFirstSix, cardLastFour);

        cards.add(cardFirstSix + cardLastFour);
    }

    /**
     * Fjerner kort.
     * 
     * @param cardFirstSix Første seks siffer av kortnummeret
     * @param cardLastFour Siste fire siffer av kortnummeret
     */
    public void removeCard(String cardFirstSix, String cardLastFour) {
        validateCard(cardFirstSix, cardLastFour);

        if (!cards.contains(cardFirstSix + cardLastFour)) {
            throw new IllegalArgumentException("Person does not have this card");
        }

        else if (cards.size() == 1) {
            throw new IllegalArgumentException("Cannot remove the only card this person has");
        }

        cards.remove("" + cardFirstSix + cardLastFour);
    }

    /**
     * Metoden .sort() vil sortere Person-objektene fra størst til minst pengesum brukt.
     * 
     * @param that
     * @return
     */
    @Override
    public int compareTo(Person that) {
        return (int) (that.amountSpent - this.amountSpent);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ": " + (int) amountSpent + " kr " + cards;
    }

    public static void main(String[] args) {

    }
}
