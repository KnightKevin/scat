package com.simon.scat.ex02;

import java.net.ServerSocket;

public class HttpServer1 {
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private final boolean shutdown = false;

    public void main(String[] args) {
        HttpServer1 server = new HttpServer1();
        server.await();
    }

    public void await() {
        // todo simon
    }
}
