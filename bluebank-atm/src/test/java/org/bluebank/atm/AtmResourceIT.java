package org.bluebank.atm;

import org.bluebank.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.websocket.Session;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.UUID;

import static com.jayway.awaitility.Awaitility.await;
import static java.lang.String.format;
import static org.bluebank.atm.AtmResourceClient.close;
import static org.bluebank.atm.AtmResourceClient.connect;
import static org.bluebank.atm.AtmResourceClient.messages;
import static org.bluebank.contract.Messages.DepositRequest;
import static org.bluebank.contract.Messages.ValidateCardRequest;
import static org.bluebank.contract.Messages.ValidatePinRequest;

public class AtmResourceIT {

    private Application server;
    private Session session;

    @Before
    public void setUp() throws Exception {
        server = new Application();
        int port = findOpenPort();
        server.start(port);
        URI uri = new URI(format("ws://localhost:%s/events", port));
        session = connect(uri);
    }

    @After
    public void shutdown() throws Exception {
        server.stop();
        session.close();
        close();
    }

    @Test
    public void should_make_deposit() throws Exception {

        UUID transactionId = new UUID(0, 1);

        ValidateCardRequest validateCardRequest = ValidateCardRequest.newBuilder()
                .setTransactionId(transactionId.toString())
                .setCardNumber("5555444433331111")
                .build();
        Message message = new Message(Message.EventType.VALIDATE_CARD_REQUEST, validateCardRequest.toByteArray());

        session.getBasicRemote().sendObject(message);
        await().until(() -> {
            return messages.containsKey(Message.EventType.CARD_READ_CONFIRMATION);
        });

        ValidatePinRequest validatePinRequest = ValidatePinRequest.newBuilder()
                .setTransactionId(transactionId.toString())
                .setPin("1234")
                .build();

        Message validatePinMessage = new Message(Message.EventType.VALIDATE_PIN_REQUEST,validatePinRequest.toByteArray() );

        session.getBasicRemote().sendObject(validatePinMessage);
        await().until(() -> {
            return messages.containsKey(Message.EventType.CARD_VALIDATION_STATUS);
        });

        DepositRequest depositRequest = DepositRequest.newBuilder()
                .setTransactionId(transactionId.toString())
                .setAmount(200)
                .build();
        Message depositMessage = new Message(Message.EventType.DEPOSIT_REQUEST, depositRequest.toByteArray());

        session.getBasicRemote().sendObject(depositMessage);
        await().until(() -> {
            return messages.containsKey(Message.EventType.RECEIPT);
        });
    }

    private int findOpenPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
