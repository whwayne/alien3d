package org.igs.android.ogl.engine.event;

import android.view.KeyEvent;

public interface KeyboardEventHandler {

	boolean onKeyDown(int keyCode, KeyEvent event);

	boolean onKeyUp(int keyCode, KeyEvent event);
	
	boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event);
	
}
