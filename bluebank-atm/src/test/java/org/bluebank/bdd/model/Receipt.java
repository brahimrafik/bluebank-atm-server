package org.bluebank.bdd.model;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Receipt {
    public final String description;

    public Receipt(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Receipt that = (Receipt) obj;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(description);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("description", description)
                .toString();
    }
}
