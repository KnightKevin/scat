package com.simon.scat.connector;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ResponseWriter extends PrintWriter {
    public ResponseWriter(OutputStreamWriter writer) {
        super(writer);
    }
}
