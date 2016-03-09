package org.bluebank.api.domain;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Singleton
public class IdGenerator {

    @Inject
    public IdGenerator() {
    }

    public UUID generate() {
        return randomUUID();
    }
}
