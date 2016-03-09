package org.bluebank;

import dagger.Module;
import dagger.Provides;
import org.apache.commons.configuration.Configuration;

import javax.inject.Named;
import javax.inject.Singleton;

import static org.bluebank.api.configuration.ConfigurationHelper.getConfiguration;

@Module
public class ConfigurationModule {

    @Provides
    @Singleton
    @Named("atm.properties")
    public Configuration provideConfiguration() {
        return getConfiguration("atm.properties");
    }
}
