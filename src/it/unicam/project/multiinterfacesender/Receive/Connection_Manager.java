package it.unicam.project.multiinterfacesender.Receive;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Connection_Manager implements Runnable {

    private static final int PORT = 3306;
    private static ServerSocket serverSocket = null;
    public static ArrayList<Connection> clients = new ArrayList<>();
    private boolean mRun = true;

    Connection_Manager() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("35.180.118.235:" + PORT + "\n\tWaiting for connections...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (mRun) {
            try {
                Socket socket = serverSocket.accept();
                Connection c = new Connection(socket,this);
                clients.add(c);
                new Thread(c).start();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
        }
    }

    void removeClient(Connection client) {
        int toRemove = clients.indexOf(client);
        System.out.println("client [" + client.connID + "] Disconnected");
        clients.remove(toRemove);
    }

    void sendMessageToClients(Message d, String dtoken) {
        for (Connection c : clients) {
            if (c.dtoken == dtoken) {
                c.SendMesage(d);
            }
        }
    }

    void Terminate() {
        mRun = false;
        for (Connection c : clients) {
            c.disconnect();
        }
    }

    Connection getConnectionByDToken(String dToken)
    {
        for (Connection client: clients) {
            if(client.dtoken.equals(dToken))
            {

                return client;
            }

        }
        return null;
    }

    Connection getControparteConnectionByDToken(String dToken)
    {
        for (Connection client: clients) {
            if(client.dtoken_controparte.equals(dToken))
            {
                return (client);
            }
        }
        return null;
    }


   /* private void appendLog(String msg, Color c) {
        //Main.AppendLog(msg, c);
    }*/
}