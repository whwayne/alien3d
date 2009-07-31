package org.igs.android.ogl.engine.event;

import android.view.MotionEvent;

public interface TouchAreaEventHandler {

	boolean onTouchAreaDownEvent(long id, MotionEvent event);
	
	boolean onTouchAreaUpEvent(long id, MotionEvent event);
	
	boolean onTouchAreaLeaveEvent(long id, MotionEvent event);
	
}
