package org.mangorage.tiab.api.common;

/**
 * Central Location for getting TIAB API
 */
public abstract class CommonTiabAPI {
    private static CommonTiabAPI API;

    public CommonTiabAPI() {
        if (API != null)
            throw new IllegalStateException("Cannot start Time in a bottle. org.mangorage.tiab.api.TiabAPI was already initialized, however someone initialized it again!.");
        API = this;
    }
}
