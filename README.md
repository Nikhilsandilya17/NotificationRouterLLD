# Notification Router

Low-level design for a notification router of an ecommerce website.

## Requirements

1. Each user has preferred notification channel(s): `EMAIL`, `SMS`, `PUSH`.
2. Every notification has a priority: `URGENT`, `NORMAL`.
3. `URGENT` notifications are sent to **all channels**; `NORMAL` notifications are sent **only to the user's preferred channels**.
4. Routing logic and notification handlers (delivery) are both in scope.

## Class Diagram

```
┌──────────────────────────────┐       ┌─────────────────────────────────┐
│      NotificationService     │       │          User                   │
│  (interface, router/)        │       │─────────────────────────────────│
│ + sendNotification(User,     │       │ - id: String                    │
│       Notification)         │       │ - name / email / contactNumber  │
│ + createNotification(...)    │       │ - notificationPreferences:      │
└──────────────┬───────────────┘       │     Set<NotificationType>       │
               │ implements            │─────────────────────────────────│
┌──────────────▼───────────────┐       │ + getNotificationPreferences()  │ ← synchronized
│  NotificationServiceImpl     │       │ + addNotificationPreference()   │ ← synchronized
│──────────────────────────────│       │ + optOutOfNotificationPref()    │ ← synchronized
│ - handlerRegistry:           │       └─────────────────────────────────┘
│   Map<NotificationType,      │
│       NotificationHandler>   │       ┌─────────────────────────────────┐
│──────────────────────────────│       │        Notification             │
│ + sendNotification(...)      │──────▶│  (immutable)                    │
└───────┬──────────────────────┘       │─────────────────────────────────│
        │ looks up handlers by type    │ - id: String (UUID)             │
        │                              │ - createdAt: LocalDateTime      │
        ▼                              │ - message: String               │
┌──────────────────────────────┐       │ - sentTo: String (userId)       │
│   NotificationHandler        │       │ - notificationPriority          │
│   (interface, handler/)      │       └─────────────────────────────────┘
│ + handleNotification(User,   │
│       Notification)          │       ┌─────────────────────────────────┐
│ + getType(): NotificationType│       │    enums                        │
└──────┬───────────────────────┘       │ NotificationType: EMAIL/SMS/PUSH│
       │ ▲ implements                  │ NotificationPriority:           │
       │ │                             │    URGENT/NORMAL                │
 ┌─────┴─┴─────┬───────────────┬───────┴──┐  └─────────────────────────────┘
 │             │               │          │
EmailNotif.  SMSNotif.     PushNotif.  ...
Handler      Handler       Handler
```

## Design Patterns Used

| Pattern | Where | Why |
|---|---|---|
| **Strategy** | `NotificationHandler` interface with `Email`/`SMS`/`Push` implementations | Channel delivery behavior is interchangeable; router is unaware of concrete channels |
| **Registry** | `Map<NotificationType, NotificationHandler>` in `NotificationServiceImpl` | Handlers self-register their type via `getType()`; router looks up by type instead of if/else on channels |
| **Factory Method** | `createNotification(...)` on the service | Centralizes notification construction (ids, timestamps) |

## Design Decisions

1. **Handler registry, not if/else** — the router maps `NotificationType → handler`. Adding a new channel (e.g. WHATSAPP) means writing a new handler and adding it to the constructor list; **zero router changes** (Open/Closed Principle).
2. **Handlers self-describe** — `getType()` on the handler interface lets the router build its registry from any `List<NotificationHandler>`; the router never references concrete handler classes.
3. **`Notification` is immutable** — all fields `final`, set in constructor. One notification instance can be safely fanned out to all channels (and across threads) without mutation races.
4. **`sentTo` is a userId, not a `User` object** — entities reference each other by id (like FK columns), avoiding object-graph tangles and stale copies.
5. **Channel is NOT a field on `Notification`** — one URGENT notification goes to all channels, so a single `notificationType` field cannot represent reality. Channel is a routing decision owned by the router/handlers; in production it belongs on a separate `Delivery`/audit record.
6. **No singleton** — the router is a plain class. Two routers with different handler sets are valid; a singleton would block unit testing and bake the first caller's config in forever. Singleton belongs on resource holders (e.g. connection pools), not on stateless services.
7. **Thread-safety of `User` preferences** — all three preference methods are `synchronized`, and the getter returns a defensive copy (`Set.copyOf`). Writers can't corrupt the `HashSet`, readers get a consistent snapshot, and callers can't mutate internal state bypassing add/optOut. (Alternative: a concurrent set, trading snapshot consistency for lock-freedom.)
8. **Registry is `final` and built once in the constructor** — after construction the map is only read, so it's safely shared across threads without synchronization.
9. **`EnumMap` is a free optimization for enum keys** — array-backed by ordinal, faster and lighter than `HashMap` (either is correct).

## Future Extensions

- `Delivery` records (notificationId, channel, status, sentAt) for auditing and retries
- Async fan-out (thread pool / queue) for urgent notifications
- Rate limiting, retry with channel fallback per handler
- User preference persistence and a UserService for lookup by userId
