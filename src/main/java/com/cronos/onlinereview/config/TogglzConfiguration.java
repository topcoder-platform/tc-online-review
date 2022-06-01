package com.cronos.onlinereview.config;

import org.togglz.core.Feature;
import org.togglz.core.manager.TogglzConfig;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.file.FileBasedStateRepository;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;

import java.io.File;

public class TogglzConfiguration implements TogglzConfig {

    public Class<? extends Feature> getFeatureClass() {
        return TogglzFeatures.class;
    }

    public StateRepository getStateRepository() {
        return new FileBasedStateRepository(new File("/tmp/features.properties"));
    }

    public UserProvider getUserProvider() {
        return () -> new SimpleFeatureUser("admin", true);
    }
}
