package com.kendao.adblock;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kendao.adblock.listener.AdsListener;
import java.security.MessageDigest;

public class AndroidLauncher extends AndroidApplication implements AdsListener {
  private final int SHOW_ADS = 1;
  private final int HIDE_ADS = 0;

  private AdView adView;

  @SuppressLint("HandlerLeak")
  protected Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case SHOW_ADS: {
          adView.setVisibility(View.VISIBLE);
          break;
        }
        case HIDE_ADS: {
          adView.setVisibility(View.GONE);
          break;
        }
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // ORIGINAL INITIALIZATION
    // initialize(new MyGdxGame(), new AndroidApplicationConfiguration());

    String deviceId = Settings.Secure.getString(super.getContentResolver(), Settings.Secure.ANDROID_ID);

    String secretKey = "unknown";
    try {
      PackageInfo packageInfo =
          getPackageManager().getPackageInfo("com.kendao.adblock", PackageManager.GET_SIGNATURES);
      for (Signature signature : packageInfo.signatures) {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        secretKey = Base64.encodeToString(sha.digest(signature.toByteArray()), Base64.DEFAULT).replace("\n", "");
      }
    } catch (Throwable t) {
      throw new RuntimeException("Unable to get signatures: " + t);
    }

    // Create layout
    RelativeLayout layout = new RelativeLayout(this);

    // Create libgdx view
    View gameView = super.initializeForView(
        new MyGdxGame(
          deviceId,
          secretKey,
          this,
          new PurchaseManagerGoogleBilling(this)
        ),
        new AndroidApplicationConfiguration()
    );

    // Add libgdx into layout
    layout.addView(gameView);

    // Create and setup the AdMob view
    this.adView = new AdView(this);

    this.adView.setAdUnitId("ca-app-pub-1111111111111111/2222222222"); // set your ads unit id

    this.adView.setAdSize(AdSize.SMART_BANNER);

    this.adView.setAdListener(new AdListener() {
      @Override
      public void onAdFailedToLoad(int errorCode) {
        super.onAdFailedToLoad(errorCode);
      }

      @Override
      public void onAdLoaded() {
        super.onAdLoaded();
      }

      @Override
      public void onAdOpened() {
        super.onAdOpened();
      }

      @Override
      public void onAdClosed() {
        super.onAdClosed();
      }
    });

    // OBS: You need to add the test device to show the ad in debug mode.
    // If you don't know the id, just look at the Android log. For example:
    // I/Ads: Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("8831A9CD6F6408098E0FAE18E6E2202A") to get test ads on this device.
    this.adView.loadAd(
        new AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            // .addTestDevice("8831A9CD6F6408098E0FAE18E6E2202A")
            .build()
    );

    // Add AdMob into layout
    layout.addView(
      this.adView,
      new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) {{
        super.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      }}
    );

    // Hook it all up
    super.setContentView(layout);
  }

  @Override
  public void showAds() {
    this.handler.sendEmptyMessage(this.SHOW_ADS);
  }

  @Override
  public void hideAds() {
    this.handler.sendEmptyMessage(this.HIDE_ADS);
  }
}
