package com.project.iplant.robot;

public class Robot {
    private String address;
    private String mode;
    private String lastChecked;

    public Robot() {
        this.address = "";
        this.mode = "";
        this.lastChecked = "";
    }

    public Robot(String address, String mode, String lastChecked) {
        this.address = address;
        this.mode = mode;
        this.lastChecked = lastChecked;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(String lastChecked) {
        this.lastChecked = lastChecked;
    }
}
