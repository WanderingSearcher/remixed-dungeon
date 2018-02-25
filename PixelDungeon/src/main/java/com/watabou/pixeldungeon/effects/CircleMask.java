package com.watabou.pixeldungeon.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;

public class CircleMask extends Image {

	private static final Object CACHE_KEY = CircleMask.class;

	protected static final int RADIUS	= 64;

	protected float radius = RADIUS;
	protected float brightness = 1;

	public CircleMask() {
		if (!TextureCache.contains( CACHE_KEY )) {
			Bitmap bmp = Bitmap.createBitmap( RADIUS * 2, RADIUS * 2, Bitmap.Config.ARGB_8888 );
			Canvas canvas = new Canvas( bmp );
			Paint paint = new Paint();
			canvas.drawColor(Color.BLACK, PorterDuff.Mode.SRC);

			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
			paint.setColor( 0x77ffffff);
			canvas.drawCircle( RADIUS, RADIUS, RADIUS, paint );

			paint.setColor( Color.TRANSPARENT);
			canvas.drawCircle( RADIUS, RADIUS, RADIUS*0.5f, paint );

			TextureCache.add( CACHE_KEY, new SmartTexture( bmp ) );
		}

		texture( CACHE_KEY );

		origin.set( RADIUS );
	}

	public CircleMask(float radius) {
		
		this();
		radius( radius );
	}
	
	public CircleMask point(float x, float y ) {
		this.x = x - RADIUS;
		this.y = y - RADIUS;
		return this;
	}
	
	public void radius( float value ) {
		scale.set(  (this.radius = value) / RADIUS );
	}
}