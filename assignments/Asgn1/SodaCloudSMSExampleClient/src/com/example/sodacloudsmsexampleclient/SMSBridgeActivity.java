package com.example.sodacloudsmsexampleclient;

import org.magnum.soda.Soda;
import org.magnum.soda.android.AndroidSoda;
import org.magnum.soda.android.AndroidSodaConnectionException;
import org.magnum.soda.android.AndroidSodaListener;
import org.magnum.soda.android.SodaInvokeInUi;
import org.magnum.soda.example.sms.SMSListener;
import org.magnum.soda.example.sms.SMSManager;
import org.magnum.soda.example.sms.SMSManagerImpl;
import org.magnum.soda.protocol.generic.DefaultProtocol;
import org.magnum.soda.proxy.ObjProxy;
import org.magnum.soda.proxy.ObjRef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SMSBridgeActivity extends Activity implements AndroidSodaListener {

	private Soda soda_;
	
	private String objRef_;
	
	private TextView status_;
	
	private SMSManager smsManager_;
	
	/**
	 * Asgn Step 8: Instantiate an instance of your
	 * Module implementation and assign it to the
	 * configuration variable. Note, your module instance
	 * should be configured to map:
	 * 
	 * SMSManager --> SMSManagerImpl
	 * ObjRefExtractor --> QRCodeObjRefExtractor
	 * 
	 */
	private Module configuration_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smsbridge);

		status_ = (TextView)findViewById(R.id.status);
		
		String ref = getIntent().getStringExtra("ref");
		String server = ref.substring(0,ref.indexOf("|"));
		objRef_ = ref.substring(ref.indexOf("|")+1);
		
		AndroidSoda.init(this, new DefaultProtocol(), server, "/sms/", 8081, this);
	}

	public void connected(AndroidSoda s) {
		soda_ = s;
		
		s.inUi(new Runnable() {
			
			@Override
			public void run() {
				status_.setText("Connected.");
			}
		});
		
		smsManager_ = configuration_.getComponent(SMSManager.class);
		
		ObjRef ref = ObjRef.fromObjUri(objRef_);
		SMSListener l = soda_.get(SMSListener.class, ref);
		smsManager_.addListener(l);
	}

	@Override
	public void connectionFailure(AndroidSoda s,
			AndroidSodaConnectionException ex) {
		Toast.makeText(this, "Unable to connect to server.", Toast.LENGTH_LONG);
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
	}

}
