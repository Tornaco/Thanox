package github.tornaco.android.thanos.core;

import java.io.PrintWriter;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class PrinterWriterAdapter extends IPrinter.Stub {

    @NonNull
    private final PrintWriter writer;

    @Override
    public void println(String content) {
        writer.println(content);
    }

    @Override
    public void print(String content) {
        writer.print(content);
    }
}
