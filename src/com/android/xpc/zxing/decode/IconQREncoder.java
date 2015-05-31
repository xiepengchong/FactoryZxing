package com.android.xpc.zxing.decode;

import java.util.EnumMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.android.xpc.zxing.R;
import com.android.xpc.zxing.decode.object.EncoderObject;
import com.android.xpc.zxing.display.BitmapDisplayer;
import com.android.xpc.zxing.display.RoundedBitmapDisplayer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class IconQREncoder implements Encoder{

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static final int OUTLINE = 0x66000000;
	private static final int FRAMECOLOR = 0xFF00FFFF;

	protected final int mQrImageWidth;
	protected final int mQrImageHeight;
	protected final int mIconWidth;
	protected final int mIconHeight;
	protected final int mDarkColor,mLightColor;
	protected final boolean mHasFrame;
	protected final Bitmap mBitmap;
	public IconQREncoder(int imageWidthAndHeight,Bitmap bitmap){
		this(imageWidthAndHeight,imageWidthAndHeight,BLACK,WHITE,true,bitmap);
	}
	
	public IconQREncoder(int imageWidth,int imageHeight,Bitmap bitmap){
		this(imageWidth,imageHeight,BLACK,WHITE,true,bitmap);
	}
	
	public IconQREncoder(int imageWidth,int imageHeight,int darkColor,int lightColor,boolean hasFrame,Bitmap bitmap){
		mQrImageWidth = imageWidth;
		mQrImageHeight = imageHeight;
		mDarkColor = darkColor;
		mLightColor = lightColor;
		mHasFrame = hasFrame;
		mBitmap = bitmap;
		
		int bitWidth = bitmap.getWidth();
		int bitHeight = bitmap.getHeight();
		int rate = bitWidth >= bitHeight ? bitWidth/bitHeight : bitHeight/bitWidth;
		
		mIconWidth = mQrImageWidth/6/rate;
		mIconHeight = mQrImageHeight/6/rate; // the max value is 5,or it will hard to be recognize
	}
	
	@Override
	public Bitmap createQRCode(EncoderObject object) throws WriterException {

		String QRInformation;
		if(object != null)
			QRInformation = object.toJsonString();
		else
			QRInformation = "NULL OBJECT";
		
		Bitmap scaleImage = Bitmap.createScaledBitmap(mBitmap, mIconWidth, mIconHeight, false);
		
		int[][] srcPixels = new int[mIconWidth][mIconHeight];
		for (int i = 0; i < scaleImage.getWidth(); i++) {
			for (int j = 0; j < scaleImage.getHeight(); j++) {
				srcPixels[i][j] = scaleImage.getPixel(i, j);
			}
		}
		
	    Map<EncodeHintType,Object> hints = null;
	    hints = new EnumMap<>(EncodeHintType.class);
	    hints.put(EncodeHintType.MARGIN, 0); //set the margin value.
	    String encoding = guessAppropriateEncoding(QRInformation);
	    if (encoding != null) {
	      hints.put(EncodeHintType.CHARACTER_SET, encoding);
	    }
		
		BitMatrix matrix = new MultiFormatWriter().encode(QRInformation,
				BarcodeFormat.QR_CODE, mQrImageWidth, mQrImageHeight,hints);
		
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int halfH = height/2;
		int halfW = width/2;
		
		int halfIconH = mIconHeight/2;
		int halfIconW = mIconWidth/2;
		
		int frameH = 3;  //the frame height
		
		int[] pixels = new int[width * height];
		for(int y=0;y < height;y++){
			int offset_y = y*width;
			for(int x=0;x < width;x++){
				if(y > halfH-halfIconH 
						&& y < halfH+halfIconH 
						&& x > halfW-halfIconW 
						&& x < halfW+halfIconW){        // to calculate the icon pixels
					pixels[offset_y+x] = srcPixels[x-halfW+halfIconW][y-halfH+halfIconH];
				}else if((y==halfH-halfIconH||y==halfH+halfIconH)&&(x>=halfW-halfIconW&&x<=halfW+halfIconW)
						||((x==halfW-halfIconW||x==halfW+halfIconW)&&(y>=halfH-halfIconH&&y<=halfH+halfIconH))){  //calculate the outline of the icon
					pixels[offset_y+x] = OUTLINE;
					
				}else if((((x>halfW-halfIconW-frameH&&x<halfW-halfIconW)
						    ||(x>halfW+halfIconW&&x<halfW+halfIconW+frameH))
						    &&(y>halfH-halfIconH-frameH&&y<halfH+halfIconH+frameH))
						    ||((x>halfW-halfIconW-frameH&&x<halfW+halfIconW+frameH)
							&&((y>halfH-halfIconH-frameH&&y<halfH-halfIconH)
							||(y>halfH+halfIconH&&y<halfH+halfIconH+frameH)))){  //calculate the frame area
					pixels[offset_y+x] = FRAMECOLOR;
				}else if((y==halfH-halfIconH-frameH||y==halfH+halfIconH+frameH)&&(x>=halfW-halfIconW-frameH&&x<=halfW+halfIconW+frameH)
						||((x==halfW-halfIconW-frameH||x==halfW+halfIconW+frameH)&&(y>=halfH-halfIconH-frameH&&y<=halfH+halfIconH+frameH))){  //calculate the outline of frame
					pixels[offset_y+x] = OUTLINE;
				}
				else{  //calculate the QR area
					pixels[offset_y+x] = matrix.get(x, y) ? mDarkColor:mLightColor;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	
	  private static String guessAppropriateEncoding(CharSequence contents) {
		    // Very crude at the moment
		  
		    for (int i = 0; i < contents.length(); i++) {
		    	Log.v("xpc","contents.charAt(i)="+contents.charAt(i));
		      if (contents.charAt(i) > 0xFF) {
		        return "UTF-8";
		      }
		    }
		    return null;
		  }
	
}
