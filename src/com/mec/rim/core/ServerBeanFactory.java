package com.mec.rim.core;

import com.mec.util.PackageScan;
import com.mec.util.XMLParse;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ServerBeanFactory {
    private static Map<Integer,ServerBeanDefination> serverBeanPool;

    static {
        serverBeanPool = new HashMap<>();
    }

    public static void ScannServerMapping(String xmlPath) {
        new XMLParse() {

            @Override
            public void dealElement(Element element, int i) {
                String interfaceName = element.getAttribute("name");
                String className = element.getAttribute("class");
                try {
                    Class<?> klass = Class.forName(className);
                    Class<?> interfaceClass = Class.forName(interfaceName);
                    setServerBean(klass,interfaceClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.parseTagByDocument(XMLParse.getDocument(xmlPath),"interface");
    }

    public static void setServerBean(Class<?> klass, Class<?> interfaceClass) {
        try {
            for (Method interfaceMethod : interfaceClass.getDeclaredMethods()) {
                ServerBeanDefination sbd = new ServerBeanDefination();
                sbd.setObject(klass.newInstance());
                int key = interfaceMethod.hashCode();
                Method method = klass.getDeclaredMethod(interfaceMethod.getName(),
                        interfaceMethod.getParameterTypes());
                sbd.setMethod(method);
                serverBeanPool.put(key,sbd);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    public static ServerBeanDefination getServerBean(int hashCode) {
        return serverBeanPool.get(hashCode);
    }

}

 //                   for (Method method : interfaceClass.getDeclaredMethods()) {
//                        System.out.println(method);
//                        serverBeanDefination.addMethod(method);
//                    }
//                    serverBeanDefination.setKlass(klass);