package com.wordz.domain;

import java.util.Optional;

public class Wordz {
    private final GameRepository gameRepository;
    private final WordSelection wordSelection ;

    public Wordz(GameRepository gr,
                 WordRepository wr,
                 RandomNumbers rn) {
        this.gameRepository = gr;
        this.wordSelection = new WordSelection(wr, rn);
    }

    public void newGame(Player player) {
        var word = wordSelection.chooseRandomWord();
        gameRepository.create(Game.create(player, word));
    }

    public GuessResult assess(Player player, String guess) {
        Game game = gameRepository.fetchForPlayer(player);

        if(game.isGameOver()) {
            return GuessResult.ERROR;
        }

        Score score = game.attempt( guess );

        gameRepository.update(game);

        return GuessResult.create(score, game.isGameOver());
    }
}
