package com.ataulm.stacks.jabber;

import android.content.Context;
import android.widget.Toast;

class Toaster {

    private static Toast toast;

    public static void displayToast(Context context, CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

}
