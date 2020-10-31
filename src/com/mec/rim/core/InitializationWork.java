package com.mec.rim.core;

import com.mec.util.PropertiesParse;

public class InitializationWork {

    public static void loadConfig(String propertyPath, Initialization object) {
        PropertiesParse.loadProperties(propertyPath);
        PropertiesParse pp = new PropertiesParse();
        String serverIp = pp.value("serverIp");
        if (serverIp != null && serverIp.length() > 0 ) {
            object.setIp(pp.value("serverIp"));
        }

        String serverPort = pp.value("serverPort");
        if (serverPort != null && serverPort.length() > 0 ) {
            object.setPort(Integer.valueOf(pp.value("serverPort")));
        }
    }

}
