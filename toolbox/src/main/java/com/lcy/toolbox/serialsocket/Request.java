package com.lcy.toolbox.serialsocket;


import android.os.Handler;
import android.os.Looper;

/**
 * Created by lcy on 2016/9/24.
 */

public class Request {
    private SSClient.Entry entry;
    private SSClient.Listener listener;
    private SSClient.ErrorListener errorListener;
    private int method;
    private Handler handler = new Handler(Looper.getMainLooper());

    public Request(int method, SSClient.Entry entry, SSClient.Listener listener, SSClient.ErrorListener errorListener) {
        this.method = method;
        this.entry = entry;
        this.listener = listener;
        this.errorListener = errorListener;
    }

    public SSClient.Entry getEntry() {
        return entry;
    }

    public void setEntry(SSClient.Entry entry) {
        this.entry = entry;
    }

    public SSClient.Listener getListener() {
        return listener;
    }

    public void setListener(SSClient.Listener listener) {
        this.listener = listener;
    }

    public SSClient.ErrorListener getErrorListener() {
        return errorListener;
    }

    public void setErrorListener(SSClient.ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void deliverResponse(final SSClient.Entry entry) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null)
                    listener.onResponse(entry);
            }
        });
    }

    public void deliverError(final SSError error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (errorListener != null)
                    errorListener.onErrorResponse(error);
            }
        });
    }
}
