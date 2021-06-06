package com.kendao.adblock.listener;

public interface ServerListener {
  void startServer(int port) throws Throwable;

  void stopServer() throws Throwable;

  String getServerLog();
}
