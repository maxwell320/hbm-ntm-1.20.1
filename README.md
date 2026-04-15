# HBM Nuclear Tech Modern Port (Forge 1.20.1)

This repository is an active restoration port of HBM Nuclear Tech to modern Forge.

The goal is not to drag 1.7.10 code forward line-for-line. The goal is to preserve gameplay behavior while rebuilding systems in a maintainable 1.20.1 codebase.

## Current status

The project is in active subsystem parity work.

Recent completed slices include:
- major shared barrel family coverage with runtime behavior
- explosive and waste barrel variants
- first pass of disperser/glyphid fluid container restoration

Still in progress:
- broader machine families
- full fluid logistics parity
- missile, weapon, and worldgen parity layers

## Baseline

- Minecraft: 1.20.1
- Forge: 47.4.18
- Java: 17
- Mod ID: hbmntm
- Package root: com.hbm.ntm

## Official releases

This project now has official distribution channels on Modrinth and CurseForge.

If a jar is posted outside the official project pages, treat it as an unofficial mirror unless explicitly linked by the maintainer.

## Build and run

From the repository root:

- gradlew.bat compileJava
- gradlew.bat runData
- gradlew.bat runClient
- gradlew.bat runServer

## Licensing and attribution

Code in this repository is GPL-3.0-or-later.

Read these files before redistributing:
- LICENSE
- LICENSE.txt
- NOTICE.md
- CREDITS.txt

In short: you can fork and modify under GPL terms, but you must keep attribution and license notices intact. Project branding and implied endorsement are handled separately in NOTICE.md.

## Attribution

Original HBM Nuclear Tech was created by HbMinecraft and contributors.

This repository is an independent modern port effort and is not the original 1.7.10 codebase.
