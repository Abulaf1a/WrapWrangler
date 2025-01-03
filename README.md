# Wrap Wrangler

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

Dependencies used:
- libGDX
- [Box2d](https://box2d.org/)

## Installation instructions:
- Install [Android Studio](https://developer.android.com/studio?gad_source=1&gclid=Cj0KCQiAst67BhCEARIsAKKdWOmP39m7bp4huGBuD8epgNN0uVVc1ILSeOmi5fxygCL0A1HjDjLTapwaAoCXEALw_wcB&gclsrc=aw.ds)
- Fork this project and clone it to your local machine
- Open the project with android studio
- Run on an emulator within the IDE of use your own android phone with developer mode enabled following [this](https://developer.android.com/studio/run/device) tutorial.


## How to Play
- Press anywhere to continue after game has loaded and splash screen shows
- From left to right, button 1 goes left, button 2 goes right, button 3 jump, button 3 is reset. 

## TODO:
- General refactoring to remove hardcoding and better separation of concerns. Create click handling classes, create methods in objects to handle their own movement, reactions to button presses etc.
- Package into an executable JAR using [this](https://github.com/libgdx/packr) tool for libGDX.
- Improve UI
- Create multiple levels and level selector. Allow permissions for reading/writing to files to serialise/deserialise game data to save progress
- Use [hyperlap2d](https://hyperlap2d.rednblack.games/) to create levels using a GUI.
- Implement dispose(), pause(), resume(), and hide() method bodies to handle context changes. Currently implementation does not dispose of some objects when they have finished use, creating memory leak. Another bug which occurs is a failure to re-assign sprite textures correctly when phone context/configuration changes. 


# libGDX generated readme below:

# WrapWrangler

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `android`: Android mobile platform. Needs Android SDK.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
