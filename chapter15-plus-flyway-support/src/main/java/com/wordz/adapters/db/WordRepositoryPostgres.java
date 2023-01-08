package com.wordz.adapters.db;

import com.wordz.domain.WordRepository;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;

public class WordRepositoryPostgres implements WordRepository {
    private static final String SQL_FETCH_WORD_BY_NUMBER
            = "select word from word where word_number=:wordNumber";
    private static final String SQL_RETURN_HIGHEST_WORD_NUMBER
            = "select max(word_number) from word";
    private final Jdbi jdbi;

    public WordRepositoryPostgres(DataSource dataSource) {
        jdbi = Jdbi.create(dataSource);
    }

    @Override
    public String fetchWordByNumber(int wordNumber) {
        String word = jdbi.withHandle(handle -> {
            var query = handle.createQuery(SQL_FETCH_WORD_BY_NUMBER);
            query.bind("wordNumber", wordNumber);

            return query
                    .mapTo(String.class)
                    .one();
        });

        return word ;
    }

    @Override
    public int highestWordNumber() {
        return jdbi.withHandle(handle->
                handle.createQuery(SQL_RETURN_HIGHEST_WORD_NUMBER)
                .mapTo(Integer.class)
                .one());
    }
}
