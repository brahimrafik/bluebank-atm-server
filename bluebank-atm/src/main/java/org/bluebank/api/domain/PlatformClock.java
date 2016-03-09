package org.bluebank.api.domain;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Clock;
import java.time.LocalDateTime;

import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;

@Singleton
public class PlatformClock {
    private final Clock clock;

    @Inject
    public PlatformClock() {
        clock = systemUTC();
    }

    public LocalDateTime getDate() {
        return now(clock);
    }
}
