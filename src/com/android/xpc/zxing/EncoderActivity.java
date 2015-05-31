package com.android.xpc.zxing;

import com.android.xpc.zxing.decode.IconQREncoder;
import com.android.xpc.zxing.decode.object.ContactEncoderObject;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class EncoderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView imageView = (ImageView) this.findViewById(R.id.testImage);
		
		Bitmap mBitmap = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.test)).getBitmap();
		
		IconQREncoder encoder = new IconQREncoder(200, mBitmap);
		try {
			ContactEncoderObject object = new ContactEncoderObject();
			object.setId(100);
			object.setEmail("187166772@qq.com");
			object.setName("wudi");
			object.setPhoneNumber("15948398000");
			Bitmap coder = encoder.createQRCode(object);
			imageView.setImageBitmap(coder);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
