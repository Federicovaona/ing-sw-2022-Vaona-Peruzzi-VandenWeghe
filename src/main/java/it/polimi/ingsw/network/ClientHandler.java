package it.polimi.ingsw.network;

import it.polimi.ingsw.message.GeneralMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements ClientHandlerInterface /** Runnable */ {
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Lobby lobby;
    private LobbyServer lobbyServer;
    private String gameId;
    private final Object lockSendMessage;
    //private final Object lockHandleMessage;

    /** costruttore di default */
    /**
     * @param client la socket client a cui vengono inviati i messaggi
     * @param lobbyServer la lobby server di default
     */
    public ClientHandler(Socket client, LobbyServer lobbyServer) {
        this.client = client;
        this.lobbyServer=lobbyServer;
        //lockHandleMessage = new Object();
        lockSendMessage=new Object();
        lobby=null;

        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** disconnete la socket dal server */
    public void disconnect() {
        try {
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.currentThread().interrupt();
        lobbyServer.leaveLobby(gameId, this);
    }

    /** invia messaggio al client */
    public void sendMessage(GeneralMessage message)
    {
        try{
            synchronized (lockSendMessage) {
                output.writeObject((Object) message);
                output.reset();
            }
        }
        catch(IOException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            disconnect();
        }

    }







}