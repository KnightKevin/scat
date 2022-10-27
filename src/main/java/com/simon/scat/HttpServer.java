package com.simon.scat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static final String WEB_ROOT = System.getProperty("usr.dir") + File.separator + "webroot";

    /**
     * the shutdown command received
     * */
    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();

        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // loop waiting for a request
        while(!shutdown) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                // create Request object and parse;
                Request request = new Request(input);


                // todo simon

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
