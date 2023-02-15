package org.server;

import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * ClientHandler - a class to handle client
 * */
public class ClientHandler implements Runnable{

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"))) {
            JSONObject jsonObject = new JSONObject(bufferedReader);
            jsonObject = callMethod(jsonObject);
            bufferedWriter.write(jsonObject.toString());
            bufferedWriter.flush();
        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject callMethod(JSONObject jsonObject) throws ReflectiveOperationException {
        Class clazz = Class.forName("org.server.service" + jsonObject.getString("class"));
        Method method = clazz.getMethod(jsonObject.getString("method"));
        return (JSONObject)method.invoke(jsonObject);
    }


}
