package com.lcy.toolbox.serialsocket;

import android.os.Process;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by lcy on 2016/9/24.
 */

public class WorkDispatcher extends Thread {
    private LinkedBlockingDeque<Request> mQueue;
    private boolean mQuit = false;
    private SocketExector socketExector = new SocketExector();
    private SerialExector serialExector = new SerialExector();

    public WorkDispatcher(LinkedBlockingDeque<Request> mQueue) {
        this.mQueue = mQueue;
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        Request request;

        while (true) {

            try {
                request = mQueue.take();

            } catch (InterruptedException e) {
                if(mQuit)
                    break;

                continue;
            }

            try {

                SSClient.Entry response;
                if(request.getMethod() == SSClient.Method.UDP){
                    response = socketExector.performRequest(request);
                }else if(request.getMethod() == SSClient.Method.SERIAL_PORT)
                    response = serialExector.performRequest(request);
                else
                    continue;

                request.deliverResponse(response);

                request = null;

            }catch (SSError error){
                request.deliverError(error);
            }
        }
    }
}
