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
- Origin
- Microsoft Store

## Run requirements

- Java 18 or above
- Configuration is done with your own credentials for each platform

## Configuration

- Read and modify the properties file **gf.properties**

## Usage

See program usage for details

```bat
    bin\gf.bat -h
```

```shell
    bin/gf -h
```

## Issues / Improvements

Please report issues and improvements idea using the [GitHub Issues page](https://github.com/orimwulong/gf/issues)\
Use the labels to differentiate between a *bug* and an *enhancement*

## License

See LICENSE file

## References

- [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [Axion Release Plugin](https://github.com/allegro/axion-release-plugin)
- [GitHub MarkDown Syntax](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)
- [Gradle](https://gradle.org)
- [Gson](https://github.com/google/gson)
- [Guava](https://github.com/google/guava)
- [LOGBack](https://logback.qos.ch)
- [SLF4j](https://www.slf4j.org)
- [Steam Web API](https://developer.valvesoftware.com/wiki/Steam_Web_API) or [Steam Web API](https://partner.steamgames.com/doc/webapi)
