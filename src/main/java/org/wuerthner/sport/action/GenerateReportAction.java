package org.wuerthner.sport.action;

//import org.wuerthner.ereport.Report;
//import org.wuerthner.ereport.SubReport;
//import org.wuerthner.ereport.generator.fop.FOPBuilder;
import org.wuerthner.sport.api.*;
import org.wuerthner.sport.attribute.IdAttribute;
import org.wuerthner.sport.core.ModelState;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateReportAction implements Action {
    @Override
    public String getId() {
        return "configurationReport";
    }

    @Override
    public String getToolTip() {
        return "Generates a report";
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String, Object> result = new HashMap<>();
        /*
        if (modelState.hasRootElement()) {
            Report report = new Report(FOPBuilder.class);
            report.addFilename("report", factory.getAppName())
                    .addTitle(factory.getAppName())
                    .addHeader("Report", new SimpleDateFormat("dd-MMM-yyyy").format(new Date()))
                    .addIntroduction("This report lists the configuration!");

            ModelElement root = modelState.getRootElement();
            List<String> typeList = factory.createElementList().stream().map(el -> el.getType()).collect(Collectors.toList());
            for (String type : typeList) {
                List<ModelElement> elementList = root.lookupByType(type);
                if (!elementList.isEmpty()) {
                    ModelElement prototype = elementList.get(0);
                    Attribute<?>[] attributes = prototype.getAttributes();
                    String[] attributeNames = Arrays.stream(attributes).map(a -> a.getName()).toArray(String[]::new);
                    SubReport subReport = report.addTable(type).setHeader(attributeNames);
                    for (ModelElement element : elementList) {
                        String[] attributeValues = Arrays.stream(attributes).map(a -> {
                            Object attributeValue = element.getAttributeValue(a);
                            if (a.getName().equals(IdAttribute.ID_NAME)) {
                                return element.getFullId();
                            } else {
                                return "" + (attributeValue == null ? "" : attributeValue);
                            }
                        }).toArray(String[]::new);
                        subReport.addRow(attributeValues);
                    }
                }
            }


            // .setColumnWidth(10, 20, 10, 40)

//                    .addRow(1, "Peter", 22, "nice guy")
//                    .addRow(2, "Harry", 24, "not so nice guy")
//                    .addSeparator()
//                    .addRow(3, "Marie", 23.5, "nice girl")
//                    .setFooter("This is just an example! Harry is actually quite a nice guy!");

            String fileAndPathName = report.generateFile();
            File pdfFile = new File(fileAndPathName);
            Optional<Executor<File>> pdfViewerExecutor = factory.getPdfViewerExecutor();
            if (pdfViewerExecutor.isPresent()) {
                pdfViewerExecutor.get().run(pdfFile);
            }
        }
        */
        return result;
    }
}
