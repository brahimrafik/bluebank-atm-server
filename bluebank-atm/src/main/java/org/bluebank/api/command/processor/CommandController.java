package org.bluebank.api.command.processor;

import org.bluebank.AtmModule.ControllerConfiguration;
import org.bluebank.api.command.Command;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.google.common.base.Throwables.propagate;
import static java.util.concurrent.TimeUnit.SECONDS;

@Singleton
public class CommandController implements CommandProcessor {
    private final int timeout;
    private final BlockingQueue<Command> commands;

    @Inject
    public CommandController(ControllerConfiguration configuration) {
        commands = new ArrayBlockingQueue<>(configuration.getCapacity());
        timeout = configuration.getTimeout();
    }

    @Override
    public boolean process(final Command command) {
        boolean success;
        try {
            success = commands.offer(command, timeout, SECONDS);
        } catch (InterruptedException e) {
            throw propagate(e);
        }
        return success;
    }

    public void run() {
        try {
            Command command = commands.poll(timeout, SECONDS);
            if (command == null) {
                return;
            }
            command.execute();
        } catch (InterruptedException e) {
            throw propagate(e);
        }
    }
}
