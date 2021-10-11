package org.wuerthner.sport.util;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.Comparison;
import org.wuerthner.sport.core.Delta;
import org.wuerthner.sport.core.XMLReader;

import java.io.File;
import java.io.FileInputStream;

public class Comparator {
    public static void main(String[] args) throws Exception {
        if (args.length == 5) {
            File f1 = new File(args[0]);
            File f2 = new File(args[1]);
            ModelElementFactory factory = (ModelElementFactory) Class.forName(args[2]).newInstance();
            String rootType = args[3];
            String rootElementName = args[4];
            System.out.println("Comparison of " + f1.getName() + " and " + f2.getName());
            System.out.println("factory: " + factory.getClass().getSimpleName() + ", rootType: " + rootType + ", rootElementName: " + rootElementName);
            XMLReader r = new XMLReader(factory, rootType, rootElementName);
            ModelElement tree1 = r.run(new FileInputStream(f1));
            ModelElement tree2 = r.run(new FileInputStream(f2));
            Comparison comp = new Comparison(tree1, tree2);
            Delta diff = comp.diff();
            String differencesInText = diff.getDifferencesInText();
            System.out.println(differencesInText);
        } else {
            System.out.println("Usage: Comparator file1 file2 factory rootType rootElementName");
        }
    }
}
