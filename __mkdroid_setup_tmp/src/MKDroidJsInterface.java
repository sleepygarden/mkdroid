package @{PACKAGE_NAME};

import android.content.Context;
@{IF_V_17_UP}import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class MKDroidJsInterface {
    Context mContext;

    MKDroidJsInterface(Context c) {
        mContext = c;
    }

    /* in your js, call showToast to use this android function*/
    @{IF_V_17_UP}@JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}