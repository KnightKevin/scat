package com.simon.scat.connector.http;


import com.simon.scat.ServletProcessor;
import com.simon.scat.StaticResourceProcessor;

import java.io.OutputStream;
import java.net.Socket;

public class HttpProcessor {

    private final HttpConnector connector;
    private HttpRequest request;
    private HttpResponse response;

    public HttpProcessor(HttpConnector connector) {
        this.connector = connector;
    }

    public void process(Socket socket) {

        SocketInputStream input;
        OutputStream output;

        try {
            input = new SocketInputStream(socket.getInputStream(), 2048);
            output = socket.getOutputStream();

            // create HttpRequest Object and parse
            request = new HttpRequest(input);

            // create HttpResponse Object
            response = new HttpResponse(output);

            response.setRequest(request);

            response.setHeader("Server", "Scat Servlet Container");

            parseRequest(input, output);
            parseHeaders(input);

            // check if this is a request for a servlet or a static resource
            // a request for a servlet begins with "/servlet/"
            if (request.getRequestURI().startsWith("/servlet/")) {
                // hand servlet
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            } else {
                // hand static
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request, response);
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void parseRequest(SocketInputStream input, OutputStream output) {

    }

    void parseHeaders(SocketInputStream input) {

    }
}
