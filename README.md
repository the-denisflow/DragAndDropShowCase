If you’ve tried to implement drag and drop in Jetpack Compose, you’ve probably hit a wall.
While Compose excels at building modern, reactive UI’s, it doesn’t provide a native drag and drop support that works seamlessly across your entire app, or handles complex interactions like reordering lists or moving items between containers.

The traditional Android drag and drop API (OnDragListener) is mature, powerful, completely view-based. This creates an interesting challenge: how do we leverage the View drag and drop system while staying in the compose world. The answer: we build a bridge.

Why do we need a state class? 

This is when the DragAndDropState comes in. When bridging between Compose and Views, we face several challenges.
Compose Needs Observable State:
Compose recomposes when state changes. Our drag and drop system needs to communicate with composeables, to highlight drop zones, update UI during drags, and animate items.
Views Need Callbacks
The Android View system uses View.OnDragListener, which operates through callbacks. We need something that can implement this interface while also exposing reactive state to Compose.

We need a Single Source of Truth 
In a typical drag operation, multiple components need to know: 
What’s being dragged?
Where is it?
When did the drag start/end?
Bridging Two Paradigms 
The state class acts as a translator : 
From Compose to View: When you call startDrag() , it translates Compose concept(GraphicsLayers Density)  into View concepts(DragShadowBuilder).
From View to Compose: When the View system triggers onDrag(), it updates 
observable state that triggers Compose recompositions.

