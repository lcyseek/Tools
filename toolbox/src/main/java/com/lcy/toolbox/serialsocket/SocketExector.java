package com.lcy.toolbox.serialsocket;

import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by lcy on 2016/9/24.
 */

public class SocketExector implements WorkExector {

    private DatagramSocket sendSocket;
    private DatagramSocket recvSocket;
    private DatagramPacket sendPacket;
    private DatagramPacket recvPacket;
    private SSClient.Entry entry;
    private byte[] buffer = new byte[1024];

    public SocketExector() {

    }

    @Override
    public SSClient.Entry performRequest(Request request) throws SSError {
        int currRetryCount = 0;

        if (request == null)
            return null;

        SSClient.Entry entry = request.getEntry();

        try {

            if (sendSocket == null || sendSocket.isClosed()) {
                sendSocket = new DatagramSocket();
            }

            if (sendPacket == null) {
                sendPacket = new DatagramPacket(entry.data, entry.data.length, InetAddress.getByName(entry.remoteIp), entry.remotePort);
            } else
                sendPacket.setData(entry.data, 0, entry.data.length);

            if (entry.needResponse) {

                if (recvSocket == null || recvSocket.isClosed()) {
                    recvSocket = new DatagramSocket(null);
                    recvSocket.setReuseAddress(true);
                    recvSocket.bind(new InetSocketAddress(entry.recvPort));
                }

                recvSocket.setSoTimeout(entry.timeOut);
                recvPacket = new DatagramPacket(buffer, 0, buffer.length);
            }
        } catch (SocketException e) {
            throw new SSError(e.getMessage());
        } catch (UnknownHostException e) {
            throw new SSError(e.getMessage());
        }


        while (currRetryCount <= entry.retryCount) {
            try {
                ++currRetryCount;
                sendSocket.send(sendPacket);

                if (entry.needResponse) {
                    recvSocket.receive(recvPacket);

                    SSClient.Entry response = obtainEntry();
                    response.remoteIp = recvPacket.getAddress().getHostAddress();
                    response.recvPort = recvPacket.getPort();
                    response.data = Arrays.copyOf(recvPacket.getData(), recvPacket.getLength());
                    return response;
                }

            } catch (SocketTimeoutException e) {
                if (currRetryCount++ > request.getEntry().retryCount)
                    throw new SSError(e.getMessage());
            } catch (IOException e) {
                throw new SSError(e.getMessage());
            }
        }

        return null;
    }

    public SSClient.Entry obtainEntry() {

        if (entry == null)
            entry = new SSClient.Entry();

        entry.recvPort = 0;
        entry.remotePort = 0;
        entry.data = null;
        entry.serial = "";
        entry.timeOut = 0;
        entry.retryCount = 0;

        return entry;
    }
}
