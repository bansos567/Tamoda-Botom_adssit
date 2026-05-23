package com.tamoda.onesignal;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;

import android.content.Context;
// Import Library OneSignal
import com.onesignal.OneSignal;
import com.onesignal.OSDeviceState;

@DesignerComponent(
    version = 1,
    description = "Tamoda OneSignal Fixer. Solusi Push ID dan Izin Notifikasi Android 13+.",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = ""
)
@SimpleObject(external = true)
// Tambahkan permission wajib Android 13+ agar tidak gagal subscribe
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.POST_NOTIFICATIONS")
public class TamodaOneSignal extends AndroidNonvisibleComponent {
    
    private Context context;

    public TamodaOneSignal(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

    // 1. INISIALISASI ONESIGNAL
    @SimpleFunction(description = "Inisialisasi OneSignal menggunakan App ID Bos. Panggil ini di Screen1.Initialize")
    public void InitializeOneSignal(String appId) {
        // Mode Debug (Opsional, hapus jika rilis)
        // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        
        // Mulai Inisialisasi
        OneSignal.initWithContext(context);
        OneSignal.setAppId(appId);
        
        // [KUNCI UTAMA] Meminta izin notifikasi untuk Android 13 ke atas
        OneSignal.promptForPushNotifications();
    }

    // 2. MENDAPATKAN PUSH ID (PLAYER ID)
    @SimpleFunction(description = "Mendapatkan Push ID (Player ID) untuk mengirim notifikasi spesifik ke user ini.")
    public String GetPlayerId() {
        try {
            OSDeviceState deviceState = OneSignal.getDeviceState();
            
            // Cek apakah device sudah tersubscribe
            if (deviceState != null && deviceState.isSubscribed()) {
                return deviceState.getUserId(); // Ini adalah Player ID yang Bos cari
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
        
        // Jika user belum mengizinkan notifikasi atau jaringan lelet
        return "NOT_SUBSCRIBED"; 
    }
}
