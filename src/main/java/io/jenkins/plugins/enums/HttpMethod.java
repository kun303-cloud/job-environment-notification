package io.jenkins.plugins.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/18
 * Time:17:23
 */
@Getter
@AllArgsConstructor
public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;
}
