package com.company.MainWindow.GamePanel;

import java.util.EventListener;
import java.util.EventObject;

public interface RePaintMeEventListener extends EventListener{
    void rePaintRequest(EventObject e);
}
