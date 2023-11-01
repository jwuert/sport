package org.wuerthner.sport.action;

import org.wuerthner.sport.api.*;
import org.wuerthner.sport.attribute.FileAttribute;
import org.wuerthner.sport.core.ModelState;
import org.wuerthner.sport.core.XMLReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportAction implements Action {
    public final static String PARAMETER_FILE = "file";

    @Override
    public String getId() {
        return "import";
    }

    @Override
    public String getToolTip() {
        return "Imports a document";
    }

    @Override
    public List<Attribute<?>> getParameterList(ModelElement selectedElement) {
        List<Attribute<?>> parameterList = new ArrayList<>();
        Attribute<?> attribute = new FileAttribute(PARAMETER_FILE)
                .label("File");
        parameterList.add(attribute);
        return parameterList;
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String,Object> result = new HashMap<>();
        try {
            String fileName = parameterMap.get(PARAMETER_FILE);
            if (fileName != null) {
                FileInputStream inputStream = new FileInputStream(fileName);
                XMLReader reader = new XMLReader(factory, factory.getRootElementType(), "root");
                ModelElement root = (ModelElement) reader.run(inputStream);
                result.put(ROOT, root);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result.put(ERROR, e.getMessage());
        }
        return result;
    }
}
