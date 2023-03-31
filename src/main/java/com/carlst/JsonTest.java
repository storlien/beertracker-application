package com.carlst;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {
    private String name;
    private int age;
    private List<String> hobbies;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonTest edward = objectMapper.readValue(new File("src/main/resources/json/JsonTestObj.json"),
                JsonTest.class);

        System.out.println(edward.getName());
        System.out.println(edward.getAge());
        System.out.println(edward.getHobbies());

        JsonTest pappa = new JsonTest();
        pappa.setName("Paul");
        pappa.setAge(54);
        pappa.setHobbies(new ArrayList<>(Arrays.asList("Smarthus", "Seiling", "Hyttekos")));

        objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("src/main/resources/json/pappa.json"), pappa);

    }
}
