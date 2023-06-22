package io.jenkins.plugins.actions;

import hudson.Extension;
import hudson.model.RootAction;
import io.jenkins.plugins.configuration.ZSConnectionConfiguration;
import io.jenkins.plugins.util.Util;
import jenkins.model.Jenkins;
import javax.annotation.CheckForNull;
import java.util.List;
import static org.apache.commons.lang.StringUtils.defaultIfEmpty;

/**
 * @author selvavignesh.m
 * @version 1.0
 */
@Extension
public class SprintsRootAction implements RootAction {

    /**
     *
     * @return Root Action IconFileName
     */
    @CheckForNull
    @Override
    public String getIconFileName() {
        return Util.getSprintsIconByAuth();
    }

    /**
     *
     * @return Root Action Dosplay Name
     */
    @CheckForNull
    @Override
    public String getDisplayName() {
        return "Sprints";
    }

    /**
     *
     * @return Root Action url
     */
    @CheckForNull
    @Override
    public String getUrlName() {
        List<ZSConnectionConfiguration> extnList = Jenkins.getInstance()
                .getExtensionList(ZSConnectionConfiguration.class);
        ZSConnectionConfiguration conf = extnList.get(0);
        return defaultIfEmpty(conf.getServiceDomain(), "https://sprints.zoho.com");
    }
}
