package org.bluebank.atm.authorization.inbound;

import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.authorization.command.HandlePinValidationResponse;
import org.bluebank.atm.authorization.command.factory.HandlePinValidationResponseFactory;
import org.bluebank.banking.authorization.outbound.PinValidationResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PinValidationResponseReceiver implements InboundEndPoint<PinValidationResponse> {
    private final CommandProcessor commandProcessor;
    private final HandlePinValidationResponseFactory handlePinValidationResponseFactory;

    @Inject
    public PinValidationResponseReceiver(CommandProcessor commandProcessor,
                                         HandlePinValidationResponseFactory handlePinValidationResponseFactory) {
        this.commandProcessor = commandProcessor;
        this.handlePinValidationResponseFactory = handlePinValidationResponseFactory;
    }

    @Override
    public void handle(PinValidationResponse pinValidationResponse) {
        final HandlePinValidationResponse handlePinValidationResponse = handlePinValidationResponseFactory.build(
                pinValidationResponse.id,
                pinValidationResponse.validationStatus
        );
        commandProcessor.process(handlePinValidationResponse);
    }
}
