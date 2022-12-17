package examples;

import examples.RandomNumbers;

public class StubRandomNumbers implements RandomNumbers {

    @Override
    public int nextInt(int upperBoundExclusive) {
        return 4;  // @see https://xkcd.com/221
    }
}
