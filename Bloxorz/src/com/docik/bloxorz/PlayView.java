package com.docik.bloxorz;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlayView extends SurfaceView implements SurfaceHolder.Callback {
	
	private SurfaceHolder surfaceHolder;
	
	private SurfaceManager surfaceManager;

	public PlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        
        surfaceManager = new SurfaceManager(surfaceHolder, context, 1);	//Start lvl
        setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceManager.setThreadState(true);
		surfaceManager.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		surfaceManager.setThreadState(false);
		while(retry)
		{
			try
			{
				surfaceManager.join();
				retry = false;
			}
			catch (InterruptedException e) { }
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		return surfaceManager.keyUp(keyCode);
	}
}
