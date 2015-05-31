package com.android.xpc.zxing.decode;

import com.android.xpc.zxing.decode.object.EncoderObject;
import com.google.zxing.WriterException;

import android.graphics.Bitmap;

public interface Encoder {

	Bitmap createQRCode(EncoderObject object) throws WriterException;
	
}
