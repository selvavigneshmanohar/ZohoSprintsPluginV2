package io.jenkins.plugins.actions;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;

public class PostBuildDescriptor extends BuildStepDescriptor<Publisher> {

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
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
}
