package com.kendao.adblock.screen.main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kendao.adblock.MyGdxGame;
import com.kendao.libgdx.scenes.scene2d.ui.CustomLabel;
import com.kendao.libgdx.scenes.scene2d.ui.CustomTextButton;
import com.kendao.libgdx.screen.base.CustomBaseScreen;

public class MainScreen extends CustomBaseScreen {
  private CustomLabel label;

  public MainScreen() {
  }

  @Override
  protected void load() {
    this.label = new CustomLabel("SERVER IS\nOFFLINE!") {{
      super.setPosition(
          (MyGdxGame.getInstance().getFullWidth() / 2) - (super.getWidth() / 2),
          MyGdxGame.getInstance().getFullHeight() - super.getHeight() - 50
      );
      super.setColor(Color.SCARLET);
    }};

    CustomTextButton button = new CustomTextButton(
        "START",
        (MyGdxGame.getInstance().getFullWidth() / 2) - 100,
        new Integer((int) (MyGdxGame.getInstance().getFullHeight() - 50 - this.label.getHeight() - 50 - 50)),
        200, 50,
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            if (label.getColor().equals(Color.SCARLET)) {
              if (MyGdxGame.getInstance().getServerListener() != null) {
                MyGdxGame.getInstance().getServerListener().startServer(8080);
              }

              label.setText("SERVER IS\nONLINE!");
              label.setPosition(
                  (MyGdxGame.getInstance().getFullWidth() / 2) - (label.getWidth() / 2),
                  MyGdxGame.getInstance().getFullHeight() - label.getHeight() - 50
              );
              label.setColor(Color.LIME);

              ((CustomTextButton) event.getListenerActor()).setText("STOP");
            } else if (label.getColor().equals(Color.LIME)) {
              if (MyGdxGame.getInstance().getServerListener() != null) {
                MyGdxGame.getInstance().getServerListener().stopServer();
              }

              label.setText("SERVER IS\nOFFLINE!");
              label.setPosition(
                  (MyGdxGame.getInstance().getFullWidth() / 2) - (label.getWidth() / 2),
                  MyGdxGame.getInstance().getFullHeight() - label.getHeight() - 50
              );
              label.setColor(Color.SCARLET);

              ((CustomTextButton) event.getListenerActor()).setText("START");
            }
          }
        }
    );

    super.getMainStage().addActor(this.label);
    super.getMainStage().addActor(button);
  }

  @Override
  public void handleInput() {
  }

  @Override
  public void update() {
  }

  @Override
  protected void render() {
  }

  @Override
  protected void pause() {
  }

  @Override
  protected void resume() {
  }

  @Override
  protected void dispose() {
  }
}