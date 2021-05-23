package org.wuerthner.sport.api;

public interface Operation {
	public boolean undoable();
	
	public void execute();
	
	public void undo();
	
	public String info();
}
