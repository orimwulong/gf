package com.orimwulong.gamefinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.orimwulong.gamefinder.game.GamesCollection;
import com.orimwulong.gamefinder.platform.Steam;
import com.orimwulong.gamefinder.util.GalleryWriter;

public class GameFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameFinder.class);

    private static final String OPT_TOTAL = "t";
    private static final String OPT_LADDER = "l";
    private static final String OPT_NEVER = "n";
    private static final String OPT_HELP = "h";
    private static final String OPT_GALLERY = "g";
    private static final String PROP_NEVER_PLAYED_MINS = "gf.never.played.mins";

    private static Options options;

    private Steam steam;
    private GamesCollection games;
    private CommandLine cmd;
    private long neverPlayedMins;
    public static void main(final String[] args) {

        Package gfPackage =  GameFinder.class.getPackage();
        String implementationVersion = gfPackage.getImplementationVersion();
        String implementationTitle = gfPackage.getImplementationTitle();
        if (LOGGER.isInfoEnabled() && !Strings.isNullOrEmpty(implementationVersion) && !Strings.isNullOrEmpty(implementationTitle)) {
            LOGGER.info(implementationTitle + " " + implementationVersion);
        }

        GameFinder gf = new GameFinder();
        gf.cmd = parseArgs(args);
        if (gf.cmd == null || gf.cmd.hasOption(OPT_HELP)) {
            usage(options);
        } else {
            if (gf.init()) {
                gf.run();
            }
        }
    }

    private static CommandLine parseArgs(String[] args) {
        options = new Options();
        options.addOption(Option.builder(OPT_TOTAL)
                                .longOpt("total")
                                .hasArg(false)
                                .desc("Total play time")
                                .build());
        options.addOption(Option.builder(OPT_LADDER)
                                .longOpt("ladder")
                                .hasArg(true)
                                .type(Integer.class)
                                .desc("Ladder of the n games played the most")
                                .build());
        options.addOption(Option.builder(OPT_NEVER)
                                .longOpt("never")
                                .hasArg(true)
                                .type(Integer.class)
                                .desc("List n games never played. A game never played is a game played less than the value of properties [" + PROP_NEVER_PLAYED_MINS + "] in minutes. -1 for all games never played")
                                .build());
        options.addOption(Option.builder(OPT_HELP)
                                .longOpt("help")
                                .hasArg(false)
                                .desc("Write this help message")
                                .build());
        options.addOption(Option.builder(OPT_GALLERY)
                                .longOpt("gallery")
                                .hasArg(false)
                                .desc("Create a HTML gallery in the current working path")
                                .build());

        CommandLineParser cmdParser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Unable to parse arguments", e);
            }
        }

        return cmd;
    }

    private static void usage(final Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("gf", "GameFinder -- what to play next?", options, "Please report issues at https://github.com/orimwulong/gf", true);
    }

    private boolean init() {
        boolean initComplete = false;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialising GameFinder...");
        }

        String propertiesFileName = "gf.properties";
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(propertiesFileName)));
            initComplete = true;
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Unable to load properties [" + propertiesFileName + "]");
            }
        }

        neverPlayedMins = Long.parseLong(properties.getProperty(PROP_NEVER_PLAYED_MINS, "10"));
        steam = new Steam();
        initComplete = initComplete && steam.configure(Maps.fromProperties(properties));
        this.games = new GamesCollection();

        return initComplete;
    }

    private void run() {
        steam.addOwnedGamesToCollection(games);

        if (cmd.hasOption(OPT_TOTAL)) {
            games.logTotalPlayTime();
        }

        if (cmd.hasOption(OPT_LADDER)) {
            games.logLadder(Integer.parseInt(cmd.getOptionValue(OPT_LADDER)));
        }

        if (cmd.hasOption(OPT_NEVER)) {
            games.logNeverPlayed(Integer.parseInt(cmd.getOptionValue(OPT_NEVER)), neverPlayedMins);
        }

        if (cmd.hasOption(OPT_GALLERY)) {
            GalleryWriter.writeHtmlGallery(games.getGamesList());
        }
    }

}
