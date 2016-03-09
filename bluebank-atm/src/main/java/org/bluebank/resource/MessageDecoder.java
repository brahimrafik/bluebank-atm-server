package org.bluebank.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bluebank.atm.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder.Text;
import javax.websocket.EndpointConfig;
import java.io.IOException;

import static com.google.common.base.Throwables.propagate;

public class MessageDecoder implements Text<Message> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Message decode(String message) throws DecodeException {
        try {
            return objectMapper.readValue(message, Message.class);
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
