package com.cronos.onlinereview.config;

import com.cronos.onlinereview.util.AuthorizationHelper;
import com.topcoder.onlinereview.component.security.RolePrincipal;
import com.topcoder.onlinereview.component.security.login.LoginBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.togglz.core.Feature;
import org.togglz.core.manager.TogglzConfig;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.file.FileBasedStateRepository;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;
import org.togglz.servlet.util.HttpServletRequestHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Set;

public class TogglzConfiguration implements TogglzConfig {
    private static final Logger logger = LoggerFactory.getLogger(TogglzConfiguration.class);

    @Value("#{'${togglz.roles}'.split(',')}")
    private List<String> roles;

    public Class<? extends Feature> getFeatureClass() {
        return TogglzFeatures.class;
    }

    public StateRepository getStateRepository() {
        return new FileBasedStateRepository(new File("/tmp/features.properties"));
    }

    public UserProvider getUserProvider() {
        return () -> {
            HttpServletRequest request = HttpServletRequestHolder.get();
            AuthorizationHelper.gatherUserRoles(request);
            Set<String> roles = (Set<String>)request.getAttribute("roles");
            for (String role : roles) {
                if (roles.contains(role)) {
                    return new SimpleFeatureUser("admin", true);
                }
            }
            return new SimpleFeatureUser("user", false);
        };
    }
}
