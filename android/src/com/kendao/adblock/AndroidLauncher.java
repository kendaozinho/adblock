package com.kendao.adblock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kendao.adblock.listener.ServerListener;
import com.kendao.adblock.server.ServerService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;

public class AndroidLauncher extends AndroidApplication implements ServerListener {
  @SuppressLint("HandlerLeak")
  final Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      Bundle b = msg.getData();
      log(b.getString("msg"));
    }
  };

  // private ToggleButton mToggleButton;
  // private EditText port;
  // private static TextView mLog;
  // private static ScrollView mScroll;

  private ServerService mBoundService;

  private final ServiceConnection mConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder service) {
      mBoundService = ((ServerService.LocalBinder) service).getService();

      // Toast.makeText(AndroidLauncher.this, "Service connected", Toast.LENGTH_SHORT).show();
      System.out.println("SERVICE CONNECTED!");

      // mToggleButton.setChecked(mBoundService.isRunning());
    }

    public void onServiceDisconnected(ComponentName className) {
      mBoundService = null;

      // Toast.makeText(AndroidLauncher.this, "Service disconnected", Toast.LENGTH_SHORT).show();
      System.out.println("SERVICE DISCONNECTED!");
    }
  };

  public static void log(String s) {
    // mLog.append(s + "\n");
    // mScroll.fullScroll(ScrollView.FOCUS_DOWN);

    System.out.println("LOG: " + s);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // mToggleButton = (ToggleButton) findViewById(R.id.toggle);
    // port = (EditText) findViewById(R.id.port);
    // mLog = (TextView) findViewById(R.id.log);
    // mScroll = (ScrollView) findViewById(R.id.ScrollView01);

    String documentRoot = getDocumentRoot();

    try {
      Log.d("WebServer", "Created " + documentRoot);

      File folder = new File(documentRoot);
      if (!folder.exists()) {
        folder.mkdirs();
      }

      File indexFile = new File(documentRoot + "index.html");
      if (!indexFile.exists()) {
        File newFile = new File(folder, "index.html");

        BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
        writer.write("<html>");
        writer.write("<head><title>Android WebServer</title></head>");
        writer.write("<body>KENDAO Android WebServer.</body>");
        writer.write("</html>");
        writer.close();
      }

      File forbiddenFile = new File(documentRoot + "403.html");
      if (!forbiddenFile.exists()) {
        File newFile = new File(folder, "403.html");

        BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
        writer.write("<html><head><title>Error 403</title></head><body>403 - Forbidden</body></html>");
        writer.close();
      }

      File notFoundFile = new File(documentRoot + "404.html");
      if (!notFoundFile.exists()) {
        File newFile = new File(folder, "404.html");

        BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
        writer.write("<html><head><title>Error 404</title></head><body>404 - Not Found</body></html>");
        writer.close();
      }

      Log.d("WebServer", "Created html files");
    } catch (Exception e) {
      Log.v("WebServer", e.toString());
    }

    log("Document-Root: " + documentRoot);

    /* mToggleButton.setOnClickListener(new OnClickListener() {
      public void onClick(View arg0) {
        if(mToggleButton.isChecked()) {
          startServer(mHandler, documentRoot, new Integer(port.getText().toString()));
        } else {
          stopServer();
        }
      }
    }); */

    super.bindService(new Intent(AndroidLauncher.this, ServerService.class), mConnection, Context.BIND_AUTO_CREATE);

    super.initialize(new MyGdxGame(this), new AndroidApplicationConfiguration());
  }

  @Override
  public void startServer(int port) {
    if (mBoundService == null) {
      // Toast.makeText(AndroidLauncher.this, "Service not connected", Toast.LENGTH_SHORT).show();
      System.out.println("SERVICE NOT CONNECTED!");
    } else {
      mBoundService.startServer(mHandler, getDocumentRoot(), port);
    }
  }

  @Override
  public void stopServer() {
    if (mBoundService == null) {
      // Toast.makeText(AndroidLauncher.this, "Service not connected", Toast.LENGTH_SHORT).show();
      System.out.println("SERVICE NOT CONNECTED!");
    } else {
      mBoundService.stopServer();
    }
  }

  private void doUnbindService() {
    if (mBoundService != null) {
      unbindService(mConnection);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    doUnbindService();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private String getDocumentRoot() {
    return Environment.getExternalStorageDirectory().getAbsolutePath() + "/webserver/";
  }
}
