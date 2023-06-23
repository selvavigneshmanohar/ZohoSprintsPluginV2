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
import io.jenkins.plugins.api.ItemAPI;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.PostBuildDescriptor;

public class UpdateWorkItem extends Recorder implements MatrixAggregatable {
    private String prefix, name, description, status, type, priority, duration, startdate, enddate, customFields;

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getPriority() {
        return priority;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getCustomFields() {
        return customFields;
    }

    @DataBoundConstructor
    public UpdateWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String startdate, String enddate, String customFields) {
        this.prefix = prefix;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.priority = priority;
        this.duration = duration;
        this.startdate = startdate;
        this.enddate = enddate;
        this.customFields = customFields;
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
        return new ItemAPI.ItemActionBuilder(prefix, build, listener)
                .withName(name)
                .withDescription(description)
                .withStatus(status)
                .withPriority(priority)
                .withType(type)
                .withDuration(duration)
                .withComment(startdate)
                .withEnddate(enddate)
                .withCustomFields(customFields)
                .build().update();
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public MatrixAggregator createAggregator(MatrixBuild build, Launcher launcher, BuildListener listener) {
        return new MatrixAggregator(build, launcher, listener) {
            @Override
            public boolean endBuild() throws InterruptedException, IOException {
                return UpdateWorkItem.this._perform(this.build, this.launcher, this.listener);
            }

            @Override
            public boolean startBuild() throws InterruptedException, IOException {
                return true;
            }
        };
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_item();
        }

    }
}
