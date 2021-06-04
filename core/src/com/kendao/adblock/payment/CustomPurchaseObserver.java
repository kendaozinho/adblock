package com.kendao.adblock.payment;

import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.kendao.adblock.enumerable.Preferences;
import com.kendao.adblock.enumerable.Purchases;

public class CustomPurchaseObserver implements PurchaseObserver {
  @Override
  public void handleInstall() {
    System.out.println("Google Billing - INSTALL with success!");
  }

  @Override
  public void handleInstallError(Throwable e) {
    System.err.println("Google Billing - INSTALL with error -> " + e);
  }

  @Override
  public void handleRestore(Transaction[] transactions) {
    System.out.println("Google Billing - RESTORE with success!");

    Preferences.PURCHASES_RESTORED.setPropertyAsBoolean(true);

    for (Transaction transaction : transactions) {
      if (transaction.isPurchased()) {
        if (transaction.getIdentifier().equals(Purchases.PURCHASE_ID.getValue())) {
          // ?
        }
      }
    }
  }

  @Override
  public void handleRestoreError(Throwable e) {
    System.err.println("Google Billing - RESTORE with error -> " + e);
  }

  @Override
  public void handlePurchase(Transaction transaction) {
    System.out.println("Google Billing - PURCHASE with success!");

    if (transaction.getIdentifier().equals(Purchases.PURCHASE_ID.getValue())) {
      // ?
    }
  }

  @Override
  public void handlePurchaseError(Throwable e) {
    System.err.println("Google Billing - PURCHASE with error -> " + e);
  }

  @Override
  public void handlePurchaseCanceled() {
    System.out.println("Google Billing - PURCHASE is canceled!");
  }
}