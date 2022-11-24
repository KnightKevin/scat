package com.simon.scat.connector;

import com.simon.scat.connector.http.HttpResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class ResponseStream extends ServletOutputStream {

    protected Boolean closed = false;

    protected Boolean commit = false;

    protected Integer count = 0;

    protected HttpResponse response = null;



    public ResponseStream(HttpResponse response) {
        super();
        this.closed = false;
        this.commit = false;
        this.count = 0;
        this.response = response;
    }

    public void setCommit(boolean commit) {
        this.commit = commit;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {

    }
}
