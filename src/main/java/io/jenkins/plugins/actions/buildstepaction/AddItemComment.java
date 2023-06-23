package io.jenkins.plugins.actions.buildstepaction;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.ItemAPI;

public class AddItemComment extends Builder {
    private String prefix, note;

    public String getPrefix() {
        return prefix;
    }

    public String getNote() {
        return note;
    }

    @DataBoundConstructor
    public AddItemComment(String prefix, String note) {
        this.prefix = prefix;
        this.note = note;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ItemAPI.ItemActionBuilder(prefix, build, listener)
                .withComment(note)
                .build()
                .addComment();
    }

    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        public FormValidation doCheckNote(@QueryParameter final String note) {
            if (StringUtils.isEmpty(note)) {
                return FormValidation.validateRequired(note);
            }
            return FormValidation.ok();
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.add_item_comment();
        }

    }
}
