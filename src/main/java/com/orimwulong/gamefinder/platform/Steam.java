package com.orimwulong.gamefinder.platform;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.gson.stream.JsonReader;
import com.orimwulong.gamefinder.game.Game;
import com.orimwulong.gamefinder.game.GamesCollection;
import com.orimwulong.gamefinder.util.HttpsHelper;

public class Steam implements Platform {

    private static final Logger LOGGER = LoggerFactory.getLogger(Steam.class);
    private static final String PLATFORM_NAME = "Steam";
    private static final String PROP_STEAM_STEAMID64 = "steam.steamid64";
    private static final String PROP_STEAM_WEBAPI_KEY = "steam.webapi.key";
    private static final String BASE_URL_GET_OWNED_GAMES = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?format=json&include_appinfo=true&include_played_free_games=true";
    private static final String BASE_URL_GAME_IMG_ICON = "https://media.steampowered.com/steamcommunity/public/images/apps";

    private String steamID64;
    private String webAPIKey;
    private int totalGamesCount;

    @Override
    public String getName() {
        return PLATFORM_NAME;
    }

    @Override
    public boolean configure(Map<String, String> configMap) {
        if (configMap == null) {
            return false;
        }

        this.steamID64 = configMap.get(PROP_STEAM_STEAMID64);
        this.webAPIKey = configMap.get(PROP_STEAM_WEBAPI_KEY);

        if (Strings.isNullOrEmpty(this.steamID64) || Strings.isNullOrEmpty(this.webAPIKey)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("configMap didn't not contain expected keys or values were empty.");
            }
            return false;
        }

        return true;
    }

    @Override
    public void addOwnedGamesToCollection(GamesCollection collection) {
        String gamesList = getRawOwnedGamesList();
        try (JsonReader reader = new JsonReader(new StringReader(gamesList))) {
            readRoot(reader, collection);
            reader.close();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Steam games count ["+ this.totalGamesCount + "]. Loaded collection games count [" + collection.getNumberOfGames() + "].");
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Exception while parsing content", e);
            }
        }
    }

    private String getRawOwnedGamesList() {
        String result;
        String urlWithTokens = BASE_URL_GET_OWNED_GAMES +"&steamid=" + this.steamID64 + "&key=" + this.webAPIKey;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Retrieving owned games list from " + PLATFORM_NAME + "...");
        }
        result = HttpsHelper.getHttpsContent(urlWithTokens);
        if (Strings.isNullOrEmpty(result) && LOGGER.isErrorEnabled()) {
            LOGGER.error("Unable to retrieve ownder games list. Base URL was [" + BASE_URL_GET_OWNED_GAMES + "]");
        }
        return result;
    }

    private void readRoot(JsonReader reader, GamesCollection collection) throws IOException {
        reader.beginObject();
        // response
        reader.nextName();
        readResponse(reader, collection);
        reader.endObject();
    }

    private void readResponse(JsonReader reader, GamesCollection collection) throws IOException {
        reader.beginObject();
        // game_count
        reader.nextName();
        this.totalGamesCount = reader.nextInt();
        readGames(reader, collection);
        reader.endObject();
    }

    private void readGames(JsonReader reader, GamesCollection collection) throws IOException {
        // games
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            readGame(reader, collection);
        }
        reader.endArray();
    }

    private void readGame(JsonReader reader, GamesCollection collection) throws IOException {
        long gameAppId = -1L;
        String gameName = null;
        long totalMinutesPlayed = -1L;
        String gameImgIcon = null;
        String gameImgIconUrl = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if ("appid".equals(name)) {
                gameAppId = reader.nextLong();
            } else if ("name".equals(name)) {
                gameName = reader.nextString();
            } else if ("playtime_forever".equals(name)) {
                totalMinutesPlayed = reader.nextLong();
            } else if ("img_icon_url".equals(name)) {
                gameImgIcon = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (gameAppId != -1L && !Strings.isNullOrEmpty(gameImgIcon)) {
            gameImgIconUrl = BASE_URL_GAME_IMG_ICON + "/" + gameAppId + "/" + gameImgIcon + ".jpg";
        }
        Game game = new Game(gameName, totalMinutesPlayed, gameImgIconUrl, PLATFORM_NAME);
        collection.addGame(game);
    }

}
