package com.CustomMessagingProtocol.Network.Protocol;

import java.util.HashMap;
import java.util.Map;

public class ProtocolParser {
    //Type:LOGIN/Headers:Id-12345-Username-User1-To-User2-From-User1/Body:Este es un mensaje

    public static String format(String type, String id, String username, String to, String from, String body) {
        id = (id.isBlank()) ? "Empty" : id;
        username = (username.isBlank()) ? "Empty" : username;
        to = (to.isBlank()) ? "Empty" : to;
        from = (from.isBlank()) ? "Empty" : from;
        return "Type:" + type + "/Headers:Id-" + id + "-Username-" + username + "-To-" + to + "-From-" + from + "/Body:" + body;
    }

    public static ProtocolMessage toProtocolMessage(String message) {
        String[] elements = message.split("/");
        MessageType type = MessageType.valueOf(elements[0].split(":")[1]);
        String headersString = elements[1].split(":")[1];
        String[] headersArray = headersString.split("-");
        String id = headersArray[1];
        String username = headersArray[3];
        String to = headersArray[5];
        String from = headersArray[7];
        String body = elements[2].split(":")[1];
        Map<String, String> headers = new HashMap<>();
        headers.put("Id", (id.equals("Empty")) ? "" : id);
        headers.put("Username", (username.equals("Empty")) ? "" : username);
        headers.put("To", (to.equals("Empty")) ? "" : to);
        headers.put("From", (from.equals("Empty")) ? "" : from);
        return new ProtocolMessage(type, headers, body);
    }

    public static String serialize(ProtocolMessage message) {
        Map<String, String> headers = message.headers();
        String idTemp = headers.get("Id");
        String id = idTemp.isBlank() ? "Empty" : idTemp;
        String usernameTemp = headers.get("Username");
        String username = usernameTemp.isBlank() ? "Empty" : usernameTemp;
        String toTemp = headers.get("To");
        String to = toTemp.isBlank() ? "Empty" : toTemp;
        String fromTemp = headers.get("From");
        String from = fromTemp.isBlank() ? "Empty" : fromTemp;
        String body = message.body();
        return "Type:" + message.messageType() + "/Headers:Id-" + id + "-Username-" + username + "-To-" + to + "-From-" + from + "/Body:" + body;
    }

    public static boolean isLoginMessage(ProtocolMessage message) {
        return message.messageType().equals(MessageType.LOGIN);
    }

    public static boolean isLogoutMessage(ProtocolMessage message) {
        return message.messageType().equals(MessageType.LOGOUT);
    }

    public static boolean isPrivateMessage(ProtocolMessage message) {
        return message.messageType().equals(MessageType.PRIVATE_MESSAGE);
    }

    public static void main(String[] args) {
        ProtocolMessage message = ProtocolParser.toProtocolMessage(
                "Type:LOGIN/Headers:Id-12345-Username-User1-To-User2-From-User1/Body:Este es un mensaje");
                //ProtocolParser.format("LOGIN", "12345", "User1", "", "", "Hola, quiero entrar"));
                //"Type:LOGIN/Headers:Id-12345-Username-User1-To-User2-From-User1/Body:Este es un mensaje");

        System.out.println(ProtocolParser.serialize(message));
    }
}
