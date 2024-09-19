package io.jenkins.plugins;

import hudson.Extension;
import io.jenkins.plugins.enums.HttpMethod;
import io.jenkins.plugins.model.HttpHeader;
import jenkins.model.GlobalConfiguration;
import lombok.Getter;
import lombok.ToString;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.List;

/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/14
 * Time:11:41
 */


@Getter
@Extension
@ToString
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
        requestUrl = json.getString("requestUrl");
        setRequestUrl(requestUrl);
        // 读取并设置httpHeaders
        httpHeaders = req.bindJSONToList(HttpHeader.class, json.get("httpHeaders"));
        setHttpHeaders(httpHeaders);
        String requestMethodOption = json.getString("requestMethod");
        setRequestMethod(HttpMethod.valueOf(requestMethodOption));
        save(); // 保存配置


        return true;  // 配置成功
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        save();
    }

    public void setHttpHeaders(List<HttpHeader> httpHeaders) {
        this.httpHeaders = httpHeaders;
        save();
    }

    public void setRequestMethod(HttpMethod requestMethod) {
        this.requestMethod = requestMethod;
        save();
    }


}
