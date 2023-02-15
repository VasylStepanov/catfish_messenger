package org.server;

import org.server.dao.UserDao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server - the core of application server.
 *      0. A construct with a setting server socket
 *      1. main -
 *      2. startServer -
 *      3. closeServer -
 *      4. getSession -
 * */

public class Server {

    private final static ServerSession session = new ServerSession();
    private final ServerSocket serverSocket;
    private static Server server;


    Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;

    }

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            server.closeServer();
        }
    }

    public void startServer(){
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        while (!serverSocket.isClosed()){
            try(Socket socket = serverSocket.accept()) {
                ClientHandler clientHandler = new ClientHandler(socket);
                executorService.submit(clientHandler);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                executorService.shutdown();
            }
        }
    }

    public void closeServer(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ServerSession getSession(){
        return session;
    }
}
