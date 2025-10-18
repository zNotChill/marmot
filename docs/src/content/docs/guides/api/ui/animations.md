
---
title: UI Animations
description: Animate your UIs.
---

# Animations

Marmot also supports UI animations, for example:
```kt
class TestUI : MarmotUI("test_ui") {
  fun new() {
    group("test_group") {
      val text1 = text("test_text") {
        text = "Hello World"
        anchor = Anchor.CENTER_CENTER // horizontal_vertical
      }
      
      text1.schedule(20) { // 20 ticks from now
        // You can run multiple animations at once, hence the list.

        // This will move the text to 50,50 from the player's crosshair
        // (since it's anchored to CENTER_CENTER), and reduce its opacity
        // to 0.5 (50%) over the course of 0.5 seconds.
        listOf(
          move(
            to = Vec2(50f, 50f),
            duration = 0.5,
            easing = Easing.LINEAR
          ),
          opacity(
            opacity = 0.5f,
            duration = 0.5,
            easing = Easing.EASE_IN_OUT
          )
        )
      }
    }
  }
}
```

### Easing
Marmot currently supports **24** easing variants:

```kt
enum class Easing {
    LINEAR,
    EASE_IN,
    EASE_OUT,
    EASE_IN_OUT,
    EASE_IN_CUBIC,
    EASE_OUT_CUBIC,
    EASE_IN_OUT_CUBIC,
    EASE_IN_QUINT,
    EASE_OUT_QUINT,
    EASE_IN_OUT_QUINT,
    EASE_IN_BACK,
    EASE_OUT_BACK,
    EASE_IN_OUT_BACK,
    EASE_IN_ELASTIC,
    EASE_OUT_ELASTIC,
    EASE_IN_OUT_ELASTIC,
    EASE_OUT_BOUNCE,
    EASE_IN_BOUNCE,
    EASE_IN_OUT_BOUNCE,
    EASE_IN_SINE,
    EASE_OUT_SINE,
    EASE_IN_OUT_SINE,
    EASE_IN_EXPO,
    EASE_OUT_EXPO,
    EASE_IN_OUT_EXPO
}
```

Most of these can be found and visualized [here](https://easings.net/), and you can also see how they work [here](https://github.com/zNotChill/marmot/blob/b42b2780c162fe57dccfa7c76954669d617a0497/mod/src/client/kotlin/me/znotchill/marmot/client/ui/UIRenderer.kt#L170) on the client.

You can also nest schedulers inside each other, which could look something like this: (**before 1.2.12, this functionality was broken!**)
```kt
text1.schedule(20) { // 20 ticks from now
    text2.schedule(5) { // 25 ticks from now
        text3.schedule(50) { // 75 ticks from now
            
        }
        
        text4.schedule(10) { // 35 ticks from now
            
        }
    }
}
```
