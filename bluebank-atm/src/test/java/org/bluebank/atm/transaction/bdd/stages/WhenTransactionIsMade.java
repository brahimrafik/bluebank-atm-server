package org.bluebank.atm.transaction.bdd.stages;


import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.bdd.BddComponent;

import static org.bluebank.contract.Messages.DepositRequest;
import static org.bluebank.contract.Messages.InquiryRequest;
import static org.bluebank.contract.Messages.ValidateCardRequest;
import static org.bluebank.contract.Messages.WithdrawRequest;

public class WhenTransactionIsMade<SELF extends WhenTransactionIsMade<?>> extends Stage<SELF> {

    @ExpectedScenarioState
    private BddComponent bddComponent;

    @ExpectedScenarioState
    private ValidateCardRequest validateCardRequest;
    private InboundEndPoint<DepositRequest> depositReceiver;
    private InboundEndPoint<WithdrawRequest> withdrawReceiver;
    private InboundEndPoint<InquiryRequest> inquiryReceiver;

    @BeforeStage
    public void before() {
        depositReceiver = bddComponent.getDepositRequestReceiver();
        inquiryReceiver = bddComponent.getInquiryRequestReceiver();
        withdrawReceiver = bddComponent.getWithdrawRequestReceiver();
    }

    public SELF a_deposit_of_$_dollars_is_made(String amount) {
        DepositRequest depositRequest = DepositRequest.newBuilder()
                .setTransactionId(validateCardRequest.getTransactionId())
                .setAmount(Double.valueOf(amount))
                .build();
        depositReceiver.handle(depositRequest);
        return self();
    }

    public SELF a_withdraw_of_$_dollars_is_made(String amount) {
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                .setTransactionId(validateCardRequest.getTransactionId())
                .setAmount(Double.valueOf(amount))
                .build();
        withdrawReceiver.handle(withdrawRequest);
        return self();
    }

    public SELF a_balance_inquiry_is_made() {
        InquiryRequest inquiryRequest = InquiryRequest.newBuilder()
                .setTransactionId(validateCardRequest.getTransactionId())
                .build();
        inquiryReceiver.handle(inquiryRequest);
        return self();
    }
}
