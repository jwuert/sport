package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.List;

public class FileAttribute extends AbstractAttribute<String> {
	private final boolean writeFile;
	public FileAttribute(String name, String label, String defaultValue, boolean readonly, boolean required, boolean hidden, boolean writeFile, String description, List<Check> dependencies,
                         List<Check> validators) {
		super(name, label, String.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
		this.writeFile = writeFile;
	}
	
	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}

	public boolean write() {
		return writeFile;
	}
}
