package io.jenkins.plugins.model;

import lombok.Data;
import lombok.ToString;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/14
 * Time:11:39
 */


@Data
@ToString
public class HttpHeader {

    private  String headerKey;
    private  String headerValue;


    @DataBoundConstructor
    public HttpHeader(String headerKey, String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
    }
}
