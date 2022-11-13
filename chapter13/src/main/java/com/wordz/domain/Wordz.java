package com.wordz.domain;

public class Wordz {
    private final GameRepository gameRepository;
    private final WordSelection selection ;

    public Wordz(GameRepository repository, WordRepository wordRepository, RandomNumbers randomNumbers) {
        this.gameRepository = repository;
        this.selection = new WordSelection(wordRepository, randomNumbers);
    }

    public void newGame(Player player) {
        var word = selection.chooseRandomWord();
        Game game = new Game(player, word, 0, false);
        gameRepository.create(game);
    }

    public GuessResult assess(Player player, String guess) {
        Game game = gameRepository.fetchForPlayer(player);

        if(game.isGameOver()) {
            return GuessResult.ERROR;
        }

        Score score = game.attempt( guess );

        gameRepository.update(game);
        return new GuessResult(score, game.isGameOver(), false);
    }
}
