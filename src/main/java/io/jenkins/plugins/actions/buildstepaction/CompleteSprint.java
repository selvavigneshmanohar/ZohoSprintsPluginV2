package io.jenkins.plugins.actions.buildstepaction;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.SprintAPI;

public class CompleteSprint extends Builder {
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    @DataBoundConstructor
    public CompleteSprint(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new SprintAPI(prefix, listener, build).complete();
    }

    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_sprint_complete();
        }

    }
}
