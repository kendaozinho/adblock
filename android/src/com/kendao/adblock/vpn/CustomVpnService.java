package com.kendao.adblock.vpn;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import com.kendao.adblock.AndroidLauncher;
import com.kendao.adblock.R;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CustomVpnService extends VpnService implements Handler.Callback {
  public static final String ACTION_CONNECT = "com.kendao.adblock.vpn.START";
  public static final String ACTION_DISCONNECT = "com.kendao.adblock.vpn.STOP";
  private static final String TAG = CustomVpnService.class.getSimpleName();
  private final AtomicReference<Thread> mConnectingThread = new AtomicReference<>();
  private final AtomicReference<Connection> mConnection = new AtomicReference<>();
  private Handler mHandler;
  private AtomicInteger mNextConnectionId = new AtomicInteger(1);
  private PendingIntent mConfigureIntent;

  @Override
  public void onCreate() {
    // The handler is only used to show messages.
    if (mHandler == null) {
      mHandler = new Handler(this);
    }
    // Create the intent to "configure" the connection (just start AndroidLauncher).
    mConfigureIntent = PendingIntent.getActivity(this, 0, new Intent(this, AndroidLauncher.class),
        PendingIntent.FLAG_UPDATE_CURRENT);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null && ACTION_DISCONNECT.equals(intent.getAction())) {
      disconnect();
      return START_NOT_STICKY;
    } else {
      connect();
      return START_STICKY;
    }
  }

  @Override
  public void onDestroy() {
    disconnect();
  }

  @Override
  public boolean handleMessage(Message message) {
    Toast.makeText(this, message.what, Toast.LENGTH_SHORT).show();
    if (message.what != R.string.disconnected) {
      updateForegroundNotification(message.what);
    }
    return true;
  }

  private void connect() {
    // Become a foreground service. Background services can be VPN services too, but they can
    // be killed by background check before getting a chance to receive onRevoke().
    updateForegroundNotification(R.string.connecting);
    mHandler.sendEmptyMessage(R.string.connecting);
    // Extract information from the shared preferences.
    final SharedPreferences prefs = getSharedPreferences(AndroidLauncher.Prefs.NAME, MODE_PRIVATE);
    final String server = prefs.getString(AndroidLauncher.Prefs.SERVER_ADDRESS, "");
    final byte[] secret = prefs.getString(AndroidLauncher.Prefs.SHARED_SECRET, "").getBytes();
    final boolean allow = prefs.getBoolean(AndroidLauncher.Prefs.ALLOW, true);
    final Set<String> packages =
        prefs.getStringSet(AndroidLauncher.Prefs.PACKAGES, Collections.emptySet());
    final int port = prefs.getInt(AndroidLauncher.Prefs.SERVER_PORT, 0);
    final String proxyHost = prefs.getString(AndroidLauncher.Prefs.PROXY_HOSTNAME, "");
    final int proxyPort = prefs.getInt(AndroidLauncher.Prefs.PROXY_PORT, 0);
    startConnection(new CustomVpnConnection(
        this, mNextConnectionId.getAndIncrement(), server, port, secret,
        proxyHost, proxyPort, allow, packages));
  }

  private void startConnection(final CustomVpnConnection connection) {
    // Replace any existing connecting thread with the  new one.
    final Thread thread = new Thread(connection, "CustomVpnThread");
    setConnectingThread(thread);
    // Handler to mark as connected once onEstablish is called.
    connection.setConfigureIntent(mConfigureIntent);
    connection.setOnEstablishListener(tunInterface -> {
      mHandler.sendEmptyMessage(R.string.connected);
      mConnectingThread.compareAndSet(thread, null);
      setConnection(new Connection(thread, tunInterface));
    });
    thread.start();
  }

  private void setConnectingThread(final Thread thread) {
    final Thread oldThread = mConnectingThread.getAndSet(thread);
    if (oldThread != null) {
      oldThread.interrupt();
    }
  }

  private void setConnection(final Connection connection) {
    final Connection oldConnection = mConnection.getAndSet(connection);
    if (oldConnection != null) {
      try {
        oldConnection.first.interrupt();
        oldConnection.second.close();
      } catch (IOException e) {
        Log.e(TAG, "Closing VPN interface", e);
      }
    }
  }

  private void disconnect() {
    mHandler.sendEmptyMessage(R.string.disconnected);
    setConnectingThread(null);
    setConnection(null);
    stopForeground(true);
  }

  private void updateForegroundNotification(final int message) {
    final String NOTIFICATION_CHANNEL_ID = "CustomVpn";
    NotificationManager mNotificationManager = (NotificationManager) getSystemService(
        NOTIFICATION_SERVICE);
    mNotificationManager.createNotificationChannel(new NotificationChannel(
        NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID,
        NotificationManager.IMPORTANCE_DEFAULT));
    startForeground(1, new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
        // .setSmallIcon(R.drawable.ic_vpn)
        .setContentText(getString(message))
        .setContentIntent(mConfigureIntent)
        .build());
  }

  private static class Connection extends Pair<Thread, ParcelFileDescriptor> {
    public Connection(Thread thread, ParcelFileDescriptor pfd) {
      super(thread, pfd);
    }
  }
}