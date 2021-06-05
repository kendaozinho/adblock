package com.kendao.adblock.screen.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kendao.adblock.MyGdxGame;
import com.kendao.adblock.enumerable.Assets;
import com.kendao.libgdx.scenes.scene2d.ui.CustomImageButton;
import com.kendao.libgdx.scenes.scene2d.ui.CustomTextButton;
import com.kendao.libgdx.scenes.scene2d.ui.CustomToast;
import com.kendao.libgdx.screen.base.CustomBaseScreen;

public class MainScreen extends CustomBaseScreen {
  private CustomImageButton image;

  public MainScreen() {
  }

  @Override
  protected void load() {
    this.image = new CustomImageButton(
        Assets.BAD_LOGIC.getValueAsTexture(),
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            CustomToast.alert("HELLO WORLD!");
          }
        }
    );

    this.image.setPosition(
        (MyGdxGame.getInstance().getFullWidth() / 2) - (this.image.getWidth() / 2),
        (MyGdxGame.getInstance().getFullHeight() / 2) - (this.image.getHeight() / 2)
    );

    CustomTextButton connectVpn = new CustomTextButton(
        "START SERVER", 25, MyGdxGame.getInstance().getFullHeight() - 75, 150, 50,
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            MyGdxGame.getInstance().getServerListener().startServer(8080);
          }
        }
    );

    CustomTextButton disconnectVpn = new CustomTextButton(
        "STOP SERVER", 25, MyGdxGame.getInstance().getFullHeight() - 150, 150, 50,
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            MyGdxGame.getInstance().getServerListener().stopServer();
          }
        }
    );

    super.getMainStage().addActor(this.image);
    super.getMainStage().addActor(connectVpn);
    super.getMainStage().addActor(disconnectVpn);
  }

  @Override
  public void handleInput() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      this.image.setY(this.image.getY() + 10);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      this.image.setX(this.image.getX() + 10);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
      this.image.setY(this.image.getY() - 10);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
      this.image.setX(this.image.getX() - 10);
    }
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