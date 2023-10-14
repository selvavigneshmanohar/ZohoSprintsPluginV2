package io.jenkins.plugins.actions;

import javax.annotation.CheckForNull;

import hudson.Extension;
import hudson.model.RootAction;

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
        return "https://sprints.zoho.com";
    }
}
