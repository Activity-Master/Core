# Private actor events

Authenticated hosts append an actor-attributed event in their existing stateless
transaction with `IEventService.createActorEvent`. The host resolves the actor
and credential server-side. The event, type link and CreatedBy link use restricted
security and the actor's read scope. Security failures abort the transaction.
Titles describe actions and must not contain private field values.

```mermaid
sequenceDiagram
    Host->>EventService: Transaction, verified party, credential, title
    EventService->>FSDM: Insert event and private type and CreatedBy links
    EventService-->>Host: All security and audit writes complete
    Host->>FSDM: Commit domain mutation and event together
```
