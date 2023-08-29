package io.jenkins.plugins.actions;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;

import javax.annotation.CheckForNull;

import hudson.Extension;
import hudson.model.RootAction;
import io.jenkins.plugins.util.Util;

/**
 * @author selvavignesh.m
 * @version 1.0
 */
@Extension
public class SprintsRootAction implements RootAction {
    private static final String PLUGIN_RESOUCE_PATH = "/plugin/zohosprints/";

    @CheckForNull
    @Override
    public String getIconFileName() {
        return PLUGIN_RESOUCE_PATH + "sprints.svg";
    }

    @CheckForNull
    @Override
    public String getDisplayName() {
        return "Sprints";
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        return defaultIfEmpty(Util.getZSConnection().getServiceDomain(), "https://sprints.zoho.com");
    }
}
