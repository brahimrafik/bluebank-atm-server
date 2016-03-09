package org.bluebank.banking.transaction.outbound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.banking.account.model.Account;
import org.bluebank.banking.transaction.model.Inquiry;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class InquiryPerformedHandler {
    private final InboundEndPoint<Inquiry> inquiryResponseReceiver;

    @Inject
    public InquiryPerformedHandler(EventBus eventBus,
                                   @Named("inquiryResponseReceiver") InboundEndPoint<Inquiry> inquiryResponseReceiver) {
        this.inquiryResponseReceiver = inquiryResponseReceiver;
        eventBus.register(this);
    }

    @Subscribe
    public void forwardInquiryPerformed(Account.InquiryPerformed inquiryPerformed) {
        Inquiry inquiry = new Inquiry(
                inquiryPerformed.atmTransaction,
                inquiryPerformed.balance,
                inquiryPerformed.cardNumber
        );
        inquiryResponseReceiver.handle(inquiry);
    }
}
