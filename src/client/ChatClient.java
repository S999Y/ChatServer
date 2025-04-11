package client;

import java.io.*;
import java.net.*;
import java.util.*;
import visual.Visual;

public class ChatClient {
    private String clientName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatFrame chatFrame;
    private Visual decryption;

    public ChatClient(String name, Socket socket) throws IOException {
        this.clientName = name;
        this.socket = socket;
        this.decryption = new Visual("Decryption");
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(clientName);
    }

    public void setChatFrame(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
    }

    public String getClientName() {
        return clientName;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("USERLIST:")) {
                    String usersString = message.substring(9);
                    List<String> users = Arrays.asList(usersString.split(","));
                    chatFrame.updateUserList(users);
                } else {
                    // This is a received message (not sent by this client)
                    String[] msg = message.split(":", 2);
                    if (msg.length > 1) {
                        // decryption process
                        System.out.println(msg[1].trim());
                        message = decryption.decrypt(msg[1].trim());
                        message = msg[0] + " : " + message;
                    } else {
                        System.out.println(msg[0]);
                    }
                    chatFrame.appendMessage(message, false);
                }
            }
        } catch (IOException e) {
            System.err.println("Error listening for messages: " + e.getMessage());
        }
    }
}