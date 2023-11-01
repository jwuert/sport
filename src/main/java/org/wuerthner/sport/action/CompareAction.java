package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class CompareAction implements Action {
    @Override
    public String getId() {
        return "compare";
    }

    @Override
    public String getToolTip() {
        return "Shows modifications of the model";
    }

    @Override
    public boolean isEnabled(ModelElement selectedElement, ModelElementFactory factory) {
        return true;
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String,Object> result = new HashMap<>();
        System.out.println("invoke CompareAction: " + modelState.hasRootElement());
        if (modelState.hasRootElement()) {
            String filePath = parameterMap.get(Model.REFERENCE_FILE);
            if (filePath != null) {
                //
                // compare to reference file
                //
                System.out.println("Compare to reference file: " + filePath);
                try {
                    File referenceFile = new File(filePath);
                    XMLReader r = new XMLReader(factory, factory.getRootElementType(), "root");
                    ModelElement reference = r.run(new FileInputStream(referenceFile));
                    Comparison comp = new Comparison(modelState.getRootElement(), reference);
                    Delta diff = comp.diff();
                    result.put(DELTA, diff);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                //
                // compare to other reference
                //
                System.out.println("Compare root to aux element!");
                if (modelState.hasAuxiliaryElement()) {
                    System.out.println("yes!");
                    Comparison comp = new Comparison(modelState.getAuxiliaryElement(), modelState.getRootElement());
                    Delta diff = comp.diff();
                    result.put(DELTA, diff);
                }
            }
        }
        return result;
    }
}
