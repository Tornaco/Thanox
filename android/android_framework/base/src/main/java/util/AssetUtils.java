package util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.SneakyThrows;

public class AssetUtils {

    @SuppressWarnings("UnstableApiUsage")
    public static String readFileToString(Context context, String name) throws IOException {
        AssetManager am = context.getAssets();
        InputStream is = am.open(name);
        return CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
    }

    @SneakyThrows
    public static String readFileToStringOrThrow(Context context, String name) {
        return readFileToString(context, name);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void copyTo(Context context, String name, File dest) throws IOException {
        AssetManager am = context.getAssets();
        InputStream is = am.open(name);
        Files.asByteSink(dest).writeFrom(is);
    }
}
