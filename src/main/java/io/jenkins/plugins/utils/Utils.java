package io.jenkins.plugins.utils;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.JobRunListenerSysConfig;
import io.jenkins.plugins.enums.ConstantsEnums;
import io.jenkins.plugins.enums.JobStatus;
import io.jenkins.plugins.model.HttpHeader;
import jenkins.model.GlobalConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.jenkins.plugins.enums.Constants.PASSWORD;

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
        if (result != null) {
            vars.put(ConstantsEnums.JOB_EXECUTE_STATUS.name(), result.toString());
            vars.put(ConstantsEnums.JOB_EXECUTE_STATUS.getLowCase(), result.toString());
        } else {
            vars.put(ConstantsEnums.JOB_EXECUTE_STATUS.name(), jobStatus.name());
            vars.put(ConstantsEnums.JOB_EXECUTE_STATUS.getLowCase(), jobStatus.name());
        }

        vars.put(ConstantsEnums.BUILD_NUMBER.getLowCase(), vars.get(ConstantsEnums.BUILD_NUMBER.name()));
        return vars;
    }

    public static void encoderPassword(List<HttpHeader> headers) {
        if(null != headers && !headers.isEmpty()){
            var sysConfig = GlobalConfiguration.all().get(JobRunListenerSysConfig.class);
            Map<String, String> oldPassword = new HashMap<>();
            if(null != sysConfig){
                var oldHeaders = sysConfig.getHttpHeaders();
                for (var header : oldHeaders) {
                    if (null != header.getHeaderKey() && PASSWORD.equals(header.getHeaderKey())) {
                        oldPassword.put(PASSWORD,header.getHeaderValue());
                    }
                }
            }

            headers.forEach(header -> {
                if (null != header.getHeaderKey() && PASSWORD.equals(header.getHeaderKey())) {
                    final Base64.Encoder encoder = Base64.getEncoder();
                    final var pass = header.getHeaderValue();
                    if(null != pass && !pass.isEmpty()){
                        final var passEncoder = encoder.encodeToString(pass.getBytes(StandardCharsets.UTF_8));
                        header.setHeaderValue(passEncoder);
                    }else {
                        if(!oldPassword.isEmpty()){
                            header.setHeaderValue(oldPassword.get(PASSWORD));
                        }
                    }
                }
            });
        }

    }

    public static void decoderPassword(List<HttpHeader> headers) {
        if(null != headers && !headers.isEmpty()){
            headers.forEach(header -> {
                if (null != header.getHeaderKey() && PASSWORD.equals(header.getHeaderKey())) {
                    final Base64.Decoder decoder = Base64.getDecoder();
                    final var pass = header.getHeaderValue();
                    if(null != pass && !pass.isEmpty()){
                        final var passDecoder = decoder.decode(pass);
                        header.setHeaderValue(new String(passDecoder, StandardCharsets.UTF_8));
                    }

                }
            });
        }

    }
}
