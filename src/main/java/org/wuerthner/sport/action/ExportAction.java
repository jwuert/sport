package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.attribute.FileAttribute;
import org.wuerthner.sport.core.ModelState;
import org.wuerthner.sport.core.XMLWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportAction implements Action {
    public final static String PARAMETER_FILE = "file";

    @Override
    public String getId() {
        return "export";
    }

    @Override
    public String getToolTip() {
        return "Export a document";
    }

    @Override
    public List<Attribute<?>> getParameterList(ModelElement selectedElement) {
        List<Attribute<?>> parameterList = new ArrayList<>();
        Attribute<?> attribute = new FileAttribute(PARAMETER_FILE)
                .label("File")
                .writeFile();
        parameterList.add(attribute);
        return parameterList;
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String,Object> result = new HashMap<>();
        if (modelState.hasRootElement()) {
            try {
                String fileName = parameterMap.get(PARAMETER_FILE);
                if (fileName != null) {
                    XMLWriter xmlWriter = new XMLWriter();
                    OutputStream outputStream = new FileOutputStream(fileName);
                    xmlWriter.run(modelState.getRootElement(), outputStream);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result.put(ERROR, e.getMessage());
            }
        } else {
            result.put(ERROR, "Nothing to export!");
        }
        return result;
    }
}
