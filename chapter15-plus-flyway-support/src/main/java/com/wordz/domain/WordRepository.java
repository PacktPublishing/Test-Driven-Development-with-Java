package com.wordz.domain;

public interface WordRepository {
    String fetchWordByNumber(int number);

    int highestWordNumber();
}
