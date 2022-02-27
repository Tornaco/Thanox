package github.tornaco.android.thanos.core;

import java.io.PrintWriter;

import lombok.NonNull;

public class PrinterWriterAdapter extends IPrinter.Stub {

    @NonNull
    private final PrintWriter writer;

    public PrinterWriterAdapter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void println(String content) {
        writer.println(content);
    }

    @Override
    public void print(String content) {
        writer.print(content);
    }
}
