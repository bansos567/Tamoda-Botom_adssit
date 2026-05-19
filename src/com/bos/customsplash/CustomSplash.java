package com.bos.customsplash;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

// Import Lottie
import com.airbnb.lottie.LottieAnimationView;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = 1,
        description = "Custom Splash Screen Extension with HTML & Lottie support",
        iconName = "images/extension.png",
        nonVisible = true)
@SimpleObject(external = true)
// PERHATIAN: Di sistem Ant, library eksternal wajib dideklarasikan di sini
@UsesLibraries(libraries = "lottie.aar, lottie.jar") 
public class CustomSplash extends AndroidNonvisibleComponent {

    private Context context;
    private WebView webView;
    private LottieAnimationView lottieView;
    private ViewGroup containerView;

    public CustomSplash(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

    // 1. FUNGSI UNTUK HTML
    @SimpleFunction(description = "Initialize Splash Screen with HTML content")
    public void InitializeHTMLSplash(AndroidViewComponent container, String htmlContent) {
        View view = container.getView();
        if (view instanceof ViewGroup) {
            this.containerView = (ViewGroup) view;
            
            webView = new WebView(context);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
            
            this.containerView.addView(webView);
            this.containerView.setVisibility(View.VISIBLE);
        }
    }

    // 2. FUNGSI UNTUK LOTTIE
    @SimpleFunction(description = "Initialize Splash Screen with Native Lottie Animation")
    public void InitializeLottieSplash(AndroidViewComponent container, String lottieFileName) {
        View view = container.getView();
        if (view instanceof ViewGroup) {
            this.containerView = (ViewGroup) view;
            
            lottieView = new LottieAnimationView(context);
            lottieView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            
            // Render dari file JSON yang ada di Assets
            lottieView.setAnimation(lottieFileName);
            lottieView.playAnimation();
            lottieView.loop(true);
            
            this.containerView.addView(lottieView);
            this.containerView.setVisibility(View.VISIBLE);
        }
    }

    // 3. FUNGSI HIDE & CLEAR RAM
    @SimpleFunction(description = "Hide the Splash Screen Container")
    public void HideSplash() {
        if (this.containerView != null) {
            this.containerView.setVisibility(View.GONE);
            this.containerView.removeAllViews(); 
            
            if (this.webView != null) {
                this.webView = null;
            }
            
            if (this.lottieView != null) {
                this.lottieView.cancelAnimation();
                this.lottieView = null;
            }
        }
    }
}
