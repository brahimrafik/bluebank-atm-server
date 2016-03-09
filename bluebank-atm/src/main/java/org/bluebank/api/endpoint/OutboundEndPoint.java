package org.bluebank.api.endpoint;

public interface OutboundEndPoint<T> {

    void send(T message);
}
