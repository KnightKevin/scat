package com.simon.scat.connector.http;

import java.io.IOException;
import java.io.InputStream;

public class SocketInputStream extends InputStream {
    private final InputStream is;

    private final byte[] buf;

    public SocketInputStream(InputStream is, int bufferSize) {
        this.is = is;
        buf = new byte[bufferSize];
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

}
