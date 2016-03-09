package org.bluebank.atm;


import org.bluebank.atm.authorization.model.Card;

public final class CardBuilder {
    private String cardNumber = "123456";

    private CardBuilder() {
    }

    public static CardBuilder aDefaultCard() {
        return new CardBuilder();
    }

    public static Card aCard() {
        return aDefaultCard().build();
    }

    public Card build() {
        return new Card(cardNumber);
    }

    public CardBuilder withCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }
}
