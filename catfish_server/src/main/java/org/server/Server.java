package org.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static org.server.ClientHandler.answerClient;

/**
 * Server - the core of application server.
 *      0. A construct with a setting server socket
 *      1. main -
 *      2. startServer -
 *      3. closeServer -
 *      4. getSession -
 * */

public class Server {

    private final static int BUFFER_SIZE = 256;
    private final static ServerSession session = new ServerSession();
    private final ServerSocketChannel serverSocket;
    private final Selector selector;
    private static Server server;


    Server(ServerSocketChannel serverSocket, Selector selector){
        this.serverSocket = serverSocket;
        this.selector = selector;
    }

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            Selector selector = Selector.open();
            serverSocket.bind(new InetSocketAddress("localhost", 8080));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            server = new Server(serverSocket, Selector.open());
            server.startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            server.closeServer();
        }
    }

    public void startServer(){
        Iterator<SelectionKey> iterator;
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        while (serverSocket.isOpen()){
            try {
                selector.select();
                iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if(selectionKey.isAcceptable())
                        register();
                    if(selectionKey.isReadable())
                        answerClient(byteBuffer, selectionKey);
                    iterator.remove();
                }
            } catch (IOException | ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void register() throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }



    private void closeServer(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ServerSession getSession(){
        return session;
    }

    public static int getBufferSize(){
        return BUFFER_SIZE;
    }
}
