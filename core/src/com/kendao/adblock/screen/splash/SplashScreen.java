package com.kendao.adblock.screen.splash;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.kendao.adblock.MyGdxGame;
import com.kendao.adblock.enumerable.Preferences;
import com.kendao.adblock.screen.main.MainScreen;
import com.kendao.libgdx.assets.CustomAssetManager;
import com.kendao.libgdx.payment.base.CustomPurchaseManager;
import com.kendao.libgdx.scenes.scene2d.ui.CustomLabel;
import com.kendao.libgdx.screen.base.CustomBaseScreen;
import com.kendao.libgdx.screen.base.CustomScreenManager;

public class SplashScreen extends CustomBaseScreen {
  public SplashScreen() {
  }

  @Override
  protected void load() {
    CustomLabel label = new CustomLabel("AdBlock", Color.WHITE, CustomLabel.Sizes.LARGE);
    label.setPosition((MyGdxGame.getInstance().getFullWidth() / 2) - (label.getWidth() / 2), (MyGdxGame.getInstance().getFullHeight() / 2) - (label.getHeight() / 2));
    label.setAlignment(Align.center);
    label.hide();

    super.getHudStage().addActor(label);

    label.addAction(
        Actions.sequence(
            Actions.delay(0.5f),
            Actions.fadeIn(1f),
            Actions.run(
                () -> {
                  CustomAssetManager.getInstance().loadAllAssets();

                  if (!Preferences.PURCHASES_RESTORED.getPropertyAsBoolean() && CustomPurchaseManager.getInstance().installed()) {
                    CustomPurchaseManager.getInstance().purchaseRestore();
                  }
                }
            ),
            Actions.delay(1f), // Waiting to load the assets
            Actions.fadeOut(1f),
            Actions.delay(0.5f),
            Actions.run(
                () -> CustomScreenManager.getInstance().setScreen(new MainScreen())
            )
        )
    );
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
