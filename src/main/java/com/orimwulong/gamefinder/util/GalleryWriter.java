package com.orimwulong.gamefinder.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orimwulong.gamefinder.game.Game;

public final class GalleryWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GalleryWriter.class);
    private static final String GALLERY_NAME = "gallery.html";

    private GalleryWriter() {
        throw new java.lang.UnsupportedOperationException("GalleryWriter is a utility class and cannot be instantiated");
    }

    public static void writeHtmlGallery(SortedMap<String, Game> collection) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss-", Locale.getDefault());
        String galleryName = sdf.format(Date.from(Instant.now()));
        galleryName += GALLERY_NAME;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(galleryName), StandardCharsets.UTF_8)) {
            writer.write("<html>");
            writer.write("</html>");
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
