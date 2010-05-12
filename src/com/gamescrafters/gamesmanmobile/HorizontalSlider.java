package com.gamescrafters.gamesmanmobile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

public class HorizontalSlider extends ProgressBar {

	private OnProgressChangeListener listener;

	private static int padding = 2;

	public interface OnProgressChangeListener {
		void onProgressChanged(View v, int progress);
	}

	public HorizontalSlider(Context context, AttributeSet attrs) {
		super(context, attrs, android.R.attr.progressBarStyleHorizontal);
	}

	public void setOnProgressChangeListener(OnProgressChangeListener l) {
		listener = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_MOVE) {
			float x_mouse = event.getX() - padding;
			float width = getWidth() - 2*padding;
			int progress = Math.round((float) getMax() * (x_mouse / width));
			
			if (progress < 0)
				progress = 0;

			this.setProgress(progress);
			
			if (listener != null)
				listener.onProgressChanged(this, progress);

		}

		return true;
	}

	public void updateProgress(int currentMove, int movesSoFar) {
		int progress = Math.round((float) getMax() * ((float) currentMove / (float) movesSoFar));
		
		this.setProgress(progress);
	}
	
	public int getPadding() {
		return padding;
	}
	
	public OnProgressChangeListener getListener() {
		return listener;
	}
}
