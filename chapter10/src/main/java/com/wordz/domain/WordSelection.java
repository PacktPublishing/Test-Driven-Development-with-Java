package com.wordz.domain;

public class WordSelection {
    private final WordRepository repository;
    private final RandomNumbers random;

    public WordSelection(WordRepository repository, RandomNumbers random) {
        this.repository = repository;
        this.random = random;
    }

    public String chooseRandomWord() {
        int wordNumber = random.next(repository.highestWordNumber());

        return repository.fetchWordByNumber(wordNumber);
    }
}
