package io.jenkins.plugins.utils;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.enums.JobStatus;

import java.io.IOException;

/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/18
 * Time:18:13
 */

public class Utils {

    @NonNull
    public static EnvVars getEnvVars(Run<?, ?> run, TaskListener listener, JobStatus jobStatus) throws IOException, InterruptedException {
        Result result = run.getResult();
        EnvVars vars = run.getEnvironment(listener);
        if(result != null){
            vars.put("JOB_EXECUTE_STATUS", result.toString());
            vars.put("jobExecuteStatus", result.toString());
        }else {
            vars.put("JOB_EXECUTE_STATUS", jobStatus.name());
            vars.put("jobExecuteStatus", jobStatus.name());
        }

        vars.put("buildNo", vars.get("BUILD_NUMBER"));
        return vars;
    }
}
