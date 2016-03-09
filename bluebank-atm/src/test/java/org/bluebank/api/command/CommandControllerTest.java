package org.bluebank.api.command;

import org.bluebank.api.command.processor.CommandController;
import org.junit.Before;
import org.junit.Test;

import static org.bluebank.AtmModule.ControllerConfiguration;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandControllerTest {

    private CommandController commandController;
    private ControllerConfiguration configuration;

    @Before
    public void before() {
        configuration = mock(ControllerConfiguration.class);
        when(configuration.getCapacity()).thenReturn(20);
    }

    @Test
    public void should_process_command() {
        // given
        Command command = mock(Command.class);
        commandController = new CommandController(configuration);
        commandController.process(command);

        // when
        commandController.run();

        // then
        verify(command).execute();
    }

}
