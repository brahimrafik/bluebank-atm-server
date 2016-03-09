package org.bluebank.atm;

public class Message {
    public final EventType event;
    public final byte[] data;

    protected Message(){
        this(null,null);
    }

    public Message(EventType event, byte[] data) {
        this.event = event;
        this.data = data;
    }

    public enum EventType {
        UNKNOWN,
        VALIDATE_CARD_REQUEST,
        VALIDATE_PIN_REQUEST,
        DEPOSIT_REQUEST,
        WITHDRAW_REQUEST,
        INQUIRY_REQUEST,
        CARD_READ_CONFIRMATION,
        CARD_VALIDATION_STATUS,
        RECEIPT
    }
}
