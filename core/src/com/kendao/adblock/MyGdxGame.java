package com.kendao.adblock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManager;
import com.kendao.adblock.enumerable.Purchases;
import com.kendao.adblock.listener.AdsListener;
import com.kendao.adblock.payment.CustomPurchaseObserver;
import com.kendao.adblock.screen.splash.SplashScreen;
import com.kendao.libgdx.assets.CustomAssetManager;
import com.kendao.libgdx.listener.CustomGameListener;
import com.kendao.libgdx.payment.base.CustomPurchaseManager;
import com.kendao.libgdx.scenes.scene2d.ui.CustomSkin;
import com.kendao.libgdx.screen.base.CustomScreenManager;
import com.kendao.libgdx.storage.CustomPreferences;

import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter implements CustomGameListener {
  private final HashMap<Class, Object> instances = new HashMap(); // for Dependency Injection

  private final String deviceId; // is UNIQUE
  private final String secretKey; // key generated based on the application's keystore
  private final AdsListener adsListener;
  private final PurchaseManager purchaseManager;

  public MyGdxGame(String deviceId, String secretKey) {
    this.deviceId = deviceId;
    this.secretKey = secretKey;
    this.adsListener = null;
    this.purchaseManager = null;
  }

  public MyGdxGame(String deviceId, String secretKey, AdsListener adsListener, PurchaseManager purchaseManager) {
    this.deviceId = deviceId;
    this.secretKey = secretKey;
    this.adsListener = adsListener;
    this.purchaseManager = purchaseManager;
  }

  public static MyGdxGame getInstance() {
    return ((MyGdxGame) Gdx.app.getApplicationListener());
  }

  @Override
  public void create() {
    // Set log level
    Gdx.app.setLogLevel(Application.LOG_DEBUG);

    // Disable hardware back button
    Gdx.input.setCatchBackKey(true);

    // Load Custom Purchase Manager
    this.instances.put(
        CustomPurchaseManager.class,
        new CustomPurchaseManager(this.purchaseManager) {{
          super.install(
              new CustomPurchaseObserver(),
              new HashMap<OfferType, String>() {{
                super.put(OfferType.ENTITLEMENT, Purchases.PURCHASE_ID.getValue());
              }}
          );
        }}
    );

    // Load asset manager
    this.instances.put(CustomAssetManager.class, new CustomAssetManager());

    // Load default skin
    this.instances.put(CustomSkin.class, new CustomSkin());

    // Load preferences
    this.instances.put(CustomPreferences.class, new CustomPreferences("com.kendao.adblock"));

    // Set fullscreen if is Desktop
    /* if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
      Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    } */

    // Load screen manager
    this.instances.put(CustomScreenManager.class, new CustomScreenManager());

    // Open the splash screen and load all assets
    CustomScreenManager.getInstance().setScreen(new SplashScreen());
  }

  @Override
  public void render() {
    CustomScreenManager.getInstance().render();
  }

  @Override
  public void resize(int width, int height) {
    CustomScreenManager.getInstance().resize(width, height);
  }

  @Override
  public void pause() {
    CustomScreenManager.getInstance().pause();
  }

  @Override
  public void resume() {
    CustomScreenManager.getInstance().resume();
  }

  @Override
  public void dispose() {
    CustomScreenManager.getInstance().dispose();
    CustomPurchaseManager.getInstance().dispose();
  }

  @Override
  public Integer getFullWidth() {
    return 480;
  }

  @Override
  public Integer getFullHeight() {
    return 800;
  }

  @Override
  public <T> T getInstanceOf(Class<T> clazz) {
    Object instance = this.instances.get(clazz);
    return (instance == null ? null : (T) instance);
  }

  public void showAds() {
    if (this.adsListener != null) {
      this.adsListener.showAds();
    }
  }

  public void hideAds() {
    if (this.adsListener != null) {
      this.adsListener.hideAds();
    }
  }
}
