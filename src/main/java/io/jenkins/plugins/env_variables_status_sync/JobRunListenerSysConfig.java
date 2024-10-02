package io.jenkins.plugins.env_variables_status_sync;

import hudson.Extension;
import io.jenkins.plugins.env_variables_status_sync.enums.HttpMethod;
import io.jenkins.plugins.env_variables_status_sync.model.HttpHeader;
import jenkins.model.GlobalConfiguration;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.List;

import static io.jenkins.plugins.env_variables_status_sync.enums.Constants.*;
import static io.jenkins.plugins.env_variables_status_sync.utils.Utils.encoderPassword;

/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/14
 * Time:11:41
 */


@Getter
@Extension
@ToString
@Slf4j
public class JobRunListenerSysConfig extends GlobalConfiguration {

    public JobRunListenerSysConfig() {
        load();
    }

    private String requestUrl;

    private List<HttpHeader> httpHeaders;

    private HttpMethod requestMethod;



    @Override
    public boolean configure(StaplerRequest req, JSONObject json) {
        // 从json对象中读取并设置requestUrl
        requestUrl = json.getString(FORM_KEY_REQUEST_URL);
        setRequestUrl(requestUrl);
        // 读取并设置httpHeaders
        httpHeaders = req.bindJSONToList(HttpHeader.class, json.get(FORM_KEY_REQUEST_HEADERS));
        setHttpHeaders(httpHeaders);
        String requestMethodOption = json.getString(FORM_KEY_REQUEST_METHOD);
        setRequestMethod(HttpMethod.valueOf(requestMethodOption));
        save(); // 保存配置


        return true;  // 配置成功
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        save();
    }

    public void setHttpHeaders(List<HttpHeader> httpHeaders) {
        encoderPassword(httpHeaders);
        this.httpHeaders = httpHeaders;
        save();
    }

    public void setRequestMethod(HttpMethod requestMethod) {
        this.requestMethod = requestMethod;
        save();
    }


}
