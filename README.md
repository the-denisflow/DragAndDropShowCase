# DragAndDropShowCase

An Android showcase app demonstrating a home screen-style drag-and-drop grid built with Jetpack Compose and the native `View.startDragAndDrop()` API.

## Why the Hybrid Compose + View Strategy?

Compose introduced its own drag-and-drop API (`Modifier.dragAndDropSource` / `dragAndDropTarget`) in 1.5, but it is intentionally high-level and designed for cross-app data transfer (sharing text, images, files between apps). It does not expose the raw `DragEvent` stream, which makes fine-grained in-app interactions — like a home screen reorder — difficult to implement cleanly.

The native `View.startDragAndDrop()` API, on the other hand, gives full access to every drag event (`ACTION_DRAG_STARTED`, `ACTION_DRAG_LOCATION`, `ACTION_DROP`, `ACTION_DRAG_ENDED`) and lets you attach any object as local state. This project uses a single full-screen transparent `View` (embedded via `AndroidView`) as the drag event receiver, while keeping all the UI in Compose.

### Pros

- **Full event access** — `ACTION_DRAG_LOCATION` fires on every frame, enabling real-time drop zone detection and instant visual feedback during the drag.
- **Custom drag shadow (the main motivation)** — `View.startDragAndDrop()` accepts a `DragShadowBuilder`, giving full control over what the dragged item looks like.
- **Composable UI is untouched** — the `View` is transparent and only exists for event routing; all layout, animation, and state remain in Compose.

### Cons

- **Two gesture systems in parallel** — long-press is detected by Compose's `detectDragGesturesAfterLongPress`, but the drag itself is handled by the View system. A `LaunchedEffect` is needed to hand off between them, adding a one-frame delay before `startDragAndDrop()` is called.

## How It Works

The project uses a **hybrid Compose + View** approach. Compose handles the UI and gesture detection, while a native `View` (embedded via `AndroidView`) receives the system `DragEvent` callbacks. This bridges the gap between Compose's pointer input and Android's drag-and-drop framework.

### Drag flow

1. Long press on a tile triggers `detectDragGesturesAfterLongPress`
2. A `GraphicsLayer` snapshot of the tile is used as the custom drag shadow
3. `View.startDragAndDrop()` starts the native drag session
4. `ACTION_DRAG_LOCATION` events are forwarded to `DropZoneDetectorHelperImpl`
5. The detector maps the cursor Y to a grid row via `GridRowPerception`, then finds the target tile by its horizontal drop zone
6. `TilesRepository` reorders the list and emits the new state via `StateFlow`

### Drop zone detection

Each tile is divided into three horizontal zones (20% / 60% / 20%):

```
|  left swap  |  center (folder, unused)  |  right swap  |
```

Dragging over the left or right zone triggers a swap with that tile.

## Architecture

Clean architecture with three layers:

```
data/       — DragListenerImpl, DropZoneDetectorHelperImpl, DragHelperImpl, TilesRepositoryImpl
domain/     — Repository interfaces, use cases, domain models (GridRowPerception, TileBounds, …)
presentation/ — Composables, DragAndDropState, TilesViewModel
```

## Tech Stack

| Library | Usage |
| --- | --- |
| Jetpack Compose + Material 3 | UI |
| Hilt | Dependency injection |
| Kotlin Coroutines + StateFlow | Async state management |
| Coil | Wallpaper image loading |
| AndroidX Activity Compose | Edge-to-edge support |

## Requirements

- Android API 26+
- compileSdk 35
