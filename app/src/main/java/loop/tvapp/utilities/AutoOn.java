package loop.tvapp.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import loop.tvapp.MainActivity;

public class AutoOn extends BroadcastReceiver {

    @Override
    public void onReceive(Context aContext, Intent aIntent) {

        Intent i = new Intent(aContext, MainActivity.class);  //MyActivity can be anything which you want to start on bootup...
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        aContext.startActivity(i);
    }
}

