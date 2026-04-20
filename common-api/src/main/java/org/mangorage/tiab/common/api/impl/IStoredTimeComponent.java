package org.mangorage.tiab.common.api.impl;

import org.mangorage.tiab.common.api.annotations.API;

@API(since = "7.0.2")
public interface IStoredTimeComponent {
    @API(since = "7.0.0")
    int stored();

    @API(since = "7.0.0")
    int total();
}
