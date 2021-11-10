package br.com.vwco.chatbot.blip.datamart.integration;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

class BlipEvent {
    public String ownerIdentity;
    public Instant storageDate;
    public String category;
    public String action;
    public Map<String, String> extras;

    public BlipEvent() {
    }

    public BlipEvent(
            String ownerIdentity,
            Instant storageDate,
            String category,
            String action,
            Map<String, String> extras
    ) {
        this.ownerIdentity = ownerIdentity;
        this.storageDate = storageDate;
        this.category = category;
        this.action = action;
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "BlipEvent{" +
                "ownerIdentity='" + ownerIdentity + '\'' +
                ", storageDate=" + storageDate +
                ", category='" + category + '\'' +
                ", action='" + action + '\'' +
                ", extras=" + extras +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlipEvent blipEvent = (BlipEvent) o;
        return Objects.equals(ownerIdentity, blipEvent.ownerIdentity) &&
                Objects.equals(storageDate, blipEvent.storageDate) &&
                Objects.equals(category, blipEvent.category) &&
                Objects.equals(action, blipEvent.action) &&
                Objects.equals(extras, blipEvent.extras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerIdentity, storageDate, category, action, extras);
    }
}
