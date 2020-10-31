package com.mec.rim.core;

import java.lang.reflect.Proxy;

public class ClientProxy {
    private final Client client;

    public ClientProxy(Client client) {
        this.client = client;
    }

    public Object getProxy(Class<?> klass) {
        Class<?>[] interfaces = new Class[]{klass};
        return Proxy.newProxyInstance(klass.getClassLoader(), interfaces, (proxy, method, args) -> {
            if (client.action == null) {
                return client.methodInvoke(method, args);
            }
            client.action.doActionBefore();
            Object res = client.methodInvoke(method, args);
            client.action.doActionAfter();
            return res;
        });
    }


}
