package com.anwajler.sensortester;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorTester extends Activity {
    Context ctx;
    TextView content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ctx = getApplicationContext();
        content = (TextView) findViewById(R.id.content);
        
		SensorManager sm = (SensorManager)ctx.getSystemService(SENSOR_SERVICE);
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
        
        String output = new String();
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
    }
}