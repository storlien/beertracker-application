package com.carlst;

import com.google.common.collect.Multimap;

public class Main {
    public static void main(String[] args) {
        // System.out.println(TokenGetter.getToken());
        // TokenGetter.retrieveToken();

        // TokenGetter tg = new TokenGetter();

        Filehandler fh = new Filehandler();

        System.out.println("Person-objekter før fil er lest:");
        System.out.println(Person.objPersons + "\n");

        fh.readFile();
        System.out.println("Person-objekter etter fil er lest:");
        System.out.println(Person.objPersons + "\n");

        PurchaseMapper.resetAmountSpent();
        System.out.println("Person-objekter etter resatt sum:");
        System.out.println(Person.objPersons + "\n");

        Multimap<String, Double> purchaseList = PurchaseReader.getPurchasesFromDate("2022-08-01");

        PurchaseMapper.mapPurchases(purchaseList);

        System.out.println("Personer etter mapping av kjøp");
        System.out.println(Person.objPersons);

        fh.saveFile();
    }
}