package com.mec.rim.core;

public interface ISpeaker {
    void publishMessage(String message);
    void addListener(IListener listener);
    void removeListener(IListener listener);
}
