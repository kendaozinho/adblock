package com.kendao.adblock;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.security.MessageDigest;

public class AndroidLauncher extends AndroidApplication {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String deviceId = Settings.Secure.getString(super.getContentResolver(), Settings.Secure.ANDROID_ID);

    String secretKey = "unknown";
    try {
      PackageInfo packageInfo =
          getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
      for (Signature signature : packageInfo.signatures) {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        secretKey = Base64.encodeToString(sha.digest(signature.toByteArray()), Base64.DEFAULT).replace("\n", "");
      }
    } catch (Throwable t) {
      throw new RuntimeException("Unable to get signatures: " + t);
    }

    super.initialize(new MyGdxGame(deviceId, secretKey), new AndroidApplicationConfiguration());
  }
}
