package org.bluebank.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bluebank.atm.Message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder.Text;
import javax.websocket.EndpointConfig;

import static com.google.common.base.Throwables.propagate;

public class MessageEncoder implements Text<Message> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(Message message) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw propagate(e);
        }
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
