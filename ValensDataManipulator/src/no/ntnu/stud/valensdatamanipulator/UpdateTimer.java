package no.ntnu.stud.valensdatamanipulator;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class UpdateTimer extends Activity {
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front);
        mHandler = new Handler();
        
        /* Get Last Update Time from Preferences */
        SharedPreferences prefs = getPreferences(0);
        Long lastUpdateTime =  prefs.getLong("lastUpdateTime", 0);
        
        /* Should Activity Check for Updates Now? */
//        if ((lastUpdateTime + (24 * 60 * 60 * 1000)) < System.currentTimeMillis()) {
        if ((lastUpdateTime + (60 * 1000)) < System.currentTimeMillis()) {

            /* Save current timestamp for next Check*/
            lastUpdateTime = System.currentTimeMillis();            
            SharedPreferences.Editor editor = getPreferences(0).edit();
            editor.putLong("lastUpdateTime", lastUpdateTime);
            editor.commit();        

            /* Start Update */            
            checkUpdate.start();
        }
    }
    
    /* This Thread checks for Updates in the Background */
    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
                URL updateURL = new URL("http://my.company.com/update");                
                URLConnection conn = updateURL.openConnection(); 
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                
                int current = 0;
                while((current = bis.read()) != -1){
                     baf.append((byte)current);
                }

                /* Convert the Bytes read to a String. */
                final String s = new String(baf.toByteArray());         
                
                /* Get current Version Number */
                int curVersion = getPackageManager().getPackageInfo("your.app.id", 0).versionCode;
                int newVersion = Integer.valueOf(s);
                
                /* Is a higher version than the current already out? */
                if (newVersion > curVersion) {
                    /* Post a Handler for the UI to pick up and open the Dialog */
                    mHandler.post(showUpdate);
                }                
            } catch (Exception e) {
            }
        }
    };

    /* This Runnable creates a Dialog and asks the user to open the Market */ 
    private Runnable showUpdate = new Runnable(){
           public void run(){
            new AlertDialog.Builder(UpdateTimer.this)
            .setIcon(R.drawable.ic_launcher)
            .setTitle("Update Available")
            .setMessage("An update for is available!\\n\\nOpen Android Market and see the details?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                            /* User clicked OK so do some stuff */
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:your.app.id"));
                            startActivity(intent);
                    }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                            /* User clicked Cancel */
                    }
            })
            .show();
           }
    };    
}