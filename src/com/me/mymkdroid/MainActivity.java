/**
 *
 * @author sleepygarden
 *
 */

package @{PACKAGE_NAME};

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

public class WebActivity extends Activity {
	protected WebView webview;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		if (webview == null) {
			webview = (WebView) findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setSupportZoom(true);
			webview.getSettings().setBuiltInZoomControls(true);
			webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			webview.setScrollbarFadingEnabled(true);
			webview.getSettings().setLoadsImagesAutomatically(true);
			webview.addJavascriptInterface(new MKDroidJsInterface(this),
					"Android");
			webview.setWebViewClient(new MKDroidClient(this, @{DOMAIN_NAME}));
			webview.loadUrl("file:///android_asset/webfiles/index.html");

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		webview.saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		webview.restoreState(savedInstanceState);
	}
}
