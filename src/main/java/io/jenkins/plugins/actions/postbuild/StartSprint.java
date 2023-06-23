package io.jenkins.plugins.actions.postbuild;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.matrix.MatrixAggregatable;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.PostBuildDescriptor;
import io.jenkins.plugins.api.SprintAPI;

public class StartSprint extends Recorder implements MatrixAggregatable {
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    @DataBoundConstructor
    public StartSprint(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        if (build instanceof MatrixRun) {
            return true;
        }
        return _perform(build, launcher, listener);
    }

    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new SprintAPI(prefix, listener, build).start();
    }

    @Override
    public MatrixAggregator createAggregator(MatrixBuild build, Launcher launcher, BuildListener listener) {
        return new MatrixAggregator(build, launcher, listener) {
            @Override
            public boolean endBuild() throws InterruptedException, IOException {
                return StartSprint.this._perform(this.build, this.launcher, this.listener);
            }

            @Override
            public boolean startBuild() throws InterruptedException, IOException {
                return true;
            }
        };
    }

    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_sprint_start();
        }
    }

}
