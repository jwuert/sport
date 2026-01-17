package org.wuerthner.sport.action;

import org.wuerthner.sport.api.*;
import org.wuerthner.sport.core.EmptyUserProvider;
import org.wuerthner.sport.core.Model;
import org.wuerthner.sport.core.ModelState;
import org.wuerthner.sport.util.fop.FOPBuilder;
import org.wuerthner.sport.util.fop.FOPProcessor;

import java.io.File;
import java.util.*;

public class GenerateModelReportAction implements Action {
    @Override
    public String getId() {
        return "modelReport";
    }

    @Override
    public String getToolTip() {
        return "Generates a report of the data model";
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        if (factory != null) {
            // List<String> modelChanges = updateProvider.getChanges();
            List<String> modelChanges = new ArrayList<>();
            String modelVersion = "?";
            ActionProvider actionProvider = factory.getActionProvider();
            FOPBuilder fb = new FOPBuilder(factory.getAppName(), "Model: " + modelVersion, actionProvider, modelChanges);

            // List<ModelElement> list = Stream.of(typesReduced).map(type -> (ModelElement) factory.createElement(type)).collect(Collectors.toList());
            List<ModelElement> list = factory.createElementList(new EmptyUserProvider());
            File fopFile = fb.collect(list);

            File pdfFile = new File(fopFile.getAbsolutePath().replaceAll("\\.fop","-X.pdf"));

            System.out.println("Specification file: " + pdfFile.getAbsolutePath());
            FOPProcessor fp = new FOPProcessor(fopFile, pdfFile);
            fp.run();

            Optional<Executor<File>> pdfViewerExecutor = factory.getPdfViewerExecutor();
            if (pdfViewerExecutor.isPresent()) {
                pdfViewerExecutor.get().run(pdfFile);
            }
        }
        return new HashMap<>();
    }
}
