package com.andikas.wang.data.local;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;

public final class UserPreferencesProto extends GeneratedMessageLite<UserPreferencesProto, UserPreferencesProto.Builder> implements UserPreferencesProtoOrBuilder {

    private UserPreferencesProto() {
        currencyCode_ = "IDR";
        language_ = "id";
        pin_ = "";
        theme_ = "SYSTEM";
    }

    public static final int IS_ONBOARDING_COMPLETE_FIELD_NUMBER = 1;
    private boolean isOnboardingComplete_;
    public boolean getIsOnboardingComplete() { return isOnboardingComplete_; }

    public static final int CURRENCY_CODE_FIELD_NUMBER = 2;
    private String currencyCode_;
    public String getCurrencyCode() { return currencyCode_; }

    public static final int LANGUAGE_FIELD_NUMBER = 3;
    private String language_;
    public String getLanguage() { return language_; }

    public static final int IS_FIN_UNLOCKED_FIELD_NUMBER = 4;
    private boolean isFinUnlocked_;
    public boolean getIsFinUnlocked() { return isFinUnlocked_; }

    public static final int IS_BIOMETRIC_ENABLED_FIELD_NUMBER = 5;
    private boolean isBiometricEnabled_;
    public boolean getIsBiometricEnabled() { return isBiometricEnabled_; }

    public static final int PIN_FIELD_NUMBER = 6;
    private String pin_;
    public String getPin() { return pin_; }

    public static final int THEME_FIELD_NUMBER = 7;
    private String theme_;
    public String getTheme() { return theme_; }

    public static final int BUBBLE_POSITION_X_FIELD_NUMBER = 8;
    private float bubblePositionX_;
    public float getBubblePositionX() { return bubblePositionX_; }

    public static final int BUBBLE_POSITION_Y_FIELD_NUMBER = 9;
    private float bubblePositionY_;
    public float getBubblePositionY() { return bubblePositionY_; }

    public static UserPreferencesProto getDefaultInstance() { return DEFAULT_INSTANCE; }
    private static final UserPreferencesProto DEFAULT_INSTANCE;
    static {
        DEFAULT_INSTANCE = new UserPreferencesProto();
        GeneratedMessageLite.registerDefaultInstance(UserPreferencesProto.class, DEFAULT_INSTANCE);
    }

    public static Builder newBuilder() { return DEFAULT_INSTANCE.createBuilder(); }

    public static UserPreferencesProto parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    @Override
    protected final Object dynamicMethod(MethodToInvoke method, Object arg0, Object arg1) {
        switch (method) {
            case NEW_MUTABLE_INSTANCE: return new UserPreferencesProto();
            case NEW_BUILDER: return new Builder();
            case BUILD_MESSAGE_INFO:
                return newMessageInfo(DEFAULT_INSTANCE,
                        "\u0000\t\u0000\u0000\u0001\t\t\u0000\u0000\u0000\u0001\u0007\u0002\u0208\u0003\u0208\u0004\u0007\u0005\u0007\u0006\u0208\u0007\u0208\u0008\u0001\t\u0001",
                        new Object[] {
                                "isOnboardingComplete_", "currencyCode_", "language_", "isFinUnlocked_",
                                "isBiometricEnabled_", "pin_", "theme_", "bubblePositionX_", "bubblePositionY_"
                        });
            case GET_DEFAULT_INSTANCE: return DEFAULT_INSTANCE;
            case GET_PARSER:
                Parser<UserPreferencesProto> parser = PARSER;
                if (parser == null) {
                    synchronized (UserPreferencesProto.class) {
                        parser = PARSER;
                        if (parser == null) {
                            parser = new DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                            PARSER = parser;
                        }
                    }
                }
                return parser;
            case GET_MEMOIZED_IS_INITIALIZED: return (byte) 1;
            case SET_MEMOIZED_IS_INITIALIZED: return null;
        }
        throw new UnsupportedOperationException();
    }

    private static volatile Parser<UserPreferencesProto> PARSER;

    public static final class Builder extends GeneratedMessageLite.Builder<UserPreferencesProto, Builder> implements UserPreferencesProtoOrBuilder {
        private Builder() { super(DEFAULT_INSTANCE); }

        public boolean getIsOnboardingComplete() { return instance.getIsOnboardingComplete(); }
        public Builder setIsOnboardingComplete(boolean value) { copyOnWrite(); instance.isOnboardingComplete_ = value; return this; }

        public String getCurrencyCode() { return instance.getCurrencyCode(); }
        public Builder setCurrencyCode(String value) { copyOnWrite(); instance.currencyCode_ = value; return this; }

        public String getLanguage() { return instance.getLanguage(); }
        public Builder setLanguage(String value) { copyOnWrite(); instance.language_ = value; return this; }

        public boolean getIsFinUnlocked() { return instance.getIsFinUnlocked(); }
        public Builder setIsFinUnlocked(boolean value) { copyOnWrite(); instance.isFinUnlocked_ = value; return this; }

        public boolean getIsBiometricEnabled() { return instance.getIsBiometricEnabled(); }
        public Builder setIsBiometricEnabled(boolean value) { copyOnWrite(); instance.isBiometricEnabled_ = value; return this; }

        public String getPin() { return instance.getPin(); }
        public Builder setPin(String value) { copyOnWrite(); instance.pin_ = value; return this; }

        public String getTheme() { return instance.getTheme(); }
        public Builder setTheme(String value) { copyOnWrite(); instance.theme_ = value; return this; }

        public float getBubblePositionX() { return instance.getBubblePositionX(); }
        public Builder setBubblePositionX(float value) { copyOnWrite(); instance.bubblePositionX_ = value; return this; }

        public float getBubblePositionY() { return instance.getBubblePositionY(); }
        public Builder setBubblePositionY(float value) { copyOnWrite(); instance.bubblePositionY_ = value; return this; }
    }
}
