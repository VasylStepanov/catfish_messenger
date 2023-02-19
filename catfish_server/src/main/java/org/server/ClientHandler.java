package org.server;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * ClientHandler - a class to handle client
 * */
public class ClientHandler{

    private final static String USER_EXIT = "exit";

    public static void answerClient(ByteBuffer buffer, SelectionKey key) throws IOException, ReflectiveOperationException {
        SocketChannel client = (SocketChannel)key.channel();
        StringBuilder stringBuilder = new StringBuilder();

        int nBytes;
        while ((nBytes = client.read(buffer)) != -1) {
            stringBuilder.append(StandardCharsets.UTF_8.decode(buffer));
        }

        JSONObject jsonObject = new JSONObject(stringBuilder);
        if(jsonObject.getString("method").equals(USER_EXIT)){
            client.close();
        }

        jsonObject = callMethod(jsonObject);

        buffer.clear();
        byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        int range = 0;

        while (range * Server.getBufferSize() < bytes.length) {
            buffer.put(Arrays.copyOfRange(bytes, range * Server.getBufferSize(), (++range) * Server.getBufferSize()));
            client.write(buffer);
            buffer.clear();
        }

    }

    private static JSONObject callMethod(JSONObject jsonObject) throws ReflectiveOperationException {
        Class clazz = Class.forName("org.server.service" + jsonObject.getString("class"));
        Method method = clazz.getMethod(jsonObject.getString("method"));
        return (JSONObject)method.invoke(jsonObject);
    }


}
