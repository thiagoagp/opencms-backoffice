package com.mscg.appstarter.server.util;

import java.io.File;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {

    private static final Logger LOG = LoggerFactory.getLogger(Settings.class);

    private static Configuration config;

    static {
        config = new CompositeConfiguration();
        File settingsFile = new File("./settings/settings.xml");
        ((CompositeConfiguration)config).addConfiguration(new SystemConfiguration());
        try {
            ((CompositeConfiguration)config).addConfiguration(new XMLConfiguration(settingsFile));
        } catch(Exception e) {
            LOG.warn("Cannot load configuration file", e);
        }
    }

    public static Configuration getConfig() {
        return config;
    }

}