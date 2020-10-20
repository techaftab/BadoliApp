package com.app.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityAboutUsBinding;

import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity {
    ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        setSupportActionBar(binding.toolbarChangePassword);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        binding.toolbarChangePassword.setTitle("");
        binding.toolbarChangePassword.setNavigationOnClickListener(v -> {
            Log.d("tag", "onClick : navigating back to back activity ");
            finish();
        });
        if (AppUtils.hasNetworkConnection(AboutUsActivity.this)){
            showLoading(getResources().getString(R.string.please_wait));
            binding.webView.setWebViewClient(new myWebClient());
            binding.webView.loadUrl(Constant.BASE_URL+Constant.ABOUT_US);
            binding.webView.getSettings().setLoadWithOverviewMode(true);
            binding.webView.getSettings().setUseWideViewPort(true);
            //  binding.webView.getSettings().setBuiltInZoomControls(true);
            binding.webView.setOnLongClickListener(v -> true);
        }else {
            Toast.makeText(AboutUsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(AboutUsActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            try {
                dismissLoading();
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            try {
                dismissLoading();
            }catch (Exception e){
                e.printStackTrace();
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            try {
                dismissLoading();
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPageFinished(view, url);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
