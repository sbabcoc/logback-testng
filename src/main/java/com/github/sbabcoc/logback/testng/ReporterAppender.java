package com.github.sbabcoc.logback.testng;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import ch.qos.logback.core.OutputStreamAppender;

public class ReporterAppender<E> extends OutputStreamAppender<E> {

    private static final Method getCurrentTestResult;
    private static final Method getOutput;
    private static final Method log;

    protected boolean logToStdOut = false;

    static {
        Method gctr = null;
        Method go = null;
        Method l = null;
        try {
            Class<?> reporter = Class.forName("org.testng.Reporter");
            Class<?> testResult = Class.forName("org.testng.ITestResult");
            
            gctr = reporter.getMethod("getCurrentTestResult");
            go = reporter.getMethod("getOutput", testResult);
            l = reporter.getMethod("log", String.class, Boolean.TYPE);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            gctr = null;
            go = null;
            l = null;
        } finally {
            getCurrentTestResult = gctr;
            getOutput = go;
            log = l;
        }
    }

    @Override
    public void start() {
        ReporterOutputStream ros = new ReporterOutputStream();
        ros.setLogToStdOut(logToStdOut);
        super.setOutputStream(ros);
        super.start();
    }

    @Override
    protected void subAppend(E event) {
        if (started) {
            super.subAppend(event);
            ((ReporterOutputStream) getOutputStream()).flush();
        }
    }

    @Override
    public void setOutputStream(OutputStream outputStream) {
        throw new UnsupportedOperationException("The output stream of " + this.getClass().getName() + " cannot be altered");
    }

    @Override
    public String toString() {
        if ((getCurrentTestResult != null) && (getOutput != null)) {
            try {
                Object testResult = getCurrentTestResult.invoke(null);
                return concat(getOutput.invoke(null, testResult));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // nothing to do here
            }
        }
        return "";
    }

    /**
     * Specify if output should be sent to <i>STDOUT</i> in addition to the TestNG HTML report.
     * @param logToStdOut {@code false} to send output only to the TestNG HTML report; 
     *         {@code true} to fork output to <i>STDOUT</i> and the TestNG HTML report.
     */
    public void setLogToStdOut(boolean logToStdOut) {
        this.logToStdOut = logToStdOut;
        ReporterOutputStream ros = (ReporterOutputStream) getOutputStream();
        if (ros != null) {
            ros.setLogToStdOut(logToStdOut);
        }
    }

    /**
     * Determine if output is being forked to <i>STDOUT</i> and the TestNG HTML report.
     * @return {@code false} if output is being sent solely to the TestNG HTML report; 
     *         {@code true} if output is being forked to <i>STDOUT</i> and the TestNG HTML report.
     */
    public boolean doLogToStdOut() {
        return logToStdOut;
    }

    /**
     * Log the passed string to the HTML reports. If logToStandardOut is true, the string will also be printed on standard out.
     * @param s The message to log
     * @param logToStandardOut Whether to print this string on standard out too
     */
    static void log(String s, boolean logToStandardOut) {
        if (log != null) {
            try {
                log.invoke(null, s, logToStandardOut);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // nothing to do here
            }
        }
    }

    /**
     * Concatenate the elements of the specified list of strings.
     * @param parts list of strings to concatenate
     * @return concatenated string
     */
    @SuppressWarnings("unchecked")
    private static String concat(Object parts) {
        StringBuilder builder = new StringBuilder();
        for (String part : (List<String>) parts) {
            builder.append(part);
        }
        return builder.toString();
    }

}
