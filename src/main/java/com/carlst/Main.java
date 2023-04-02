package com.carlst;

public class Main {
    public static void main(String[] args) {
        System.out.println(TokenGetter.getToken());
        TokenGetter.retrieveToken();

        TokenGetter tg = new TokenGetter();
    }
}