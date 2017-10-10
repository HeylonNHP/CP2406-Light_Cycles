package com.company.MainWindow.GamePanel;

import java.util.EventListener;

public interface JoinServerFailedListener extends EventListener {
    void joinServerFailed(JoinServerFailedEvent e);
}
