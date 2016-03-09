package org.bluebank.resource;


import com.google.protobuf.InvalidProtocolBufferException;
import org.bluebank.api.endpoint.InboundEndPoint;
import org.bluebank.atm.Message;
import org.bluebank.contract.Messages.DepositRequest;
import org.bluebank.contract.Messages.InquiryRequest;
import org.bluebank.contract.Messages.ValidateCardRequest;
import org.bluebank.contract.Messages.ValidatePinRequest;
import org.bluebank.contract.Messages.WithdrawRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Throwables.propagate;
import static java.util.Collections.synchronizedSet;


@Singleton
@ServerEndpoint(value = "/events", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class AtmResource {

    private static final Set<Session> sessions = synchronizedSet(new HashSet<>());
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final InboundEndPoint<ValidateCardRequest> validateCardRequestReceiver;
    private final InboundEndPoint<ValidatePinRequest> validatePinRequestReceiver;
    private final InboundEndPoint<DepositRequest> depositRequestReceiver;
    private final InboundEndPoint<WithdrawRequest> withdrawRequestReceiver;
    private final InboundEndPoint<InquiryRequest> inquiryRequestReceiver;

    @Inject
    public AtmResource(@Named("validateCardRequestReceiver") InboundEndPoint<ValidateCardRequest> validateCardRequestReceiver,
                       @Named("validatePinRequestReceiver") InboundEndPoint<ValidatePinRequest> validatePinRequestReceiver,
                       @Named("depositRequestReceiver") InboundEndPoint<DepositRequest> depositRequestReceiver,
                       @Named("withdrawRequestReceiver") InboundEndPoint<WithdrawRequest> withdrawRequestReceiver,
                       @Named("inquiryRequestReceiver") InboundEndPoint<InquiryRequest> inquiryRequestReceiver) {
        this.validateCardRequestReceiver = validateCardRequestReceiver;
        this.validatePinRequestReceiver = validatePinRequestReceiver;
        this.depositRequestReceiver = depositRequestReceiver;
        this.withdrawRequestReceiver = withdrawRequestReceiver;
        this.inquiryRequestReceiver = inquiryRequestReceiver;
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Session ID : {} - Connection opened", session.getId());
        sessions.add(session);
    }

    public void send(Message message) {
        sessions.stream().forEach(session ->
                session.getAsyncRemote().sendObject(message));
    }

    @OnMessage
    public void onMessage(Message message) {
        final byte[] data = message.data;
        try {
            switch (message.event) {
                case VALIDATE_CARD_REQUEST:
                    final ValidateCardRequest validateCardRequest = ValidateCardRequest.parseFrom(data);
                    logger.info("Received message : {} ", validateCardRequest);
                    validateCardRequestReceiver.handle(validateCardRequest);
                    break;
                case VALIDATE_PIN_REQUEST:
                    final ValidatePinRequest validatePinRequest = ValidatePinRequest.parseFrom(data);
                    logger.info("Received message : {} ", validatePinRequest);
                    validatePinRequestReceiver.handle(validatePinRequest);
                    break;
                case DEPOSIT_REQUEST:
                    final DepositRequest depositRequest = DepositRequest.parseFrom(data);
                    logger.info("Received message : {} ", depositRequest);
                    depositRequestReceiver.handle(depositRequest);
                    break;
                case WITHDRAW_REQUEST:
                    final WithdrawRequest withdrawRequest = WithdrawRequest.parseFrom(data);
                    logger.info("Received message : {} ", withdrawRequest);
                    withdrawRequestReceiver.handle(withdrawRequest);
                    break;
                case INQUIRY_REQUEST:
                    final InquiryRequest inquiryRequest = InquiryRequest.parseFrom(data);
                    logger.info("Received message : {} ", inquiryRequest);
                    inquiryRequestReceiver.handle(inquiryRequest);
                    break;
                default:
                    logger.info("Discard unknown {} message type", message.event);
            }
        } catch (InvalidProtocolBufferException e) {
            propagate(e);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessions.remove(session);
        logger.info("Session ID : {} - Connection closed with status code {} and reason {}",
                session.getId(),
                closeReason.getCloseCode(),
                closeReason.getReasonPhrase()
        );
    }

    @OnError
    public void onError(Session session, Throwable cause) {
        sessions.remove(session);
        logger.info("Session ID : {} - Connection error : {}", session.getId(), cause);
    }
}
