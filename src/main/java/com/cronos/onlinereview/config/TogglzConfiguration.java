package com.cronos.onlinereview.config;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

public class TogglzConfiguration implements TogglzConfig {
    private static final Logger logger = LoggerFactory.getLogger(TogglzConfiguration.class);

    @Value("#{'${togglz.roles}'.split(',')}")
    private List<String> roles;
    @Value("${togglz.role_key}")
    private String roleKey;

    public Class<? extends Feature> getFeatureClass() {
        return TogglzFeatures.class;
    }

    public StateRepository getStateRepository() {
        return new FileBasedStateRepository(new File("/tmp/features.properties"));
    }

    public UserProvider getUserProvider() {
        return () -> {
            HttpServletRequest request = HttpServletRequestHolder.get();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals(ConfigHelper.getV2jwtCookieName())) {
                        DecodedJWT jwt;
                        try {
                            jwt = AuthorizationHelper.validateJWTToken(c.getValue());
                        } catch (Exception e) {
                            return new SimpleFeatureUser("user", false);
                        }
                        Claim claim = jwt.getClaim(roleKey);
                        if (claim != null) {
                            for (String role : claim.asArray(String.class)) {
                                if (roles.contains(role)) {
                                    return new SimpleFeatureUser("admin", true);
                                }
                            }
                        }
                    }
                }
            }
            return new SimpleFeatureUser("user", false);
        };
    }
}
