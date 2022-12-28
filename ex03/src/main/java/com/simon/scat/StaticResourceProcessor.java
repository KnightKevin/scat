package com.simon.scat;

import com.simon.scat.connector.http.HttpRequest;
import com.simon.scat.connector.http.HttpResponse;

import java.io.IOException;

public class StaticResourceProcessor {
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        response.sendStaticResource();
    }
}
