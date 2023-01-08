package com.wordz.domain;

import java.util.Optional;

public class Wordz {
    private final GameRepository gameRepository;
    private final WordSelection selection ;

    public Wordz(GameRepository repository, WordRepository wordRepository, RandomNumbers randomNumbers) {
        this.gameRepository = repository;
        this.selection = new WordSelection(wordRepository, randomNumbers);
    }

    public boolean newGame(Player player) {
        Optional<Game> currentGame = gameRepository.fetchForPlayer(player);

        if (isGameInProgress(currentGame)) {
            return false ;
        }

        var word = selection.chooseRandomWord();
        Game game = new Game(player, word, 0, false);
        gameRepository.create(game);
        return true;
    }

    public GuessResult assess(Player player, String guess) {
        Optional<Game> currentGame = gameRepository.fetchForPlayer(player);

        if (!isGameInProgress(currentGame)) {
            return GuessResult.ERROR;
        }

        return calculateScore(currentGame.get(), guess);
    }

    private GuessResult calculateScore(Game game, String guess) {
        Score score = game.attempt( guess );

        gameRepository.update(game);
        return GuessResult.create(score, game.isGameOver());
    }

    private boolean isGameInProgress(Optional<Game> currentGame) {
        if (currentGame.isEmpty()) {
            return false ;
        }

        return !currentGame.get().isGameOver();
    }
}