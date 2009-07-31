package org.igs.android.ogl.engine.gui;

import java.util.ArrayList;
import java.util.List;

public class TouchMap {

	private List<TouchMapItem> touchMapItemList;
	
	public TouchMap() {
		touchMapItemList = new ArrayList<TouchMapItem>(5);
	}
	
	public void addTouchMapItem(TouchMapItem touchMapItem) {
		touchMapItemList.add(touchMapItem);
	}
	
	public void removeTouchMapItem(TouchMapItem touchMapItem) {
		this.touchMapItemList.remove(touchMapItem);
	}
	
	public void removeTouchMapItem(long id) {
		for (TouchMapItem item : this.touchMapItemList) {
			if (item.getId() == id) {
				this.touchMapItemList.remove(item);
				break;
			}
		}
	}
	
	private boolean verifyArea(float x, float y, TouchMapItem item, float touchX, float touchY) {
		return (touchX - x >= item.getX() && touchX - x <= item.getX() + item.getWidth()) && (touchY - y>= item.getY() && touchY - y <= item.getY() + item.getHeight());
	}
	
	public TouchMapItem verifyTouch(float x, float y, float touchX, float touchY) {
		for (TouchMapItem item : this.touchMapItemList) {
			if (this.verifyArea(x, y, item, touchX, touchY - 51)) {
				return item;
			}
		}
		return null;
	}
	
}
