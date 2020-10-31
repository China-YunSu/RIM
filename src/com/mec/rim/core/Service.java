package com.mec.rim.core;

import com.mec.util.ArgumentMaker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;

public class Service implements Runnable{
    private DataOutputStream dos;
    private DataInputStream dis;
    private Socket client;

    public Service(Socket client) {
        this.client = client;
    }

    private Object dealMessage(String message) throws InvocationTargetException,
            IllegalAccessException, NoSuchServerBean {
        String[] strings = message.split("&&", 2);
        int methodHash = Integer.valueOf(strings[0]);
        String argPool = strings[1];

        ServerBeanDefination serverBean = ServerBeanFactory.getServerBean(methodHash);
        if (serverBean == null) {
            throw new NoSuchServerBean();
        }

        Method method = serverBean.getMethod();
        Parameter[] parameterTypes = method.getParameters();
        Object[] values = getValues(parameterTypes, argPool);
        Object object = serverBean.getObject();

        return method.invoke(object, values);
    }

    public Object[] getValues( Parameter[] parameterTypes,String argPool) {
        Object[] values = null;
        if (parameterTypes.length > 0) {
            ArgumentMaker argumentMaker = new ArgumentMaker(argPool);
            values = new Object[parameterTypes.length];
            for (int index = 0; index < parameterTypes.length; index++) {
                Object value = argumentMaker.getVaule(parameterTypes[index].getName()
                        ,parameterTypes[index].getParameterizedType());
                values[index] = value;
            }
        } else {
            values = new Object[]{};
        }

        return values;
    }

    public void endServer() {
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

        if (client != null) {
            try {
                if(!client.isClosed()) {
                    client.close();
                }
            } catch (IOException ignored) {
            }finally {
                client = null;
            }
        }
    }


    @Override
    public void run() {
        try {
            dos = new DataOutputStream(client.getOutputStream());
            dis = new DataInputStream(client.getInputStream());

            String message = dis.readUTF();
            Object res = dealMessage(message);

            dos.writeUTF(ArgumentMaker.gson.toJson(res));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                dos.writeUTF("ERROR");
            } catch (IOException ioException) {
            }
        } finally {
            endServer();
        }
    }
}
