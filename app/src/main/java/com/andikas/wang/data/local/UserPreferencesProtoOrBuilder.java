package com.andikas.wang.data.local;

import com.google.protobuf.MessageLiteOrBuilder;

public interface UserPreferencesProtoOrBuilder extends MessageLiteOrBuilder {
    boolean getIsOnboardingComplete();
    String getCurrencyCode();
    String getLanguage();
    boolean getIsFinUnlocked();
    boolean getIsBiometricEnabled();
    String getPin();
    String getTheme();
    float getBubblePositionX();
    float getBubblePositionY();
}
