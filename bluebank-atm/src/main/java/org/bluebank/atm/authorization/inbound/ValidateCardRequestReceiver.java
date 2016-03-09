package org.bluebank.atm.authorization.inbound;


import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.authorization.command.RequestCardValidation;
import org.bluebank.atm.authorization.command.factory.RequestCardValidationFactory;
import org.bluebank.atm.authorization.model.AtmInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.UUID.fromString;
import static org.bluebank.contract.Messages.ValidateCardRequest;

@Singleton
public class ValidateCardRequestReceiver implements InboundEndPoint<ValidateCardRequest> {

    private final RequestCardValidationFactory requestCardValidationFactory;
    private final CommandProcessor commandProcessor;

    @Inject
    public ValidateCardRequestReceiver(RequestCardValidationFactory requestCardValidationFactory,
                                       CommandProcessor commandProcessor) {
        this.requestCardValidationFactory = requestCardValidationFactory;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void handle(ValidateCardRequest validateCardRequest) {

        RequestCardValidation requestCardValidation = requestCardValidationFactory.build(
                fromString(validateCardRequest.getTransactionId()),
                validateCardRequest.getCardNumber(),
                new AtmInfo(validateCardRequest.getId(),
                        validateCardRequest.getLocation(),
                        validateCardRequest.getBankName()
                )
        );
        commandProcessor.process(requestCardValidation);
    }
}
