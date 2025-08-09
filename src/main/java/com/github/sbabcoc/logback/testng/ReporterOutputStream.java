package com.github.sbabcoc.logback.testng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class implements an {@link OutputStream} for the <b>TestNG Reporter</b>.
 * 
 * @see ReporterAppender
 */
public class ReporterOutputStream extends OutputStream {

    private boolean logToStdOut = false;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    /**
     * Default constructor
     */
    public ReporterOutputStream() {
    	super();
    }

    /**
     * Specify if output should be sent to <i>STDOUT</i> in addition to the TestNG HTML report.
     * 
     * @param logToStdOut {@code false} to send output only to the TestNG HTML report; 
     *         {@code true} to fork output to <i>STDOUT</i> and the TestNG HTML report.
     */
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