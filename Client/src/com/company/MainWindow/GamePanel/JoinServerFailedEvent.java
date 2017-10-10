package com.company.MainWindow.GamePanel;

import java.util.EventObject;

public class JoinServerFailedEvent extends EventObject{
    String connectionFailureReason;
    public JoinServerFailedEvent(Object source, String reason){
        super(source);
        connectionFailureReason = reason;
    }

    public String getConnectionFailureReason() {
        return connectionFailureReason;
    }
}
