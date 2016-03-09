package org.bluebank.atm.transaction.inbound;

import org.bluebank.api.command.processor.CommandProcessor;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.transaction.command.RequestWithdraw;
import org.bluebank.atm.transaction.command.factory.RequestWithdrawFactory;
import org.bluebank.contract.Messages.WithdrawRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_DOWN;
import static java.util.UUID.fromString;

@Singleton
public class WithdrawRequestReceiver  implements InboundEndPoint<WithdrawRequest> {
    private final RequestWithdrawFactory requestWithdrawFactory;
    private final CommandProcessor commandProcessor;

    @Inject
    public WithdrawRequestReceiver(RequestWithdrawFactory requestWithdrawFactory,
                                   CommandProcessor commandProcessor) {
        this.requestWithdrawFactory = requestWithdrawFactory;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void handle(WithdrawRequest withdrawRequest) {
        RequestWithdraw requestWithdraw = requestWithdrawFactory.build(
                fromString(withdrawRequest.getTransactionId()),
                BigDecimal.valueOf(withdrawRequest.getAmount()).setScale(2, ROUND_DOWN)
        );
        commandProcessor.process(requestWithdraw);
    }
}
