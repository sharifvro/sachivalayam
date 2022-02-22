package com.sachivalayam.sachivalayam;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.webkit.WebView;

public class IntentHandler {

    private Activity context;
    public static int type = Intent.URI_INTENT_SCHEME;
    public static String SCHEME = "intent://";

    public IntentHandler(Activity activity){ this.context = activity; }

    public void startIntent(WebView view,Intent intent){
        PackageManager packageManager = context.getPackageManager();
        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info != null) {
            context.startActivity(intent);
        } else {
            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
            view.loadUrl(fallbackUrl);
        }
    }
}
