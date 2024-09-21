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
import io.jenkins.plugins.enums.JobStatus;
import io.jenkins.plugins.utils.HttpClient;
import io.jenkins.plugins.utils.Utils;
import jenkins.tasks.SimpleBuildStep;
import lombok.Data;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

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
        try {
            HttpClient.executeRequest(envVar);
        } catch (Exception e) {
            listener.getLogger().println(e);
        }
    }


    @Symbol("notify")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

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
