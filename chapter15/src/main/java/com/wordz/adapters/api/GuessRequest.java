package com.wordz.adapters.api;

import com.wordz.domain.Player;

public record GuessRequest(Player player, String guess) {
}
