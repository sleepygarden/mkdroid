package com.mcornell.checkinapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MKDroidClient extends WebViewClient {
	private String domain;
	private Context context;

	public MKDroidClient(Context context, String domain) {
		this.context = context;
		this.domain = domain;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.v("URL", url);

		if (url.contains(domain)) {
			url = url.replaceAll("https?://" + domain, "file:///android_asset/webfiles");
			Log.v("URL", url);
			view.loadUrl(url);
			return false;
		} else {
			confirmDialog(url); // open in chrome y/n
			return true; 
		}
	}

	@Override
	public void onLoadResource(WebView view, String url) {

	}

	@Override
	public void onPageFinished(WebView view, String url) {
		//view.setBackgroundColor(0x00000000); TODO idk if i need to do this??
	}

	public void confirmDialog(String url) {
		final Uri uri = Uri.parse(url);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					intent.putExtra(Browser.EXTRA_APPLICATION_ID,
							context.getPackageName());
					context.startActivity(intent);
					dialog.dismiss();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.dismiss();
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Open this page in Chrome?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

}