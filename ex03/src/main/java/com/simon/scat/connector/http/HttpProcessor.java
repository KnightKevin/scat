package com.simon.scat.connector.http;


import com.simon.scat.ServletProcessor;
import com.simon.scat.StaticResourceProcessor;
import org.apache.catalina.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static com.simon.scat.connector.http.SocketInputStream.sm;

public class HttpProcessor {

    private final HttpConnector connector;
    private HttpRequest request;
    private HttpResponse response;

    private final HttpRequestLine requestLine = new  HttpRequestLine();

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

    void parseRequest(SocketInputStream input, OutputStream output) throws IOException, ServletException {
        // Parse the incoming request line
        input.readRequestLine(requestLine);
        String method = new String(requestLine.method, 0, requestLine.methodEnd);

        String uri = null;
        String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);

        // Validate the incoming request line
        if (method.length() < 1) {
            throw new ServletException("Missing HTTP request method");
        } else if (requestLine.uriEnd < 1) {
            throw new ServletException("Missing HTTP request URI");
        }

        // Parse any query parameters out of the request URI
        int question = requestLine.indexOf("?");
        if (question >= 0) {
            request.setQueryString(new String(requestLine.uri, question+1, requestLine.uriEnd - question -1));
            uri = new String(requestLine.uri, 0, question);
        } else {
            request.setQueryString(null);
            uri = new String(requestLine.uri, 0, requestLine.uriEnd);
        }

        // Checking for an absolute URI (with the HTTP protocol)
        if (!uri.startsWith("/")) {
            int pos = uri.indexOf("://");
            //  Parsing out protocol and host name
            if (pos != -1) {
                pos = uri.indexOf('/', pos+3);
                if (pos == -1) {
                    uri = "";
                } else {
                    uri = uri.substring(pos);
                }
            }
        }

        // Normalize URI (using String operations at the moment)
        String normalizedUri = normalize(uri);

        // Set the corresponding request properties
        request.setMethod(method);
        request.setProtocol(protocol);

        if (normalizedUri == null) {
            request.setRequestURI(uri);
        } else {
            request.setRequestURI(normalizedUri);
        }

    }

    void parseHeaders(SocketInputStream input) throws IOException, ServletException {
        while (true) {
            HttpHeader header = new HttpHeader();
            // Read the next header
            input.readHeader(header);
            if (header.nameEnd == 0) {
                if (header.valueEnd == 0) {
                    return;
                } else {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.colon"));
                }
            }

            String name = new String(header.name, 0, header.nameEnd);
            String value = new String(header.value, 0, header.valueEnd);

            request.addHeader(name, value);

            if ("cookie".equals(name)) {
                Cookie cookies[] = RequestUtil.parseCookieHeader(value);
                for (Cookie cookie : cookies ) {
//                    if ("jsessionid".equals(cookie.getName())) {
//                        // Accept anything requested in the URL
//                        if (!request.isRequestedSessionIdFromCookie()) {
//                            // Accept only the first session id cookie
//
//                        }
//                    }
                    request.addCookie(cookie);
                }
            }

            if ("content-length".equals(name)) {
                int n = -1;
                try {
                    n = Integer.parseInt(value);
                } catch (Exception e) {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.contentLength"));
                }

                request.setContentLength(n);
            }

            if ("content-type".equals(name)) {
                request.setContentType(value);
            }
        }
    }

    /**
     * Return a context-relative path, beginning with a "/", that represents
     * the canonical version of the specified path after
     * @param path Path to be normalized
     */
    protected String normalize(String path) {
        // todo normalize
        return path;
    }
}
