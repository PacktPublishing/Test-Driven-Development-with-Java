package com.wordz;

import com.wordz.domain.RandomNumbers;

import java.util.Random;

class ProductionRandomNumbers implements RandomNumbers {
    private final Random random = new Random();

    @Override
    public int next(int upperBoundInclusive) {
        int upperBoundExclusive = upperBoundInclusive + 1;
        return random.nextInt(upperBoundExclusive);
    }
}
