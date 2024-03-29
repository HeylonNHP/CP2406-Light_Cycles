package game.client.MainWindow.GamePanel;

import java.util.EventObject;

public class JoinServerFailedEvent extends EventObject{
    private String connectionFailureReason;
    public JoinServerFailedEvent(Object source, String reason){
        super(source);
        connectionFailureReason = reason;
    }

    public String getConnectionFailureReason() {
        return connectionFailureReason;
    }
}
