---
title: UI
description: Handle your epic UIs.
---

# Marmot UI Overview

In Marmot, you can create UIs that are serialized and sent to the client.

:::caution
Due to Marmot’s age, some features may still be buggy or unstable. We’re actively improving them.
:::

:::caution
Marmot UI is **HUD based**. This means that openable, interactable menus are not yet possible in Marmot.
:::

A simple UI can be created like this:

```kt
class TestUI : MarmotUI("test_ui") {
  fun new() {}
}
```

:::warning
Because of Marmot’s layout-based system, you **must** add components inside `group()` blocks.
:::

Most components expect a unique ID as their first parameter — using duplicates **will cause conflicts**.

---

## Adding Components

Inside the `new()` function, you can add components:

```kt
class TestUI : MarmotUI("test_ui") {
    fun new() {
        group("test_group") {
            text("test_text") {
                text = "Hello World"
                anchor = Anchor.CENTER_CENTER // horizontal_vertical
            }
        }
    }
}
```

---

## Sending a UI

To send a UI to a player or audience:

```kt
val ui = TestUI()
playerOrAudience.openUI(ui.new())
```