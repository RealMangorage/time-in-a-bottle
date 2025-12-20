<a href="https://discord.mangorage.org/" rel="nofollow"><img src="https://img.shields.io/discord/834300742864601088?label=Discord&amp;logo=discord&amp;logoColor=white&amp;style=for-the-badge" alt="Discord"></a>

## Introduction

A Minecraft mod that adds one simple game mechanic: small rooms inside of blocks. 

You can grab the latest build off
[Curseforge] or on [Modrinth].

| Version | Minecraft Version | Released | Support | Support Ends |
|:--------|:-----------------:|---------:|:-------:|--------------|
| 6.x     |   1.21 / 1.21.1   |     2024 |    ✅    | 6/1/2026     |
| 7.x     |       26.1        |     2026 |    ❌    | N/A          |



\* *Note - only the most recent versions are shown here for brevity.*

\* Note: The way you build against has been changed, API version has been removed entirely. Use Mod Version as shown below.

# Contributing

Make a PR to contribute. 

Please make PR description clear as to what its goal is. 

## Project Layout
Time in a bottle is split into multiple projects to make updating and version maintenance easier. 

The following is a quick summary of each module's purpose:

|     Module | Description                                                  |
|-----------:|--------------------------------------------------------------|
|   buildSrc | Where we keep all of our build script stuff for each module  |
|     common | Where all the common code built against vanilla mc is at     |
| common-api | Where all the common api code built against vanilla mc is at |
|      forge | Where all the common code built against forge is at          |
|     fabric | Where all the common code built against fabric is at         |
|   neoforge | Where all the common code built against neoforge is at       |

## API

To use the API just add https://maven.mangorage.org/ as a maven repo

```
repositories {
    maven {
        url = "https://maven.mangorage.org/releases"
    }
}

dependencies {
    compileOnly("org.mangorage:tiab-<module>-api:<version>")
}
```


## External Libraries

|   Library | Optional | Description    |
|----------:|----------|----------------|
| EMI       |   Yes    | Compat for EMI |
| JEI       |   Yes    | Compat for JEI |


[Curseforge]: https://www.curseforge.com/minecraft/mc-mods/time-in-a-bottle-universal
[Modrinth]: https://modrinth.com/mod/time-in-a-bottle-universal
