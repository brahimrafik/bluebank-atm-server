package org.bluebank.api.command.service;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.bluebank.api.command.processor.CommandController;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandControllerService extends AbstractExecutionThreadService {
    private final CommandController commandController;

    @Inject
    public CommandControllerService(final CommandController commandController) {
        this.commandController = commandController;
    }

    @Override
    protected void run() {
        while (isRunning()) {
            commandController.run();
        }
    }
}

