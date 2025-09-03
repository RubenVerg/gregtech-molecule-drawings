# GregTech Addon Template
## Template for GregTech Modern addons on 1.20.1

 Original template by [screret](https://github.com/screret), maintained by [JuiceyBeans](https://github.com/JuiceyBeans)

<hr>

## How do I make an addon for GregTech Modern?
Well for one, you WILL need to know Java to make an addon. There's no getting around this. A good starting point would be [MOOC](https://java-programming.mooc.fi/) or [W3Schools](https://www.w3schools.com/java/
)

Unfortunately, there isn't any official documentation so far for making GregTech Modern addons. There are a couple of mods you can look at to reference though!

Repositories for other addons:

- [GT Community Additions](https://github.com/mordgren/GTCA)
- [Gregtech: Extended Chemistry Extended](https://github.com/jmoiron/Gregtech-Extended-Chemistry)
- [GCYLM](https://github.com/eve336/gcylm)
- [Gregicality Rocketry](https://github.com/Argent-Matter/gcyr/)

Additionally, you may be able to find help on the [GregTech CEu Discord](https://discord.gg/bWSWuYvURP)!

<hr>

## This template comes packaged with [Spotless](https://github.com/diffplug/spotless)!

### 1. What is Spotless?
- Spotless keeps your code neatly formatted. It's essentially a grammar check for your code!
### 2. Can I choose not to use Spotless?
- Yes! Spotless is completely optional and will not affect your project by default
### 3. How do I run Spotless?
- You can run Spotless anytime by:
  - Running the `spotlessApply` task from the Gradle tab in IntelliJ
  - Installing the [Spotless Gradle plugin for IntelliJ](https://plugins.jetbrains.com/plugin/18321-spotless-gradle)
  - Typing in `gradlew.bat :spotlessApply` if you're on Windows
  - Typing in `bash gradlew :spotlessApply` if you're on Linux
### 4. So how do I check if Spotless has been applied to my code?
- Running `spotlessApply` will format all files for you automatically! If you want GitHub to check each commit for if Spotless has been run, you can add [this](https://github.com/Frontiers-PackForge/CosmicCore/blob/main-1.20.1-forge/.github/workflows/spotless.yml) and [this](https://github.com/Frontiers-PackForge/CosmicCore/blob/main-1.20.1-forge/.github/actions/build_setup/action.yml) to your project
