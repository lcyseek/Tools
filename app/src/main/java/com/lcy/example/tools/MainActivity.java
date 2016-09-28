package com.lcy.example.tools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lcy.toolbox.serialsocket.Request;
import com.lcy.toolbox.serialsocket.RequestQueue;
import com.lcy.toolbox.serialsocket.SSClient;
import com.lcy.toolbox.serialsocket.SSError;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SSClient ssClient ;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openBox(View view) {
        final SSClient.Entry entry = new SSClient.Entry();
        entry.remoteIp = "192.168.1.12";
        entry.recvPort = 30000;
        entry.remotePort = 30000;
        entry.data = new byte[]{0x04,0x05,0x02,0x02,0x01};
        entry.timeOut = 3000;

        Request request = new Request(SSClient.Method.UDP,entry,new SSClient.Listener(){
            @Override
            public void onResponse(final SSClient.Entry response) {
                if(response == null){
                    System.out.println("response is null");
                    return;
                }
                Toast.makeText(MainActivity.this,bytesToHexString(response.data," "),Toast.LENGTH_LONG).show();
            }
        },new SSClient.ErrorListener(){
            @Override
            public void onErrorResponse(final SSError error) {
                System.out.println("SSError--->"+error.getMessage());
            }
        });

        queue.add(request);
    }

    public static String bytesToHexString(byte[] src,String split){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }

        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv.toUpperCase(Locale.getDefault()));
            stringBuilder.append(split);
        }

        return stringBuilder.toString();
    }

    public void initQueue(View view) {
        if(ssClient == null)
            ssClient = new SSClient();

        queue = ssClient.newRequest();
    }

    public void destory(View view) {
        if(queue != null)
            queue.stop();
    }
}
