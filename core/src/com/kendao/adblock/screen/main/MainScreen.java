package com.kendao.adblock.screen.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kendao.adblock.MyGdxGame;
import com.kendao.libgdx.scenes.scene2d.ui.CustomLabel;
import com.kendao.libgdx.scenes.scene2d.ui.CustomTextArea;
import com.kendao.libgdx.scenes.scene2d.ui.CustomTextButton;
import com.kendao.libgdx.screen.base.CustomBaseScreen;

public class MainScreen extends CustomBaseScreen {
  public MainScreen() {
  }

  @Override
  protected void load() {
    CustomLabel label = new CustomLabel("SERVER IS\nOFFLINE!", CustomLabel.Sizes.EXTRA_SMALL) {{
      super.setPosition(
          (MyGdxGame.getInstance().getFullWidth() / 2) - (super.getWidth() / 2),
          MyGdxGame.getInstance().getFullHeight() - super.getHeight() - 50
      );
      super.setAlignment(Align.center);
      super.setColor(Color.SCARLET);
    }};

    CustomTextArea textArea = new CustomTextArea(
        5, 5,
        MyGdxGame.getInstance().getFullWidth() - 10,
        (int) (MyGdxGame.getInstance().getFullHeight() - 50 - label.getHeight() - 50 - 50 - 50)
    );

    CustomTextButton button = new CustomTextButton(
        "START",
        (MyGdxGame.getInstance().getFullWidth() / 2) - 100,
        ((int) (MyGdxGame.getInstance().getFullHeight() - 50 - label.getHeight() - 50 - 50)),
        200, 50,
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            try {
              if (label.getColor().equals(Color.SCARLET)) {
                if (MyGdxGame.getInstance().getServerListener() != null) {
                  MyGdxGame.getInstance().getServerListener().startServer(8080);
                }

                label.setText("SERVER IS\nONLINE!");
                label.setPosition(
                    (MyGdxGame.getInstance().getFullWidth() / 2) - (label.getWidth() / 2),
                    MyGdxGame.getInstance().getFullHeight() - label.getHeight() - 50
                );
                label.setAlignment(Align.center);
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
                label.setAlignment(Align.center);
                label.setColor(Color.SCARLET);

                ((CustomTextButton) event.getListenerActor()).setText("START");
              }
            } catch (Throwable t) {
              label.setText(t.toString());
              label.setPosition(
                  (MyGdxGame.getInstance().getFullWidth() / 2) - (label.getWidth() / 2),
                  MyGdxGame.getInstance().getFullHeight() - label.getHeight() - 50
              );
              label.setAlignment(Align.center);
            }
          }
        }
    );

    super.getHudStage().addActor(label);
    super.getHudStage().addActor(button);
    super.getHudStage().addActor(textArea);

    new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(500);
        } catch (Throwable t) {
          // do nothing!
        }

        Gdx.app.postRunnable(() -> {
          if (MyGdxGame.getInstance().getServerListener() != null) {
            String log = MyGdxGame.getInstance().getServerListener().getServerLog();
            if (log != null && !log.trim().isEmpty()) {
              textArea.addText(log);
            }
          }
        });
      }
    }).start();
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