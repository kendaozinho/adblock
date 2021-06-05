package com.kendao.adblock;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import android.widget.Toast;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kendao.adblock.listener.ServerListener;
import com.kendao.adblock.server.ServerService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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

  private String documentRoot;
  private String lastMessage = "";

  private ServerService mBoundService;

  private ServiceConnection mConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder service) {
      mBoundService = ((ServerService.LocalBinder) service).getService();
      Toast.makeText(AndroidLauncher.this, "Service connected", Toast.LENGTH_SHORT).show();
      mBoundService.updateNotification(lastMessage);

      // mToggleButton.setChecked(mBoundService.isRunning());
    }

    public void onServiceDisconnected(ComponentName className) {
      mBoundService = null;
      Toast.makeText(AndroidLauncher.this, "Service disconnected", Toast.LENGTH_SHORT).show();
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

    documentRoot = getDocRoot();

    if (documentRoot != null) {
      try {
        if (!(new File(documentRoot)).exists()) {
          (new File(documentRoot)).mkdir();
          Log.d("Webserver", "Created " + documentRoot);
          BufferedWriter bout = new BufferedWriter(new FileWriter(documentRoot + "index.html"));
          bout.write("<html><head><title>Android Webserver</title>");
          bout.write("</head>");
          bout.write("<body>Willkommen auf dem Android Webserver.");
          bout.write("<br><br>Die HTML-Dateien liegen in " + documentRoot + ", der Sourcecode dieser App auf ");
          bout.write("<a href=\"https://github.com/bodeme/androidwebserver\">Github</a>");
          bout.write("</body></html>");
          bout.flush();
          bout.close();
          bout = new BufferedWriter(new FileWriter(documentRoot + "403.html"));
          bout.write("<html><head><title>Error 403</title>");
          bout.write("</head>");
          bout.write("<body>403 - Forbidden</body></html>");
          bout.flush();
          bout.close();
          bout = new BufferedWriter(new FileWriter(documentRoot + "404.html"));
          bout.write("<html><head><title>Error 404</title>");
          bout.write("</head>");
          bout.write("<body>404 - File not found</body></html>");
          bout.flush();
          bout.close();
          Log.d("Webserver", "Created html files");
        }
      } catch (Exception e) {
        Log.v("ERROR", e.getMessage());
      }

      log("");
      log("Please mail suggestions to fef9560@b0d3.de");
      log("");
      log("Document-Root: " + documentRoot);
    } else {
      log("Error: Document-Root could not be found.");
    }

    /* mToggleButton.setOnClickListener(new OnClickListener() {
      public void onClick(View arg0) {
        if(mToggleButton.isChecked()) {
          startServer(mHandler, documentRoot, new Integer(port.getText().toString()));
        } else {
          stopServer();
        }
      }
    }); */

    doBindService();

    super.initialize(new MyGdxGame(this), new AndroidApplicationConfiguration());
  }

  @Override
  public void startServer(int port) {
    if (mBoundService == null) {
      Toast.makeText(AndroidLauncher.this, "Service not connected", Toast.LENGTH_SHORT).show();
    } else {
      mBoundService.startServer(handler, documentRoot, port);
    }
  }

  @Override
  public void stopServer() {
    if (mBoundService == null) {
      Toast.makeText(AndroidLauncher.this, "Service not connected", Toast.LENGTH_SHORT).show();
    } else {
      mBoundService.stopServer();
    }
  }

  private void doUnbindService() {
    if (mBoundService != null) {
      unbindService(mConnection);
    }
  }

  private void doBindService() {
    bindService(new Intent(AndroidLauncher.this, ServerService.class), mConnection, Context.BIND_AUTO_CREATE);
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

  private String getDocRoot() {
    return Environment.getExternalStorageDirectory().getAbsolutePath() + "/androidwebserver/";
  }
}
