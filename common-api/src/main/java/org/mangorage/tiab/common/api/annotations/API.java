package org.mangorage.tiab.common.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@API(since = "7.0.2")
@Retention(RetentionPolicy.SOURCE)
public @interface API {
    @API(since = "7.0.2")
    String since();
}
