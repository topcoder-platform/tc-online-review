package com.cronos.onlinereview.config;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum TogglzFeatures implements Feature {

    @EnabledByDefault
    @Label("Send kafka message")
    SEND_KAFKA_MESSAGE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
