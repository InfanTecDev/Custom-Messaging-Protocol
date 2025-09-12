package com.CustomMessagingProtocol.Network.Protocol;

import java.util.Map;

public record ProtocolMessage(MessageType messageType, Map<String, String> headers, String body) {

    public String getIdUser() {
        return headers.get("Id");
    }

    public String getUsername() {
        return headers.get("Username");
    }

    public String getTo() {
        return headers.get("To");
    }

    public String getFrom() {
        return headers.get("From");
    }
}