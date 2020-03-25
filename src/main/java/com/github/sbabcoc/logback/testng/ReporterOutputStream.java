package com.github.sbabcoc.logback.testng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ReporterOutputStream extends OutputStream {
	
	private boolean logToStdOut = false;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	public void setLogToStdOut(boolean logToStdOut) {
		this.logToStdOut = logToStdOut;
	}
	
	@Override
	public void write(int b) throws IOException {
		baos.write(b);
	}
	
	@Override
    public void write(byte b[], int off, int len) throws IOException {
		baos.write(b, off, len);
	}
	
	@Override
	public void flush() {
		ReporterAppender.log(baos.toString(), logToStdOut);
		baos.reset();
	}
	
	@Override
	public void close() {
		flush();
	}
	
}