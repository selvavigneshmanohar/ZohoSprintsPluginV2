package io.jenkins.plugins.actions.postbuild.descriptor;

import org.kohsuke.stapler.QueryParameter;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import io.jenkins.plugins.Util;

public class PostBuildDescriptor extends BuildStepDescriptor<Publisher> {

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }

    public FormValidation doCheckPrefix(@QueryParameter final String prefix) {
        return Util.validateRequired(prefix);
    }

    public FormValidation doCheckNote(@QueryParameter final String note) {
        return Util.validateRequired(note);
    }
}
