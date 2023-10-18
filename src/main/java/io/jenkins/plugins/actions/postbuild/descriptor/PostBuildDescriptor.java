package io.jenkins.plugins.actions.postbuild.descriptor;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import io.jenkins.plugins.util.Util;
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
        return Util.validateRequired(prefix);
    }

    public FormValidation doCheckNote(@QueryParameter final String note) {
        return Util.validateRequired(note);
    }
}
