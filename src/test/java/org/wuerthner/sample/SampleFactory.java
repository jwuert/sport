package org.wuerthner.sample;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.api.UserProvider;
import org.wuerthner.sport.core.EmptyUserProvider;

public class SampleFactory implements ModelElementFactory {
	public final static List<ModelElement> elementList = Arrays.asList(new ModelElement[] {
			new School(), new Course(), new Participant(), new ListObject()
	});
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ModelElement> T createElement(String typeName) {
		T element;
		switch (typeName) {
			case School.TYPE: element = (T) new School(); break;
			case Course.TYPE: element = (T) new Course(); break;
			case Participant.TYPE: element = (T) new Participant(); break;
			case ListObject.TYPE: element = (T) new ListObject(); break;
			case TestObject.TYPE: element = (T) new TestObject(); break;
			default:
				throw new RuntimeException("Invalid element type: " + typeName);
		}
		return element;
	}
	
	@Override
	public List<ModelElement> createElementList(UserProvider userProvider) {
		return elementList;
	}
	
	@Override
	public String getRootElementType() {
		return School.TYPE;
	}

    @Override
    public Map<String, String> getUserMap() {
        return new HashMap<>();
    }

}
