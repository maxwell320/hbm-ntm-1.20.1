# HBM's Nuclear Tech Mod Modern Port

This repo is for a 1.20.1 Forge port of HBM's Nuclear Tech Mod.

It is not going to be a straight copy of the old 1.7.10 code. Too much of that code depends on old Forge and old Minecraft internals, so the only sane way to do this is to rebuild it piece by piece and keep the original feel where it matters.

## Current state

The project is past pure bootstrap and is in active system-by-system reconstruction.

Current rough progress snapshot:
- generated material and item naming parity audit: `~90%`
- overall full 1.7.10 gameplay/content parity: still much earlier than that

So far the project has:
- a working Forge 1.20.1 workspace
- Java 17 set as the build toolchain
- Gradle runs working
- a minimal mod bootstrap
- a basic CI build
- generated material/item infrastructure with datagen-backed registries and assets
- a large legacy-backed parity sweep across isotope, powder, wire, crystal, and special-material labels

## Project baseline

- Minecraft: `1.20.1`
- Forge: `47.4.18`
- Java: `17`
- Mod ID: `hbmntm`
- Package: `com.hbm.ntm`

## What comes first

The first real milestones are the boring but necessary parts:
- registry and package structure cleanup
- core materials and item families
- basic blocks and recipes
- machine foundations
- hazard and radiation groundwork
- heat, power, and reactor groundwork

## What is not happening yet

- full content parity with the 1.7.10 version
- one huge dump of legacy code
- advanced compat support
- dimensions and large world systems
- anything that pretends this is already release-ready

## Building

From the project root:

- `gradlew.bat genIntellijRuns`
- `gradlew.bat build`
- `gradlew.bat runClient`
- `gradlew.bat runServer`

## Notes

This is an unofficial port effort based on the original HBM project by HbMinecraft and contributors.

The plan will move around as the project gets deeper into actual subsystem work. `ROADMAP.md` is just the current working order, not a promise that every phase will stay exactly the same.
