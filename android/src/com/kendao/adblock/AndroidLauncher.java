package com.kendao.adblock;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kendao.adblock.listener.VpnListener;
import com.kendao.adblock.vpn.CustomVpnService;

public class AndroidLauncher extends AndroidApplication implements VpnListener {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    super.initialize(new MyGdxGame(this), new AndroidApplicationConfiguration());
  }

  @Override
  public void connectVpn() {
    Intent intent = VpnService.prepare(this);
    if (intent != null) {
      startActivityForResult(intent, 0);
    } else {
      onActivityResult(0, RESULT_OK, null);
    }
  }

  @Override
  public void disconnectVpn() {
    super.startService(this.getServiceIntent().setAction(CustomVpnService.ACTION_DISCONNECT));
  }

  @Override
  protected void onActivityResult(int request, int result, Intent data) {
    if (result == RESULT_OK) {
      startService(getServiceIntent().setAction(CustomVpnService.ACTION_CONNECT));
    }
  }

  private Intent getServiceIntent() {
    return new Intent(this, CustomVpnService.class);
  }

  public interface Prefs {
    String NAME = "connection";
    String SERVER_ADDRESS = "server.address";
    String SERVER_PORT = "server.port";
    String SHARED_SECRET = "shared.secret";
    String PROXY_HOSTNAME = "proxyhost";
    String PROXY_PORT = "proxyport";
    String ALLOW = "allow";
    String PACKAGES = "packages";
  }
}
