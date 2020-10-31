package com.mec.rim.core;

import java.lang.reflect.Method;


public class ServerBeanDefination {
    private Object object;
    private Method method;

    public Object getObject() {

        return object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
//  private Class<?> klass;

//    private Map<String, Method> methodPool;
//
//    {
//        methodPool = new HashMap<>();
//    }

//    public void addMethod(Method method) {
//
//        String name = method.toString();
//        if (methodPool.containsKey(name)) {
//            return;
//        }
//        methodPool.put(name,method);
//    }

//    public Object getObject() {
//        if (object == null) {
//            try {
//                object = klass.newInstance();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return object;
//    }