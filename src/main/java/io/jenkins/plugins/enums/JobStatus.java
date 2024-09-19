package io.jenkins.plugins.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/19
 * Time:12:18
 */
@Getter
@AllArgsConstructor
public enum JobStatus {
    START,
    RUNNING,
    COMPLETE;
}
