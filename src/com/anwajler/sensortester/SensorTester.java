package com.anwajler.sensortester;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build.VERSION;


public class SensorTester extends Activity {
    Context ctx;
    EditText content;
    Button share;
    String ver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ctx     = getApplicationContext();
        content = (EditText) findViewById(R.id.content);
        share   = (Button) findViewById(R.id.share);
        ver     = new VERSION().RELEASE;
        
		SensorManager sm          = (SensorManager)ctx.getSystemService(SENSOR_SERVICE);
        List <Sensor> sensorsList = (List) sm.getSensorList(Sensor.TYPE_ALL);
        ArrayList<Sensor> sensors = new ArrayList(sensorsList);
        
        // for API < 9 
        Method check;
        boolean hasMinDelay = false;

        try {
            check = Sensor.class.getMethod(
                    "getMinDelay", new Class[] { String.class } );
            hasMinDelay = true;
        } catch (NoSuchMethodException nsme) {
        	// API < 9, no getMinDelay method available
        }
        
        String output = new String(android.os.Build.MANUFACTURER + "\n" +
        						   android.os.Build.DEVICE       + "\n" +
        						   android.os.Build.MODEL        + "\n" +
        						   android.os.Build.PRODUCT      + "\n" +
        						   ver+"\n");
        if(hasMinDelay == true) {
            for(Sensor s: sensors){
            	output += 		 
            	"\nName: "       + s.getName()         + 
            	"\nMax range: "  + s.getMaximumRange() + 
            	"\nMin delay: "  + s.getMinDelay()     + 
            	"\nPower: " 	 + s.getPower()        +
            	"\nResolution: " + s.getResolution()   +
            	"\nVendor: "     + s.getVendor()       +
            	"\nVersion: "    + s.getVersion()      +
            	"\n\n";
            }        	
        } else {
            for(Sensor s: sensors){
            	output += 
            	"\nName: "       + s.getName()         + 
            	"\nMax range: "  + s.getMaximumRange() + 
            	// no minDelay
            	"\nPower: " 	 + s.getPower()        +
            	"\nResolution: " + s.getResolution()   +
            	"\nVendor: "     + s.getVendor()       +
            	"\nVersion: "    + s.getVersion()      +
            	"\n\n";
            }        	
        }
        content.setText(output);
        
        // event handler
        share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/html");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Sensor Tester");
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, content.getText());
				shareIntent.setClassName("com.android.email", 
				"com.android.email.activity.MessageCompose");				
				startActivity(Intent.createChooser(shareIntent, "Share your device details"));				
			}
		});
        
    }
}