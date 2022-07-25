package com.orimwulong.gamefinder.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orimwulong.gamefinder.game.Game;
import com.orimwulong.gamefinder.util.TimeHelper;

public class HtmlGalleryWriter implements GamesCollectionWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlGalleryWriter.class);
    private static final String GALLERY_NAME = "-gallery.html";

    @Override
    public void write(List<Game> gamesList) {
        String currentDate = TimeHelper.getNowAsPattern("yyyyMMdd-HHmmss");
        String galleryName = currentDate + GALLERY_NAME;
        String title = "My games collection as of " + currentDate;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(galleryName), StandardCharsets.UTF_8)) {
            writer.write("<!DOCTYPE html><html><head><title>" + title + "</title></head>");
            writer.write("<body><div><p>" + title + "<br/>Total: " + gamesList.size() + " games</p></div><div>");
            writer.write("<table><tr><th>Icon</th><th>Name</th><th>Play Time</th><th>Owned On</th></tr>");
            Consumer<Game> getGameRow = new Consumer<Game>() {
                @Override
                public void accept(Game t) {
                    try {
                        String name = t.getName();
                        String icon = "<img src=\"" + t.getImgIconUrl() + "\">";
                        String playTime = TimeHelper.getDurationText(t.getTotalMinutesPlayed());
                        String ownedOn = t.getOnPlatforms().toString();
                        writer.write("<tr><td>" + icon + "</td><td>" + name + "</td><td>" + playTime + "</td><td>" + ownedOn + "</td></tr>");
                    } catch (IOException e) {
                        if (LOGGER.isErrorEnabled()) {
                            LOGGER.error("While writing game row", e.getMessage());
                        }
                    }
                }
            };
            gamesList.forEach(getGameRow);
            writer.write("</table></div></body></html>");
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(e.getMessage());
            }
        }
    }

}
