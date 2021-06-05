/*
 * Copyright (C) 2009-2014 Markus Bode
 *
 * Licensed under the GNU General Public License v3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.kendao.adblock.server;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.util.Log;
import com.kendao.adblock.AndroidLauncher;

public class ServerService extends Service {
  private final IBinder mBinder = new LocalBinder();
  private Server server;
  private boolean isRunning = false;

  public static String intToIp(int i) {
    return ((i) & 0xFF) + "." +
        ((i >> 8) & 0xFF) + "." +
        ((i >> 16) & 0xFF) + "." +
        (i >> 24 & 0xFF);
  }

  public void startServer(Handler handler, String documentRoot, int port) {
    try {
      isRunning = true;
      WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
      WifiInfo wifiInfo = wifiManager.getConnectionInfo();

      String ipAddress = intToIp(wifiInfo.getIpAddress());

      if (wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
        new AlertDialog.Builder(this).setTitle("Error").setMessage("Please connect to a WIFI-network for starting the webserver.").setPositiveButton("OK", null).show();
        throw new Exception("Please connect to a WIFI-network.");
      }

      server = new Server(handler, documentRoot, ipAddress, port, getApplicationContext());
      server.start();

      Intent i = new Intent(this, AndroidLauncher.class);
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

      System.out.println("WebServer is running on port " + ipAddress + ":" + port);

      Message msg = new Message();
      Bundle b = new Bundle();
      b.putString("msg", "WebServer is running on port " + ipAddress + ":" + port);
      msg.setData(b);
      handler.sendMessage(msg);

    } catch (Exception e) {
      isRunning = false;
      Log.e("WebServer", e.toString());
      System.err.println("WebServer Error: " + e.getMessage());
    }
  }

  public void stopServer() {
    if (null != server) {
      server.stopServer();
      server.interrupt();
      isRunning = false;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public class LocalBinder extends Binder {
    public ServerService getService() {
      return ServerService.this;
    }
  }
}
