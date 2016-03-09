package org.bluebank.atm;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bluebank.atm.Message.EventType;
import org.bluebank.resource.MessageDecoder;
import org.bluebank.resource.MessageEncoder;
import org.eclipse.jetty.util.component.LifeCycle;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class AtmResourceClient {

    public static final ConcurrentMap<EventType, Message> messages = new ConcurrentHashMap<>();
    private static WebSocketContainer webSocketContainer;

    public static Session connect(URI uri) throws Exception {
        webSocketContainer = ContainerProvider.getWebSocketContainer();
        return webSocketContainer.connectToServer(AtmResourceClient.class, uri);
    }

    public static void close() throws Exception {
        if (webSocketContainer instanceof LifeCycle) {
            ((LifeCycle) webSocketContainer).stop();
        }
    }

    @OnMessage
    public void onMessage(Message message) throws InvalidProtocolBufferException {
        messages.put(message.event, message);
    }

}
