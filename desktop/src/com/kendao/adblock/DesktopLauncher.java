package com.kendao.adblock;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = "AdBlock";
    config.width = 480;
    config.height = 800;
    new LwjglApplication(new MyGdxGame(), config);
  }
}
