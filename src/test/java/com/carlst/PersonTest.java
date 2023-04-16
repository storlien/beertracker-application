package com.carlst;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class PersonTest {

    private Person p;

    @BeforeEach
    public void setUp() {
        p = new Person("Test", "Kanin");
        Person.getObjPersons().add(p);
    }

    @AfterEach
    public void tearDown() {
        Person.setObjPersons(new ArrayList<>());
    }
    
    @Test
    public void testConstructor() {
        new Person("Fanto", "Rangen");

        assertThrows(IllegalArgumentException.class, () -> {
            new Person("Agent", "007");
        });

        new Person("KL", "Amydia", "123456", "1234");

        assertThrows(IllegalArgumentException.class, () -> {
            new Person("Eddy", "Størlien", "12345", "1234");
        });
    }

    @Test
    public void testValidateCard() {
        assertEquals(true, p.validateCard("123456", "1234"));
        assertEquals(false, p.validateCard("12345", "1234"));
        assertEquals(false, p.validateCard("1to3456", "1234"));
    }

    @Test
    public void testValidateAmount() {
        assertEquals(true, p.validateAmount(10.5));
        assertEquals(false, p.validateAmount(-10.5));
    }

    @Test
    public void testValidateName() {
        assertEquals(true, p.validateName("Dette går", "fint"));
        assertEquals(false, p.validateName("Edw4rd", "Storlien"));
        assertEquals(false, p.validateName("Jeg er ", "rå!!!"));
        assertEquals(false, p.validateName("Test", "Kanin"));
    }
}