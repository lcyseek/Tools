package com.lcy.toolbox.serialsocket;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by lcy on 2016/9/24.
 */

public class RequestQueue {
    private LinkedBlockingDeque<Request> mQueue = new LinkedBlockingDeque<>();
    private WorkDispatcher workDispatcher = new WorkDispatcher(mQueue);

    public void add(Request request){
        mQueue.add(request);
    }

    public void start(){
        workDispatcher.start();
    }

    public void stop(){
        workDispatcher.quit();
    }
}
