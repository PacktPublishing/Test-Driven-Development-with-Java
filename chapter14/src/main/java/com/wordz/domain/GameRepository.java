package com.wordz.domain;

import java.util.Optional;

public interface GameRepository {
    void create(Game game);

    Optional<Game> fetchForPlayer(Player player);

    void update(Game game);
}
