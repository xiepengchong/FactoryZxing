package com.android.xpc.zxing.display;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class RoundedBitmapDisplayer implements BitmapDisplayer{

	protected final int cornerRadius;
	protected final int margin;
	protected final int frameRadius;
	
	public RoundedBitmapDisplayer(int cornerRadiuspixels){
		this(cornerRadiuspixels,0);
		
	}
	
	public RoundedBitmapDisplayer(int cornerRadiusPixels, int marginPixels){
		this(cornerRadiusPixels,marginPixels,0);
	}
	
	public RoundedBitmapDisplayer(int cornerRadiusPixels, int marginPixels,int frameRadius){
		this.cornerRadius = cornerRadiusPixels;
		this.margin = marginPixels;
		this.frameRadius = frameRadius;
	}
	
	@Override
	public void display(Bitmap bitmap, ImageView imageView) {
		imageView.setImageDrawable(new RoundedDrawable(bitmap,cornerRadius,margin));
	}
	
	@Override
	public Bitmap getBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		RoundedDrawable drawable = new RoundedDrawable(bitmap,cornerRadius,margin);
		return drawable.toBitmap();
	}
	
	class RoundedDrawable extends Drawable{

		private final float cornerRadius;
		private final float margin;
		protected final RectF mBitmapRect,mRect = new RectF(),mAroundRect = new RectF();
		protected final BitmapShader bitmapShader;
		protected final Paint paint;
		protected final Paint framePaint;
		private final Bitmap mBitmap;
		public RoundedDrawable(Bitmap bitmap,int cornerRadius,int margin){
			this.cornerRadius = cornerRadius;
			this.margin = margin;
			mBitmap = bitmap;
			bitmapShader = new BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
			this.mBitmapRect = new RectF (margin, margin, bitmap.getWidth() - margin, bitmap.getHeight() - margin);
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setShader(bitmapShader);
			
			
			framePaint = new Paint();
			framePaint.setColor(Color.RED);
			framePaint.setAntiAlias(true);
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			mRect.set(margin+frameRadius, margin+frameRadius, bounds.width() - margin-frameRadius, bounds.height() - margin-frameRadius);
			mAroundRect.set(margin, margin, bounds.width() - margin, bounds.height() - margin);
			Matrix shaderMatrix = new Matrix();
			shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
			bitmapShader.setLocalMatrix(shaderMatrix);
			
			Matrix aroundMatrix = new Matrix();
			aroundMatrix.setRectToRect(mBitmapRect, mAroundRect, Matrix.ScaleToFit.FILL);
			bitmapShader.setLocalMatrix(aroundMatrix);
			
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawRoundRect(mAroundRect, cornerRadius, cornerRadius, framePaint);
			canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);
		}

		@Override
		public int getOpacity() {
			// TODO Auto-generated method stub
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
			paint.setAlpha(alpha);
			
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			paint.setColorFilter(cf);
		}
		
		public Bitmap toBitmap(){
			Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(),mBitmap.getHeight(),Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			this.setBounds(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
			this.draw(canvas);
			return bitmap;
		}
		
	}


}
