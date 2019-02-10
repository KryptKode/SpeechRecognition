package com.text.speech.data;

public interface Repository {
    boolean isSetUpComplete();
    void updateSetUp(boolean value);
}
