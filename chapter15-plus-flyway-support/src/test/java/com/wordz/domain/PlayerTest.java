package com.wordz.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    @Test
    void twoAreEqual() {
        Player a = new Player("same-name-means-equal");
        Player b = new Player("same-name-means-equal");

        assertThat(a).isEqualTo(b);
    }

    @Test
    void twoAreNotEqual() {
        Player a = new Player("same-name-means-equal");
        Player b = new Player("different-name-means-not-equal");

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void equalHaveSameHashcode() {
        Player a = new Player("same-name-means-equal");
        Player b = new Player("same-name-means-equal");

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}