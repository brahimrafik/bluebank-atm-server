package org.bluebank.api.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.io.Resources.getResource;

public final class ConfigurationHelper {

    private ConfigurationHelper() {
    }

    public static Configuration getConfiguration(final String path) {
        checkNotNull(path);
        final URL url = getResource(path);
        try {
            final PropertiesConfiguration configuration = new PropertiesConfiguration(url);
            configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
            configuration.refresh();
            return configuration;
        } catch (ConfigurationException e) {
            throw propagate(e);
        }
    }
}
