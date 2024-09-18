package storlien.beertracker.application.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Person implements Comparable<Person> {
    private static List<Person> objPersons = new ArrayList<>();

    private String firstName;
    private String lastName;
    private double amountSpent;
    private List<String> cards = new ArrayList<>();

    // Constructors

    public Person(String firstName, String lastName) {
        if (!validateName(firstName, lastName)) {
            throw new IllegalArgumentException("Invalid name");
        }

        this.firstName = firstName;
        this.lastName = lastName;

    }

    public Person(String firstName, String lastName, String cardFirstSix, String cardLastFour) {
        if (!validateName(firstName, lastName)) {
            throw new IllegalArgumentException("Invalid name");
        }

        else if (!validateCard(cardFirstSix, cardLastFour)) {
            throw new IllegalArgumentException("Invalid card");
        }

        this.firstName = firstName;
        this.lastName = lastName;

        cards.add(cardFirstSix + cardLastFour);
    }

    // Setters

    public void setAmountSpent(double amountSpent) {
        if (!validateAmount(amountSpent)) {
            throw new IllegalArgumentException("Amount cannot be below zero");
        }

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

    /**
     * Validerer om et navn er gyldig. Sjekker også om navnet er registrert fra før.
     *
     * @param firstName
     * @param lastName
     * @return
     */
    public boolean validateName(String firstName, String lastName) {

        Pattern patt = Pattern.compile("\\p{L}+( \\p{L}+)*");

        if (!patt.matcher(firstName).matches() || !patt.matcher(lastName).matches()) {
            return false;
        }

        List<String> fNames = objPersons.stream().map(p -> p.getFirstName()).collect(Collectors.toList());
        List<String> lNames = objPersons.stream().map(p -> p.getLastName()).collect(Collectors.toList());

        for (int i = 0; i < fNames.size(); i++) {
            if (firstName.equals(fNames.get(i)) && lastName.equals(lNames.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validerer om kjøpesummen er gyldig.
     *
     * @param amount Kjøpesum
     * @return True dersom kjøpesummen er gyldig, false ellers
     */
    public boolean validateAmount(double amount) {

        if (amount < 0) {
            return false;
        }

        return true;
    }

    /**
     * Validerer om et kort er gyldig. Sjekker også om kortet er lagt inn fra før,
     * uansett person.
     *
     * @param cardFirstSix Første seks siffer av kortnummeret
     * @param cardLastFour Siste fire siffer av kortnummeret
     * @return True dersom kortet er gyldig, false ellers
     */
    public boolean validateCard(String cardFirstSix, String cardLastFour) {

        if (!Pattern.compile("\\d{6}").matcher(cardFirstSix).matches()
                || !Pattern.compile("\\d{4}").matcher(cardLastFour).matches()) {
            return false;
        }

        String newCard = cardFirstSix + cardLastFour;

        for (Person person : objPersons) {
            for (String card : person.getCards()) {
                if (card.equals(newCard)) {
                    return false;
                }
            }
        }

        return true;

    }

    // Andre metoder

    /**
     * Legger til sum på tidligere sum brukt.
     *
     * @param amount Sum å legge til tidligere sum brukt
     */
    public void addAmount(double amount) {
        if (!validateAmount(amount)) {
            throw new IllegalArgumentException("Amount cannot be below zero");
        }

        amountSpent += amount;
    }

    /**
     * Legger til kort.
     *
     * @param cardFirstSix Første seks siffer av kortnummeret
     * @param cardLastFour Siste fire siffer av kortnummeret
     */
    public void addCard(String cardFirstSix, String cardLastFour) {
        if (!validateCard(cardFirstSix, cardLastFour)) {
            throw new IllegalArgumentException("Invalid card");
        }

        cards.add(cardFirstSix + cardLastFour);
    }

    /**
     * Fjerner kort.
     *
     * @param cardFirstSix Første seks siffer av kortnummeret
     * @param cardLastFour Siste fire siffer av kortnummeret
     */
    public void removeCard(String cardFirstSix, String cardLastFour) {
        if (!Pattern.compile("\\d{6}").matcher(cardFirstSix).matches()
                || !Pattern.compile("\\d{4}").matcher(cardLastFour).matches()) {
            throw new IllegalArgumentException("Invalid card number");
        }

        else if (!cards.contains(cardFirstSix + cardLastFour)) {
            throw new IllegalArgumentException("Person does not have this card");
        }

        else if (cards.size() == 1) {
            throw new IllegalArgumentException("Cannot remove the only card this person has");
        }

        cards.remove("" + cardFirstSix + cardLastFour);
    }

    /**
     * Metoden .sort() vil sortere Person-objektene fra størst til minst pengesum
     * brukt.
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

        objPersons.add(new Person("Carl æøåÆØÅ Edward", "Storlien", "121212", "1212"));
        objPersons.add(new Person("Paul Andre Johanson Storlien Pappa", "Johanson", "131313", "1313"));
        objPersons.add(new Person("Annika", "Johansen", "131313", "1311"));

        // newObjPersons.add(new Person("Merete", "Storlien"));

        System.out.println(getObjPersons());

        // System.out.println("Valid? " + objPersons.get(0).validateCard("141414",
        // "1414"));
    }
}
