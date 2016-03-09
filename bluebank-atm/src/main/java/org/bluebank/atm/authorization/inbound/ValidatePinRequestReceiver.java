package org.bluebank.atm.authorization.inbound;

import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.authorization.command.RequestPinValidation;
import org.bluebank.atm.authorization.command.factory.RequestPinValidationFactory;
import org.bluebank.contract.Messages.ValidatePinRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.UUID.fromString;

@Singleton
public class ValidatePinRequestReceiver implements InboundEndPoint<ValidatePinRequest> {
    private final RequestPinValidationFactory requestPinValidationFactory;
    private final CommandProcessor commandProcessor;

    @Inject
    public ValidatePinRequestReceiver(RequestPinValidationFactory requestPinValidationFactory,
                                      CommandProcessor commandProcessor) {
        this.requestPinValidationFactory = requestPinValidationFactory;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void handle(ValidatePinRequest validationRequest) {
        RequestPinValidation requestPinValidation = requestPinValidationFactory.build(
                fromString(validationRequest.getTransactionId()),
                validationRequest.getPin()
        );
        commandProcessor.process(requestPinValidation);
    }
}
