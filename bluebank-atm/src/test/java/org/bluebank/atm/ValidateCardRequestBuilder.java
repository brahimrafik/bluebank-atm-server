package org.bluebank.atm;

import java.util.UUID;

import static org.bluebank.contract.Messages.ValidateCardRequest;

public final class ValidateCardRequestBuilder {
    private String id = "47";
    private String location = "100 Main Street, NewYork 12345,USA";
    private String bankName = "Blue Bank International";
    private String cardNumber = "123456";
    private String transactionID = new UUID(0, 1).toString();

    private ValidateCardRequestBuilder() {
    }

    public static ValidateCardRequestBuilder aDefaultValidateCardRequest() {
        return new ValidateCardRequestBuilder();
    }

    public static ValidateCardRequest aValidateCardRequest() {
        return aDefaultValidateCardRequest().build();
    }

    public ValidateCardRequest build() {
        return ValidateCardRequest.newBuilder()
                .setId(id)
                .setLocation(location)
                .setBankName(bankName)
                .setCardNumber(cardNumber)
                .setTransactionId(transactionID)
                .build();
    }

    public ValidateCardRequestBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ValidateCardRequestBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public ValidateCardRequestBuilder withBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public ValidateCardRequestBuilder withCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public ValidateCardRequestBuilder withTransactionID(String transactionID) {
        this.transactionID = transactionID;
        return this;
    }

}
