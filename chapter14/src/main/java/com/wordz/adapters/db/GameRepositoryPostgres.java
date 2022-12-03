package com.wordz.adapters.db;

import com.wordz.domain.Game;
import com.wordz.domain.GameRepository;
import com.wordz.domain.Player;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class GameRepositoryPostgres implements GameRepository {

    private static final String SQL_INSERT_NEW_GAME_ROW
            = "insert into game " +
            "(player_name, word, attempt_number, is_game_over) " +
            "values(:playerName,:word,:attemptNumber,:isGameOver)";
    private static final String SQL_FIND_GAME_FOR_PLAYER
            = "select player_name, word, attempt_number, is_game_over " +
            "from game where player_name=:playerName ";
    private static final String SQL_UPDATE_GAME_BY_PLAYER
            = "update game " +
            "set attempt_number = :attemptNumber, is_game_over = :isGameOver " +
            "where player_name = :playerName";
    private final Jdbi jdbi;

    public GameRepositoryPostgres(DataSource dataSource) {
        jdbi = Jdbi.create(dataSource);
    }

    @Override
    public void create(Game game) {
        jdbi.useHandle(handle ->
                handle.createUpdate(SQL_INSERT_NEW_GAME_ROW)
                        .bind("playerName", game.getPlayer().getName())
                        .bind("word", game.getWord())
                        .bind("attemptNumber", game.getAttemptNumber())
                        .bind("isGameOver", game.isGameOver())
                        .execute()
        );
    }

    /**
     * @param player - assumes player.name is unique in this implementation
     * One player can have one active game at any time
     */
    @Override
    public Optional<Game> fetchForPlayer(Player player) {
        return jdbi.withHandle(handle ->
                handle.createQuery(SQL_FIND_GAME_FOR_PLAYER)
                        .bind("playerName", player.getName())
                        .map((rs, ctx) -> mapToGame(rs))
                        .findOne()
        );
    }

    @Override
    public void update(Game game) {
        jdbi.useHandle(handle->
                handle.createUpdate(SQL_UPDATE_GAME_BY_PLAYER)
                .bind("playerName", game.getPlayer().getName())
                .bind("attemptNumber", game.getAttemptNumber())
                .bind("isGameOver", game.isGameOver())
                .execute()
        );
    }

    private Game mapToGame(ResultSet rs) throws SQLException {
        return new Game(new Player(rs.getString("player_name")),
                rs.getString("word"),
                rs.getInt("attempt_number"),
                rs.getBoolean("is_game_over"));
    }
}
