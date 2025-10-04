---
title: Get Started with Paper
description: Add Marmot to your Paper server.
---

## Installation
Add the repository below to your build config:
```
https://repo.znotchill.me/repository/maven-releases/
```

Example:
```kt
repositories {
  mavenCentral()
  maven { url = uri("https://repo.znotchill.me/repository/maven-releases/") }
}
```

Add the library:
```
me.znotchill.marmot:paper-api:LATEST
me.znotchill.marmot:common:LATEST
```

Replace `LATEST` with whichever version is latest according to the [GitHub](https://github.com/zNotChill/marmot). Currently, it is `1.2.9`.

## Usage
Once you have finished installation, you can add the Marmot API to your Paper server:
```kt
MarmotAPI.registerEvents(JavaPlugin)
MarmotAPI.registerTasks(JavaPlugin)
```

Full example:
```kt
class MarmotPaperTest : JavaPlugin() {
    override fun onEnable() {
        MarmotAPI.registerEvents(this)
        MarmotAPI.registerTasks(this)
    }
}

```

Now, the Marmot API is hooked into your server and everything is good to go!
The API handles all of the internals so you don't have to worry about anything.

Now the API is hooked, you can hook into Marmot events, for example:
```kt
MarmotAPI.addEvent(MarmotEvent.LEFT_CLICK_BEGIN) { player ->
  player.sendMessage("${player.username} began holding left click!")
}
MarmotAPI.addEvent(MarmotEvent.LEFT_CLICK_END) { player ->
  player.sendMessage("${player.username} stopped holding left click!")
}
```

Since the Marmot API is unified across all server softwares, the API specification works anywhere.