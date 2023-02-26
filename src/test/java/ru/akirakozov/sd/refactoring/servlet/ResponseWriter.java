package ru.akirakozov.sd.refactoring.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ResponseWriter extends PrintWriter {
    public ResponseWriter() {
        super(new StringWriter());
    }

    @Override
    public String toString() {
        return out.toString();
    }
}
