package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private int port;
    private Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler newClient = new ClientHandler(socket, this);
                clients.add(newClient);
                new Thread(newClient).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void broadcast(String message, ClientHandler excludeClient) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != excludeClient) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public void broadcastUserList() {
        StringBuilder userList = new StringBuilder("USERLIST:");
        synchronized (clients) {
            for (ClientHandler client : clients) {
                userList.append(client.getClientName()).append(",");
            }
        }
        String userListStr = userList.toString();
        for (ClientHandler client : clients) {
            client.sendMessage(userListStr);
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcast(client.getClientName() + " has left the chat", null);
        broadcastUserList();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public String getClientName() {
        return clientName;
    }

    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            clientName = in.readLine();
            server.broadcast(clientName + " has joined the chat", this);
            server.broadcastUserList();

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                server.broadcast(clientName + ": " + clientMessage, this);
            }
        } catch (IOException ex) {
            System.out.println("Error in ClientHandler: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error closing socket: " + ex.getMessage());
            }
            server.removeClient(this);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}