package com.starit.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;

class BufferedServletInputStream extends ServletInputStream {
    private ByteArrayInputStream inputStream;
    public BufferedServletInputStream(byte[] buffer) {
        this.inputStream = new ByteArrayInputStream( buffer );
    }
    @Override
    public int available() throws IOException {
        return inputStream.available();
    }
    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputStream.read( b, off, len );
    }
}
