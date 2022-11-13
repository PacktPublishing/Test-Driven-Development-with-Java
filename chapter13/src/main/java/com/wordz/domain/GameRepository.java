package com.wordz.domain;

public interface GameRepository {
    void create(Game game);

    Game fetchForPlayer(Player player);

    void update(Game game);
}
