package com.lcy.toolbox.serialsocket;

/**
 * Created by lcy on 2016/9/24.
 */

public interface WorkExector {
    public SSClient.Entry performRequest(Request request) throws SSError;
}
