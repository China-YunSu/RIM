package com.mec.rim.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIServer extends InitializtionAdptor implements Runnable,ISpeaker{
    private int port;
    private static final int DEFAULT_PORT = 54188;
    private ServerSocket serverSocket;
    private final List<IListener> listeners;
    private volatile boolean goon;
    private ExecutorService threadPool;

    public RMIServer() {
        this(DEFAULT_PORT);
    }


    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public RMIServer(int port) {
        listeners = new LinkedList<>();
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void startUp() {
        try {
            publishMessage("服务器开始启动......");
            serverSocket =  new ServerSocket(port);
            publishMessage("服务器启动成功");
            goon = true;
            if (threadPool == null) {
                threadPool = Executors.newCachedThreadPool();
            }
            new Thread(this,"服务器").start();
        } catch (IOException e) {
            stop();
        }
    }

    public void shutDown() {
        if (!goon) {
            publishMessage("服务器已关闭");
            return;
        }
        stop();
    }

    public void stop() {
        if (!goon) {
            return;
        }

        try {
            goon = false;
            threadPool.shutdown();
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        } finally {
            serverSocket = null;
        }
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }


    public void initWork(String configPath) {
        InitializationWork.loadConfig(configPath,this);
    }

    @Override
    public void run() {
        publishMessage("开始侦听客户端连接请求.....");
        while (goon) {
            try {
                Socket client = serverSocket.accept();
                publishMessage("客户端[ " + client.getInetAddress().getHostAddress() +
                        " ]连接请求");
                threadPool.submit(new Service(client));
            } catch (IOException e) {
                stop();
            }
        }
        publishMessage("服务器关闭成功！");
    }

    @Override
    public void publishMessage(String message) {
        for (IListener listener : listeners) {
            listener.listenMessage(message);
        }
    }

    @Override
    public void addListener(IListener listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    @Override
    public void removeListener(IListener listener) {
        listeners.remove(listener);
    }
}
