package com.wordz.domain;

import org.assertj.core.api.AbstractAssert;

public class GameAssert extends AbstractAssert<GameAssert, Game> {

    private GameAssert(Game actual) {
        super(actual, GameAssert.class);
    }

    public static GameAssert assertThat(Game actual) {
        return new GameAssert(actual);
    }

    public GameAssert hasWord(String word) {
        isNotNull();

        if (!actual.getWord().equals(word)) {
            failWithMessage("Expected game to have word %s but was %s",
                    word, actual.getWord());
        }

        return this;
    }

    public GameAssert hasAttemptNumber(int attemptNumber) {
        isNotNull();

        if (actual.getAttemptNumber() != attemptNumber) {
            failWithMessage("Expected game to have attemptNumber %d but was %d",
                    attemptNumber, actual.getAttemptNumber());
        }

        return this;
    }

    public GameAssert hasPlayer(Player player) {
        isNotNull();

        if (!actual.getPlayer().equals(player)) {
            failWithMessage("Expected game to have player %s but was %s",
                    player, actual.getPlayer());
        }

        return this;
    }

    public GameAssert hasGameOver(boolean isGameOver) {
        isNotNull();

        if (actual.isGameOver() != isGameOver) {
            failWithMessage("Expected game to have isGameOver %s but was %s",
                    isGameOver, actual.isGameOver());
        }

        return this;
    }

}
