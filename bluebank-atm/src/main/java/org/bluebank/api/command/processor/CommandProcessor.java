package org.bluebank.api.command.processor;

import org.bluebank.api.command.Command;

public interface CommandProcessor {

    boolean process(Command command);
}
