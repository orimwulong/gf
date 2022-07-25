package com.orimwulong.gamefinder.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleWriter.class);

    private SimpleWriter() {
        throw new java.lang.UnsupportedOperationException("SimpleWriter is a utility class and cannot be instantiated");
    }

    public static void writeToFile(String filePath, String data) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8)) {
            writer.write(data);
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(e.getMessage());
            }
        }
    }

}
