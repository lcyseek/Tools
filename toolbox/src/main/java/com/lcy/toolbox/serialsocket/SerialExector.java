package com.lcy.toolbox.serialsocket;

/**
 * Created by lcy on 2016/9/24.
 */

public class SerialExector implements WorkExector {
    @Override
    public SSClient.Entry performRequest(Request request) throws SSError {
        int currRetryCount = 0;

        if (request == null)
            return null;

        SSClient.Entry entry = request.getEntry();

        openSerial(entry.serial);
        closeSerial(entry.serial);
        return null;
    }

    private void closeSerial(String serial) {

    }

    private void openSerial(String serial) {

    }
}
