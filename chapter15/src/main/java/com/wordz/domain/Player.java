package com.wordz.domain;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.*;

public class Player {
    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }
}
