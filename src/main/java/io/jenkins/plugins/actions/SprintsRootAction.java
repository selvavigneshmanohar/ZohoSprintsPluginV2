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

    @CheckForNull
    @Override
    public String getIconFileName() {
        return Util.getSprintsIconByAuth();
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
