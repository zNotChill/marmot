---
title: Text
description: Render styled text with color, padding, and background.
---

## Text

In Marmot, you can create UI texts, which can have color, padding, background, and size.

Example:
```kt
class TestUI : MarmotUI("test_ui") {
    fun new() {
        group("test_group") {
            val text1 = text("test_text") {
                text = "Hello World"
                anchor = Anchor.CENTER_CENTER
            }

            val text2 = text("test_text_2") {
                text = "Test text"
                padding = Spacing(5f)
                backgroundColor = UIColor(255, 255, 255)
                color = UIColor(0, 0, 0)
            } rightOf text1
        }
    }
}
```