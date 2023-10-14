package io.jenkins.plugins.actions.buildstepaction.descriptor;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.matrix.MatrixProject;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import net.sf.json.JSONObject;

public class BuildStepDescriptorImpl extends BuildStepDescriptor<Builder> {
    protected BuildStepDescriptorImpl() {

    }

    // @Extension
    // public static final AddFeedStatus.DescriptorImpl addFeedStatus;
    // static {
    // addFeedStatus = new AddFeedStatus.DescriptorImpl(AddFeedStatus.class);
    // }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return !MatrixProject.class.equals(jobType);
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) {
        req.bindJSON(this, json);
        save();
        return true;
    }

    public FormValidation doCheckPrefix(@QueryParameter final String prefix) {
        return FormValidation.validateRequired(prefix);
    }

    public ListBoxModel doFillDurationTypeItems() {
        ListBoxModel items = new ListBoxModel();

        items.add("Duration", "duration");
        items.add("Date", "date");
        return items;
    }

    public boolean isDurationSelected(String option) {
        return "duration".equals(option);
    }

}
