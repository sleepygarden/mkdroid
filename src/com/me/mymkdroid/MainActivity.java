/**
 *
 * @author sleepygarden
 *
 */

package @{PACKAGE_NAME};

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity {

	@SuppressLint("SetJavaScriptEnabled") //christ i know already
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);  


		WebView webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl("file:///android_asset/index.html");
		webview.getSettings().setJavaScriptEnabled(true);
		webview.addJavascriptInterface(new WebAppInterface(this), "Android");
		webview.setWebViewClient(new MKDroidClient(this,"www.mydomain.com"));

	}

}