package com.mec.rim.core;

import com.mec.util.ArgumentMaker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Socket;

public class RMIClient extends InitializtionAdptor {
    private String serverip;
    private int port;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Socket server;
    IClientAction action;

    public void setAction(IClientAction action) {
        this.action = action;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> klass) throws  NotInterfaceClassException {
        if (!klass.isInterface()) {
            throw new NotInterfaceClassException();
        }
        ClientProxy clientProxy = new ClientProxy(this);
        return (T) clientProxy.getProxy(klass);
    }

    public String getServerip() {
        return serverip;
    }

    public int getPort() {
        return port;
    }

    public void initWork(String configPath) {
        InitializationWork.loadConfig(configPath,this);
    }

    public Object methodInvoke(Method method, Object[] args) {
        Object result = null;
        try {
            server = new Socket(serverip,port);
            dos = new DataOutputStream(server.getOutputStream());
            dis = new DataInputStream(server.getInputStream());

            String message = method.hashCode() + "&&" +
                    getParameterValues(method, args);
            dos.writeUTF(message);
            result = dealReturnValue(dis.readUTF(),method.getGenericReturnType());
        } catch (IOException ignored) {
        } finally {
            close();
        }

        return result;
    }

    private Object dealReturnValue(String returnMessage,Type type) {
        if (returnMessage.equals("null")) {
            return null;
        }

        if (returnMessage.equals("ERROR")) {
            throw new MethodInvokException();
        }

        return ArgumentMaker.gson.fromJson(returnMessage, type);
    }


    private String getParameterValues(Method method,Object[] args) {
        if (args == null) {
            return "";
        }

        ArgumentMaker argumentMaker = new ArgumentMaker();
        int length = method.getParameterTypes().length;
        for (int i = 0; i < length; i++) {
            argumentMaker.add("arg" + i, args[i]);
        }

        return argumentMaker.toString();
    }

    @Override
    public void setIp(String ip) {
        serverip = ip;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    public void close() {
        if (dos != null) {
            try {
                dos.close();
            } catch (IOException ignored) {
            }finally {
                dos = null;
            }
        }

        if (dis != null) {
            try {
                dis.close();
            } catch (IOException ignored) {
            } finally {
                dis = null;
            }
        }

        if (server != null) {
            try {
                if(!server.isClosed()) {
                    server.close();
                }
            } catch (IOException ignored) {
            }finally {
                server = null;
            }
        }
    }
}
