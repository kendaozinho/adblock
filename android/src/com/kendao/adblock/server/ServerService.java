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

import android.app.Service;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.*;

public class ServerService extends Service {
  private final IBinder binder = new LocalBinder();
  private Server server;
  private Handler handler;

  public static String intToIp(int i) {
    return ((i) & 0xFF) + "." +
        ((i >> 8) & 0xFF) + "." +
        ((i >> 16) & 0xFF) + "." +
        (i >> 24 & 0xFF);
  }

  public void startServer(Handler handler, String documentRoot, int port) {
    try {
      WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
      WifiInfo wifiInfo = wifiManager.getConnectionInfo();

      String ipAddress = intToIp(wifiInfo.getIpAddress());

      if (wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
        throw new RuntimeException("Please connect to a Wifi");
      }

      this.server = new Server(handler, documentRoot, ipAddress, port, getApplicationContext());
      this.server.start();

      this.handler = handler;

      this.sendMessage("WebServer is running on " + ipAddress + ":" + port);
    } catch (Throwable t) {
      System.err.println("Unable to start WebServer -> " + t.toString());
      throw new RuntimeException("Unable to start WebServer" + "\n" + t.getClass().getName() + "\n" + t.getMessage());
    }
  }

  public void stopServer() {
    if (this.server != null) {
      this.server.stopServer();
      this.server.interrupt();
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return this.binder;
  }

  private void sendMessage(String text) {
    System.out.println(text);

    if (this.handler != null) {
      Message msg = new Message();
      Bundle b = new Bundle();
      b.putString("msg", text);
      msg.setData(b);
      this.handler.sendMessage(msg);
    }
  }

  public class LocalBinder extends Binder {
    public ServerService getService() {
      return ServerService.this;
    }
  }
}
