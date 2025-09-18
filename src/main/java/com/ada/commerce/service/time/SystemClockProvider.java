package com.ada.commerce.service.time;

import java.time.Clock;
import java.time.Instant;

public final class SystemClockProvider implements ClockProvider {
  private final Clock clock = Clock.systemUTC();
  @Override public Instant now() { return Instant.now(clock); }
}

