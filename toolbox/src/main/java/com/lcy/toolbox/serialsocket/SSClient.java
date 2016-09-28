package com.lcy.toolbox.serialsocket;

/**
 * Created by lcy on 2016/9/24.
 */

public class SSClient {

    public RequestQueue newRequest(){
        RequestQueue queue = new RequestQueue();
        queue.start();

        return queue;
    }

    public static class Entry{
        public boolean needResponse = true;

        public String remoteIp;
        public int remotePort = 0;
        public int recvPort = 0;

        public String serial;

        public byte[] data;
        public int timeOut = 0;
        public int retryCount = 0;
    }

    public interface Method{
        int SERIAL_PORT = 0;//串口请求
        int UDP = 1;//网络请求
    }

    public interface Listener{
        public void onResponse(Entry response);
    }

    public interface ErrorListener{
        public void onErrorResponse(SSError error);
    }
}
