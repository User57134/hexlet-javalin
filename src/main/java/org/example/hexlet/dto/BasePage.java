package org.example.hexlet.dto;


import lombok.Getter;


@Getter
public class BasePage {
    private String flash;
    private String status;

    public void setFlash(String message, String messageStatus) {
        if (messageStatus == null) {
            setFlash(message);
        } else {
            flash = message;
            status = messageStatus;
        }
    }

    public void setFlash(String message) {
        flash = message;
        status = "info";
    }

    public boolean isSuccess() {
        return (status.equalsIgnoreCase("success"));
    }

    public boolean isDanger() {
        return (status.equalsIgnoreCase("danger"));
    }

    public boolean isWarning() {
        return (status.equalsIgnoreCase("warning"));
    }

    public boolean isInfo() {
        return (status.equalsIgnoreCase("info"));
    }
}
