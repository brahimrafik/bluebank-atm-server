package org.bluebank.api.endpoint;

public interface InboundEndPoint<T> {

    void handle(T t);
}
