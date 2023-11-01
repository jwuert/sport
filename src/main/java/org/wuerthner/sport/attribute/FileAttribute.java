package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.Chooser;

public class FileAttribute extends AbstractAttribute<String,FileAttribute,Chooser> implements Chooser {
	private boolean writeFile = false;

	public FileAttribute(String name) {
		super(name, String.class, Chooser.class);
	}

	public FileAttribute writeFile() {
		this.writeFile = true;
		return this;
	}

	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}

	public boolean write() {
		return writeFile;
	}
}
