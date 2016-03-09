package org.bluebank.atm.transaction.inbound;

import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.transaction.command.RequestDeposit;
import org.bluebank.atm.transaction.command.factory.RequestDepositFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_DOWN;
import static java.util.UUID.fromString;
import static org.bluebank.contract.Messages.DepositRequest;

@Singleton
public class DepositRequestReceiver implements InboundEndPoint<DepositRequest> {
    private final RequestDepositFactory requestDepositFactory;
    private final CommandProcessor commandProcessor;

    @Inject
    public DepositRequestReceiver(RequestDepositFactory requestDepositFactory,
                                  CommandProcessor commandProcessor) {
        this.requestDepositFactory = requestDepositFactory;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void handle(DepositRequest depositRequest) {
        RequestDeposit requestDeposit = requestDepositFactory.build(
                fromString(depositRequest.getTransactionId()),
                BigDecimal.valueOf(depositRequest.getAmount()).setScale(2, ROUND_DOWN)
        );
        commandProcessor.process(requestDeposit);
    }
}
