package com.wordz.domain;

import java.util.List;

public record GuessResult(
        Score score,
        boolean isGameOver,
        boolean isError
) {
    public static final GuessResult ERROR = new GuessResult(null, true, true);
}
