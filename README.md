# GameFinder

Game Finder -- what to play next?

## Vision

A simple command line tool to provide proposals on what to play next based on:

- you own the game on a digital platform
- the game score on [OpenCritic](https://opencritic.com/)
- you never played the game or played less than X minutes

Maybe one day a fancy GUI

### Supported platforms

Currently supported platforms:

- Steam

Maybe one day:

- GOG
- Epic Game Store

## Run requirements

- Java 18 or above
- Configuration is done with your own credentials for each platform

## Configuration

 1. Read and modify the properties file **gf.properties**

## Usage

```shell
    How many hours / days have I played on Steam across all apps?
    What are the top X games I played in terms of hours? (Similar to what you can find in someone profiles)
    List X games I never played.
```

## Issues / Improvements

Please report issues and improvements idea using the [GitHub Issues page](https://github.com/orimwulong/gf/issues)
Use the labels to differentiate between a *bug* and an *enhancement*

## References

- [Steam Web API](https://developer.valvesoftware.com/wiki/Steam_Web_API)
- [GitHub MarkDown Syntax](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)
