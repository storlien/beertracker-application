package com.carlst;

public class Main {
    public static void main(String[] args) {
        System.out.println(TokenGetter.getToken());
        TokenGetter.retrieveToken();
        System.out.println(TokenGetter.getToken());

        TokenGetter tg = new TokenGetter();
    }
}
