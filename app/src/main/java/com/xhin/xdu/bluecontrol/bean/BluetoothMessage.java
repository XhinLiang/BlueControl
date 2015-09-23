package com.xhin.xdu.bluecontrol.bean;

/**
 * Created by xhinliang on 15-9-17.
 * xhinliang@gmail.com
 */
public class BluetoothMessage {
    int ascCode;
    String message;
    boolean success;

    public BluetoothMessage(int ascCode, String message) {
        this.ascCode = ascCode;
        this.message = message;
    }

    public int getAscCode() {
        return ascCode;
    }

    public void setAscCode(int ascCode) {
        this.ascCode = ascCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
