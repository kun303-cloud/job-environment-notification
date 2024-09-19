package io.jenkins.plugins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.plugins.enums.JobStatus;
import io.jenkins.plugins.utils.HttpClient;
import io.jenkins.plugins.utils.Utils;
import jenkins.tasks.SimpleBuildStep;
import lombok.Data;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;

/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/18
 * Time:16:43
 */
@Data
public class NotificationStep extends Builder implements SimpleBuildStep {

    private String body;

    @DataBoundConstructor
    public NotificationStep(String body) {
        this.body = body;
    }

    @Override
    public void perform(@NonNull Run<?, ?> run, @NonNull FilePath workspace, @NonNull EnvVars env, @NonNull Launcher launcher, @NonNull TaskListener listener) throws InterruptedException, IOException {
        var envVar = Utils.getEnvVars(run, listener, JobStatus.RUNNING);
        envVar.put("body", body);
        HttpClient.executeRequest(envVar);
    }


    @Symbol("notify")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        public FormValidation doCheckName(@QueryParameter String body) {
            if (body.isEmpty()) {
                return FormValidation.error(Messages.NotificationStep_notify_body_errors_empty());
            }
            return FormValidation.ok();
        }

        @NonNull
        @Override
        public String getDisplayName() {
            return Messages.NotificationStep_notify();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
