---
title: Boxes
description: Render UI boxes!
---

## Boxes

In Marmot, you can create a UI Box using one solid UIColor.

Example:
```kt
box("box") {
    fillScreen = true // size the component to the client's viewport
    anchor = Anchor.TOP_LEFT // anchor the component to the TOP_LEFT, required for fillScreen components
    color = UIColor(100, 100, 100)
}
```
```kt
box("box") {
    color = UIColor(100, 100, 100)
    size = Vec2(50f, 50f)
}
```