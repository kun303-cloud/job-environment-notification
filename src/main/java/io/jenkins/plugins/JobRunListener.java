package io.jenkins.plugins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import io.jenkins.plugins.enums.JobStatus;
import io.jenkins.plugins.utils.HttpClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static io.jenkins.plugins.utils.Utils.getEnvVars;


/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/13
 * Time:18:30
 */
@Extension
@Slf4j
public class JobRunListener extends RunListener<Run<?, ?>> {

    @Override
    public void onCompleted(Run<?, ?> run, @NonNull TaskListener listener) {
        sendStatus(run, listener,JobStatus.COMPLETE);
    }

    @Override
    public void onStarted(Run<?, ?> run, TaskListener listener) {
        sendStatus(run, listener,JobStatus.START);
    }



    private static void sendStatus(Run<?, ?> run, TaskListener listener, JobStatus jobStatus) {
        try {
            log.info("Pipeline Status Notification job status : {} ",jobStatus);
            EnvVars vars = getEnvVars(run, listener,jobStatus);
            log.info("Pipeline Status Notification send values : {}",vars);
            HttpClient.executeRequest(vars);
        } catch (Exception e) {
            listener.getLogger().println("Pipeline Status Notification send job status error:"+e.getMessage());
        }
    }


}
