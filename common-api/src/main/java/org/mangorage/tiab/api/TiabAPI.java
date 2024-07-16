package org.mangorage.tiab.api;

public abstract class TiabAPI {
    private static TiabAPI API;

    public TiabAPI() {
        if (API != null)
            throw new IllegalStateException("Cannot start Time in a bottle. org.mangorage.tiab.api.TiabAPI was already initialized, however someone initialized it again!.");
        API = this;
    }
}
