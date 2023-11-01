package org.wuerthner.sport.util.fop;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.wuerthner.sport.api.*;
import org.wuerthner.sport.api.attributetype.StaticMapping;
import org.wuerthner.sport.core.Model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class FOPBuilder {
    public final static String TABLE_OF_CONTENTS = "Table of Contents";
    public final static List<String> description = Arrays.asList(
            "This report lists all elements available in the data model."
    );
    public final static String DATE_FORMAT = "dd MMM yyyy";
    public final static String EXCLUDED_ACTIONS = ":downloadStudymodel:reset:testName:debug:testNameDisabled:separator:";
    private final static String TABLE_HEADER_COLOR = "#ccdadc";
    private final static String TABLE_BORDER = "solid 2pt " + TABLE_HEADER_COLOR;
    private final boolean header = true;
    private final boolean footer = true;
    private Document document;
    private final String projectName;
    private final String projectVersion;
    private final List<Action> actionList;
    private final List<String> modelChanges;

    public FOPBuilder(String projectName, String projectVersion, ActionProvider actionProvider, List<String> modelChanges) {
        this.projectName = projectName;
        this.projectVersion = projectVersion;
        this.modelChanges = modelChanges;
        this.actionList = actionProvider == null ? new ArrayList<>() : actionProvider
                .getActionList()
                .stream()
                .filter(action -> !EXCLUDED_ACTIONS.contains(":"+action.getId()+":"))
                .collect(Collectors.toList());
    }

    public File collect(List<ModelElement> list) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        File foFile = null;
        try {
            File specFile = new File("specification");
            if (!specFile.exists()) {
                specFile.mkdir();
            } else if (!specFile.isDirectory()) {
                throw new RuntimeException("output directory 'specification' exists but is not a directory!");
            }
            foFile = new File(specFile, "elementsReport.fop");

            // builder
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.newDocument();
            Element rootElement = document.createElementNS("http://www.w3.org/1999/XSL/Format", "fo:root");
            document.appendChild(rootElement);
            rootElement.setAttribute("font-family", "Helvetica");
            // layout
            rootElement.appendChild(createLayout());
            // <fo:page-sequence master-reference="simple">
            Element pageSequence = document.createElement("fo:page-sequence");
            pageSequence.setAttribute("master-reference", "simple");
            rootElement.appendChild(pageSequence);
            // header
            if (header) {
                pageSequence.appendChild(createHeader());
            }
            // footer
            if (footer) {
                pageSequence.appendChild(createFooter());
            }
            // body
            // <fo:flow flow-name="xsl-region-body">
            Element bodyFlow = document.createElement("fo:flow");
            bodyFlow.setAttribute("flow-name", "xsl-region-body");
            pageSequence.appendChild(bodyFlow);
            //
            // TITLE:
            //
            bodyFlow.appendChild(makeDocumentTitle(list));
            bodyFlow.appendChild(makeIntroduction());
            //
            // ELEMENT CONTENT:
            //
            List<String> typeList = list.stream().map(el -> el.getType()).collect(Collectors.toList());
            for (ModelElement entry : list) {
                ModelElement element = (ModelElement) entry;

                // child types:
                List<String> childTypes = element.getChildTypes();
                List<List<String>> childTypePathList = new ArrayList<>();
                for (String child: childTypes) {
                    if (typeList.contains(child)) {
                        childTypePathList.add(Arrays.asList(new String[]{child}));
                    }
                }
                // upstream types:
                List<List<String>> upstreamTypeList = getUpstreamTypeList(element, list, "");
                Collections.sort(upstreamTypeList, new java.util.Comparator<List<String>>() {
                    @Override
                    public int compare(List<String> o1, List<String> o2) {
                        return o1.toString().compareTo(o2.toString());
                    }
                });
                bodyFlow.appendChild(makeTitle(element.getType(), ""));
                if (!upstreamTypeList.isEmpty()) {
                    bodyFlow.appendChild(makeInfoBox("Occurrence of element type '" + element.getType() + "' in the studymodel:", upstreamTypeList, "#f0e1b6"));
                }
                if (!childTypePathList.isEmpty()) {
                    bodyFlow.appendChild(makeInfoBox("Child element types for '" + element.getType() + "' are:", childTypePathList, "#eaf7ce"));
                }
                if (!element.getDescription().isEmpty()) {
                    bodyFlow.appendChild(makeDescription("Remarks:"));
                    for (String descr : element.getDescription()) {
                        bodyFlow.appendChild(makeDescription("\u2022 " + descr));
                    }
                }
                Node table = openAttributesTable();
                Element body = document.createElement("fo:table-body");
                table.appendChild(body);
                makeRows(element, body);
                bodyFlow.appendChild(table);
            }
            //
            // ACTION CONTENT:
            //
            for (Action action: actionList) {
                bodyFlow.appendChild(makeTitle(action.getToolTip(), "A_"));
                if (!action.getDescription().isEmpty()) {
                    bodyFlow.appendChild(makeDescription("Remarks:"));
                    for (String descr : action.getDescription()) {
                        bodyFlow.appendChild(makeDescription("\u2022 " + descr));
                    }
                }
                if (action.getParameterList(null).size()>0) {
                    Node table = openActionTable();
                    Element body = document.createElement("fo:table-body");
                    table.appendChild(body);
                    makeActionRows(action, body);
                    bodyFlow.appendChild(table);
                }
            }
            //
            // MODEL CHANGES:
            //
            bodyFlow.appendChild(makeTitle("Model Update", "MU_"));
            bodyFlow.appendChild(makeDescription("Changes for " + projectName + " " + projectVersion + ":"));

            if (modelChanges.isEmpty()) {
                bodyFlow.appendChild(makeDescription("N/A"));
            } else {
                for (String change: modelChanges) {
                    bodyFlow.appendChild(makeDescription("\u2022 " + change));
                }
            }
            //
            // END
            //

            // end reference
            // <fo:block id="theEnd"></fo:block>
            Element endBlock = document.createElement("fo:block");
            endBlock.setAttribute("id", "theEnd");
            bodyFlow.appendChild(endBlock);

            // for output to file, console
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);

            // write to console or file
            // StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(foFile);

            // write data
            // transformer.transform(source, console);
            transformer.transform(source, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return foFile;
    }

    private Node makeInfoBox(String header, List<List<String>> typePathList, String color) {
        Element box = document.createElement("fo:block");
        box.setAttribute("border-width", "1px");
        box.setAttribute("border-color", "grey");
        box.setAttribute("border-style", "solid");
        box.setAttribute("space-after", "4pt");
        box.setAttribute("background-color", color);
        box.setAttribute("padding", "6pt");
        box.setAttribute("margin", "1pt");

        Element description = makeDescription(header);
        description.setAttribute("space-before.optimum", "1pt");
        description.setAttribute("space-after.optimum", "3pt");
        box.appendChild(description);

        Element content = document.createElement("fo:block");
        for (List<String> typePath : typePathList) {
            Element block = document.createElement("fo:block");
            boolean first = true;
            for (String type : typePath) {
                // Element data = makeLinkBlock(entry);
                Element data = makeLink(type, "");
                data.setAttribute("font-family", "monospace");
                data.setAttribute("font-size", "10pt");
                data.setAttribute("padding", "0mm");
                data.setAttribute("margin", "0mm");
                data.setAttribute("space-before.optimum", "1pt");
                data.setAttribute("space-after.optimum", "1pt");
                if (!first) {
                    block.appendChild(document.createTextNode(" > "));
                }
                block.appendChild(data);
                first = false;
            }
            content.appendChild(block);
        }
        box.appendChild(content);
        return box;
    }

    private Node makeIntroduction() {
        Element intro = document.createElement("fo:block");
        intro.setAttribute("space-after", "40pt");
        // title
        Element block = document.createElement("fo:block");
        block.setAttribute("line-height", "12pt");
        // block.setAttribute("font-weight", "bold");
        block.setAttribute("font-size", "10pt");
        block.setAttribute("text-align", "left");
        block.setAttribute("space-before.optimum", "4pt");
        block.setAttribute("space-after.optimum", "10pt");
        for (String descr : description) {
            block.appendChild(makeDescription("\u2022 " + descr));
        }
        // block.setTextContent(description);
        intro.appendChild(block);
        return intro;
    }

    private Node makeDocumentTitle(List<ModelElement> list) {
        Element documentHeader = document.createElement("fo:block");
        documentHeader.setAttribute("id", projectName);
        //documentHeader.setAttribute("space-after", "10pt");

        // title
        Element block = document.createElement("fo:block");
        block.setAttribute("line-height", "60pt");
        block.setAttribute("font-weight", "bold");
        block.setAttribute("font-size", "20pt");
        block.setAttribute("color", "#128474");
        block.setAttribute("text-align", "center");
        // block.setAttribute("space-after", "-15pt");
        block.setTextContent("Elements Report for " + projectName + " " + projectVersion);
        documentHeader.appendChild(block);

        // table
//          Element table = document.createElement("fo:table");
//          table.setAttribute("border-collapse", "separate");
//          table.setAttribute("space-before", "20pt");
//          table.setAttribute("space-after", "10pt");
//
//          // columns and widths:
//          Element column = document.createElement("fo:table-column");
//          column.setAttribute("column-width", "55mm");
//          table.appendChild(column);
//          column = document.createElement("fo:table-column");
//          column.setAttribute("column-width", "202mm");
//          table.appendChild(column);
//          Element body = document.createElement("fo:table-body");
//          // body.appendChild(makeDocumentTitleTableRow("Author", author));
//          body.appendChild(makeDocumentTitleTableRow("Date",
//                      new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
//                                  .format(new Date())));
//          table.appendChild(body);
//          documentHeader.appendChild(table);

        // TOC
        Node toc = createTableOfContents(list);
        documentHeader.appendChild(toc);

        // RULE
        // <fo:leader leader-pattern="rule" rule-thickness="0.5pt"
        // leader-length="15cm" space-after.optimum="2pt" color="#000050" />

        Element leader = document.createElement("fo:leader");
        leader.setAttribute("leader-pattern", "rule");
        leader.setAttribute("rule-thickness", "0.5pt");
        leader.setAttribute("leader-length", "25.7cm");
        leader.setAttribute("color", "#128474");
        documentHeader.appendChild(leader);

        //
        return documentHeader;
    }

    private Element makeDocumentTitleTableRow(String key, String value) {
        Element row = document.createElement("fo:table-row");
        {
            Element cell = document.createElement("fo:table-cell");
            cell.setAttribute("border", "solid 1px black");
            cell.setAttribute("padding", "3pt");
            Element cellBlock = document.createElement("fo:block");
            cellBlock.setAttribute("font-size", "12pt");
            cellBlock.setTextContent(key);
            cell.appendChild(cellBlock);
            row.appendChild(cell);
        }
        {
            Element cell = document.createElement("fo:table-cell");
            cell.setAttribute("border", "solid 1px black");
            cell.setAttribute("padding", "3pt");
            Element cellBlock = document.createElement("fo:block");
            cellBlock.setAttribute("font-size", "12pt");
            cellBlock.setTextContent(value);
            cell.appendChild(cellBlock);
            row.appendChild(cell);
        }
        return row;
    }

    private Node makeTitle(String title, String prefix) {
        Element block = document.createElement("fo:block");
        block.setAttribute("id", prefix+title);
        block.setAttribute("page-break-before", "always");
        block.setAttribute("line-height", "28pt");
        block.setAttribute("font-weight", "bold");
        block.setAttribute("font-size", "14pt");
        block.setAttribute("text-align", "left");
        block.setAttribute("space-before", "20pt");
        block.setTextContent(title);
        return block;
    }

    private Element makeDescription(String description) {
        Element block = document.createElement("fo:block");
        block.setAttribute("line-height", "12pt");
        block.setAttribute("font-size", "10pt");
        block.setAttribute("text-align", "left");
        block.setAttribute("space-before.optimum", "4pt");
        block.setAttribute("space-after.optimum", "10pt");
        block.setTextContent(description);
        return block;
    }

    private Element makeLink(String id, String prefix) {
        Text titleNode = document.createTextNode(id);
        Element link = document.createElement("fo:basic-link");
        if ((prefix+id).equals("")) {id="X";}
        link.setAttribute("internal-destination", prefix+id);
        link.appendChild(titleNode);
        return link;
    }

    private Element makeLinkBlock(String id) {
        Text titleNode = document.createTextNode(id);
        Element link = document.createElement("fo:basic-link");
        if ((id).equals("")) {id="Y";}
        link.setAttribute("internal-destination", id);
        link.appendChild(titleNode);

        Element block = document.createElement("fo:block");
        block.setAttribute("line-height", "12pt");
        block.setAttribute("font-size", "10pt");
        block.setAttribute("text-align", "left");
        block.setAttribute("space-before.optimum", "4pt");
        block.setAttribute("space-after.optimum", "10pt");
        block.appendChild(link);
        return block;
    }

    private Node openAttributesTable() {
        Element table = document.createElement("fo:table");
        table.setAttribute("border-collapse", "collapse");
        table.setAttribute("space-before", "10pt");
        table.setAttribute("space-after", "10pt");

        // Portrait: total width: 169
        // Landscape: total width: 256
        // Attribute | Default | Type | Req | RO | Properties

        // header:
        Element header = document.createElement("fo:table-header");
        Element row = document.createElement("fo:table-row");

        makeColumnHeaderCell("Attribute", table, row, 30);
        makeColumnHeaderCell("Type", table, row, 50);
        makeColumnHeaderCell("Default", table, row, 31);
        makeColumnHeaderCell("Req", table, row, 7);
        makeColumnHeaderCell("RO", table, row, 7);
        makeColumnHeaderCell("Description", table, row, 71);
        makeColumnHeaderCell("Properties", table, row, 60);
        return table;
    }

    private Node openActionTable() {
        Element table = document.createElement("fo:table");
        table.setAttribute("border-collapse", "collapse");
        table.setAttribute("space-before", "10pt");
        table.setAttribute("space-after", "10pt");

        // Portrait: total width: 169
        // Landscape: total width: 256
        // Action | Description | Parameters

        // header:
        Element row = document.createElement("fo:table-row");

        makeColumnHeaderCell("Parameter / Message", table, row, 40);
        makeColumnHeaderCell("Default Value", table, row, 30);
        makeColumnHeaderCell("Description", table, row, 100);
        makeColumnHeaderCell("Code List", table, row, 86);
        return table;
    }

    private Element makeColumnHeaderCell(String name, Node table, Node row, int width) {
        Element column = document.createElement("fo:table-column");
        column.setAttribute("column-width", width+"mm");
        table.appendChild(column);

        Element cell = document.createElement("fo:table-cell");
        cell.setAttribute("border-collapse", "collapse");
        Element rightHeaderBlock = document.createElement("fo:block");
        rightHeaderBlock.setAttribute("border", TABLE_BORDER);
        rightHeaderBlock.setAttribute("background-color", TABLE_HEADER_COLOR);
        rightHeaderBlock.setAttribute("text-align", "center");
        rightHeaderBlock.setAttribute("line-height", "28pt");
        rightHeaderBlock.setAttribute("font-size", "9pt");
        rightHeaderBlock.setTextContent(name);
        cell.appendChild(rightHeaderBlock);
        row.appendChild(cell);
        return cell;
    }

    private void makeActionRows(Action action, Element body) {
        Element hRow = document.createElement("fo:table-row");
        makeTableCell("Parameter / Message", hRow);
        makeTableCell("Default Value", hRow);
        makeTableCell("Description", hRow);
        makeTableCell("Code List", hRow);

        hRow.setAttribute("background-color",  TABLE_HEADER_COLOR);
        body.appendChild(hRow);
        // List<String> selectionList = new ArrayList<>();
        ModelElement element = null;
        if (action.getParameterList(element)!=null && action.getParameterList(element).size()>0) {
            for (Attribute<?> parameter : action.getParameterList(element)) {
                Element row = document.createElement("fo:table-row");
                makeTableCell(parameter.getLabel(), row);
                makeTableCell(parameter.getDefaultValue().isPresent() ? "" + parameter.getDefaultValue().get() : "", row);
                makeTableCell(parameter.getDescription().isPresent() ? "" + parameter.getDescription().get() : "", row);
                String codeList = "";
                // TODO: check attributetype
//                if (parameter.getCodeList().isEmpty()) {
//                    codeList = "";
//                } else {
//                    parameter.getCodeList().values().stream().collect(Collectors.joining(", ", "{", "}"));
//                }
                makeTableCell(codeList, row);
                body.appendChild(row);
            }
        }
    }

    private void makeRows(ModelElement element, Element body) {
        Element hRow = document.createElement("fo:table-row");
        makeTableCell("Attribute", hRow);
        makeTableCell("Type", hRow);
        makeTableCell("Default", hRow);
        makeTableCell("Req", hRow);
        makeTableCell("RO", hRow);
        makeTableCell("Description", hRow);
        makeTableCell("Properties", hRow);
        hRow.setAttribute("background-color",  TABLE_HEADER_COLOR);
        body.appendChild(hRow);

        // Attribute | Type | Default | Req | RO | Description | Properties
        Map<Attribute<?>, List<Check>> dependencies = ((ModelElement)element).getDependencies();
        Map<Attribute<?>, List<Check>> validatorMap = ((ModelElement)element).getValidatorMap();
        List<Attribute<?>> list = Arrays.asList(element.getAttributes());
        for (Attribute<?> attribute : list) {
            if (!attribute.isHidden()) {
                Element row = document.createElement("fo:table-row");
                String dependenciesAndValidators = getDepAndVal(attribute, validatorMap.get(attribute), dependencies.get(attribute));
                Set<String> valueSet;
                String value = "";
                switch (attribute.getInputType()) {
                    case "Text":
                        value = "";
                        break;
                    case "Textarea":
                        value = "";
                        break;
                    case "StaticMapping":
                        valueSet = ((StaticMapping<?>) attribute).getValueMap().keySet();
                        value = valueSet.stream().collect(Collectors.joining(", ", "One of {", "}"));
                        break;
                    case "DynamicMapping":
                        value = "";
                        break;
                    case "Chooser":
                        value = "chooser";
                        break;
                    case "Alternative":
                        value = "true or false";
                        break;
                    case "Display":
                        value = "message";
                        break;
                    case "DynamicMultiSelect":
                        break;
                    case "StaticMultiSelect":
                        valueSet = ((StaticMapping<?>) attribute).getValueMap().keySet();
                        value = valueSet.stream().collect(Collectors.joining(", ", "One or more of {", "}"));
                        break;
                    default:
                        value = "not defined!";
                }

                makeTableCell(attribute.getLabel(), row);
                makeTableCell(value, row);
                makeTableCell(""+(attribute.getDefaultValue().isPresent() ? attribute.getDefaultValue().get() : ""), row);
                makeTableCell(attribute.isRequired() ? "X" : "", row);
                makeTableCell(attribute.isReadonly() ? "X" : "", row);
                makeTableCell(attribute.getDescription().isPresent() ? attribute.getDescription().get() : "", row);
                makeTableCell(dependenciesAndValidators, row);
                body.appendChild(row);
            }
        }
    }

    private void makeTableCell(String text, Element row) {
        if (text==null) {
            text = "";
        }
        String[] split = text.split("; ");
        Element cell = document.createElement("fo:table-cell");
        cell.setAttribute("border", TABLE_BORDER);
        cell.setAttribute("padding", "3pt");
        cell.setAttribute("wrap-option", "wrap");
        cell.setAttribute("hyphenate", "true");
        for (String value : split) {
            Element block = document.createElement("fo:block");
            block.setAttribute("font-size", "8pt");
            block.setAttribute("wrap-option", "wrap");
            block.setAttribute("hyphenate", "true");
            block.appendChild(makeNode(value, "#000000"));
            cell.appendChild(block);
        }
        row.appendChild(cell);
    }

    static String getDepAndVal(Attribute<?> attribute, List<Check> validationList, List<Check> dependencyList) {
        String validation = validationList!=null && !validationList.isEmpty()
                ? validationList.stream().map(val -> val.getTrueIf()).collect(Collectors.joining(" and ", "Valid if ", ".")) : "";
        String dependency = dependencyList!=null && !dependencyList.isEmpty()
                ? dependencyList.stream().map(val -> val.getTrueIf()).collect(Collectors.joining(" and ", "Enabled if ", ".")) : "";
        if (StringUtils.countMatches(validation, '(')==1) {
            validation = validation.replaceAll("\\(", "").replaceAll("\\)", "");
        }
        if (StringUtils.countMatches(dependency, '(')==1) {
            dependency = dependency.replaceAll("\\(", "").replaceAll("\\)", "");
        }
        return (dependency + (validation.isEmpty() || dependency.isEmpty() ? "" : "; ")) + validation;
    }
    // <fo:inline font-weight="bold"><xsl:value-of select="." /></fo:inline>

    private Element makeNode(String text, String color) {
        Element prefix = document.createElement("fo:inline");
        prefix.setAttribute("color", color);
        prefix.setAttribute("wrap-option", "wrap");
        prefix.setAttribute("hyphenate", "true");
        prefix.setTextContent(text);
        return prefix;
    }

    private String adjustTextToCell(String text) {
        int LIMIT = 11;
        String value = "";
        String[] split = text.split(" ");
        for (String entry : split) {
            if (entry.length()>LIMIT) {
                value += entry.substring(0, LIMIT) + " ";
                value += entry.substring(LIMIT) + " ";
            } else {
                value += entry + " ";
            }
        }
        return value.trim();
    }

    private Node createLayout() {
        // <fo:layout-master-set>
        // <fo:simple-page-master master-name="simple"
        // page-height="29.7cm"
        // page-width="21cm"
        // margin-top="5mm"
        // margin-bottom="0cm"
        // margin-left="2cm"
        // margin-right="2cm">
        // <fo:region-body margin-top="1.5cm" margin-bottom="-2cm"/>
        // <fo:region-before extent="1.5cm"/>
        // <fo:region-after extent="1.5cm"/>
        // </fo:simple-page-master>
        // </fo:layout-master-set>
        Element layoutMasterSet = document
                .createElement("fo:layout-master-set");

        Element master = document.createElement("fo:simple-page-master");
        master.setAttribute("master-name", "simple");
        // master.setAttribute("type", "landscape");
//          master.setAttribute("page-height", "29.7cm");
//          master.setAttribute("page-width", "21cm");
        master.setAttribute("page-height", "21cm");
        master.setAttribute("page-width", "29.7cm");
        master.setAttribute("margin-top", "1cm");
        master.setAttribute("margin-bottom", "2cm");
        master.setAttribute("margin-left", "2cm");
        master.setAttribute("margin-right", "2cm");

        Element regionBody = document.createElement("fo:region-body");
        regionBody.setAttribute("margin-top", "1.2cm");
        regionBody.setAttribute("margin-bottom", "0cm");
        master.appendChild(regionBody);

        if (header) {
            Element regionBefore = document.createElement("fo:region-before");
            regionBefore.setAttribute("extent", "1.5cm");
            master.appendChild(regionBefore);
        }
        if (footer) {
            Element regionAfter = document.createElement("fo:region-after");
            regionAfter.setAttribute("extent", "-1.5cm");
            master.appendChild(regionAfter);
        }
        layoutMasterSet.appendChild(master);

        return layoutMasterSet;
    }

    private Node createHeader() {
        Element staticContent = document.createElement("fo:static-content");
        staticContent.setAttribute("flow-name", "xsl-region-before");

        // Element blockTopLeft = document.createElement("fo:block");
        // blockTopLeft.setAttribute("line-height", "16pt");
        // blockTopLeft.setAttribute("font-weight", "bold");
        // blockTopLeft.setAttribute("font-size", "12pt");
        // blockTopLeft.setAttribute("text-align", "left");
        // blockTopLeft.setTextContent("Traceability: " + projectName);
        // staticContent.appendChild(blockTopLeft);

        Element blockLeft = document.createElement("fo:block"); // Date
        blockLeft.setAttribute("line-height", "12pt");
        blockLeft.setAttribute("font-weight", "bold");
        blockLeft.setAttribute("font-size", "10pt");
        blockLeft.setAttribute("text-align", "left");
        // blockLeft.setTextContent(new SimpleDateFormat(DATE_FORMAT,
        // Locale.ENGLISH).format(new Date()));
        blockLeft.setAttribute("color", "#128474");
        // blockLeft.setTextContent(projectName);
        blockLeft.appendChild(makeLink(projectName, ""));
        staticContent.appendChild(blockLeft);

        // Element blockCenter = document.createElement("fo:block"); // Version
        // No.
        // blockCenter.setAttribute("line-height", "12pt");
        // blockCenter.setAttribute("font-weight", "bold");
        // blockCenter.setAttribute("font-size", "10pt");
        // blockCenter.setAttribute("text-align", "center");
        // blockCenter.setAttribute("space-before.optimum", "-12pt");
        // blockCenter.setTextContent("Version " + version);
        // staticContent.appendChild(blockCenter);

        Element blockRight = document.createElement("fo:block"); // page x of y
        blockRight.setAttribute("line-height", "12pt");
        blockRight.setAttribute("font-weight", "bold");
        blockRight.setAttribute("font-size", "10pt");
        blockRight.setAttribute("text-align", "end");
        blockRight.setAttribute("space-before.optimum", "-12pt");
        blockRight.setAttribute("color", "#128474");
        blockRight.setTextContent(projectVersion);
//          blockRight.appendChild(document.createTextNode("Page "));
//          blockRight.appendChild(document.createElement("fo:page-number"));
//          blockRight.appendChild(document.createTextNode(" of "));
//          Element endReference = document
//                      .createElement("fo:page-number-citation");
//          endReference.setAttribute("ref-id", "theEnd");
//          blockRight.appendChild(endReference);
        staticContent.appendChild(blockRight);

        Element blockHR = document.createElement("fo:block");
        Element leader = document.createElement("fo:leader");
        leader.setAttribute("leader-pattern", "rule");
        leader.setAttribute("rule-thickness", "0.5pt");
        leader.setAttribute("leader-length", "25.7cm"); // for portrait: "17cm");
        // leader.setAttribute("space-after.optimum", "20pt");
        leader.setAttribute("color", "#128474");
        blockHR.appendChild(leader);
        staticContent.appendChild(blockHR);

        return staticContent;
    }

    private Node createFooter() {
        // <fo:static-content flow-name="xsl-region-after">
        // <fo:block>
        // <fo:leader leader-pattern="rule" rule-thickness="0.5pt"
        // leader-length="17cm" space-after.optimum="2pt" color="#000050" />
        // </fo:block>
        // <fo:block line-height="14pt" font-weight="bold" font-size="10pt"
        // text-align="begin" space-after.optimum="-14pt">Protocol</fo:block>
        // <fo:block line-height="14pt" font-weight="bold" font-size="10pt"
        // text-align="end">Page <fo:page-number/> of <fo:page-number-citation
        // ref-id="theEnd" /></fo:block>
        // </fo:static-content>
        Element staticContent = document.createElement("fo:static-content");
        staticContent.setAttribute("flow-name", "xsl-region-after");

        Element block1 = document.createElement("fo:block");
        Element leader = document.createElement("fo:leader");
        leader.setAttribute("leader-pattern", "rule");
        leader.setAttribute("rule-thickness", "0.5pt");
        leader.setAttribute("leader-length", "25.7cm"); // for portrait: "17cm");
        leader.setAttribute("space-after.optimum", "2pt");
        leader.setAttribute("color", "#128474");
        block1.appendChild(leader);
        staticContent.appendChild(block1);

        Element block2 = document.createElement("fo:block");
        block2.setAttribute("line-height", "14pt");
        block2.setAttribute("font-weight", "bold");
        block2.setAttribute("font-size", "10pt");
        block2.setAttribute("text-align", "left");
        block2.setAttribute("space-after.optimum", "-14pt");
        block2.setTextContent("Date: "
                + new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
                .format(new Date()));
        staticContent.appendChild(block2);

        Element block3 = document.createElement("fo:block");
        block3.setAttribute("line-height", "14pt");
        block3.setAttribute("font-weight", "bold");
        block3.setAttribute("font-size", "10pt");
        block3.setAttribute("text-align", "end");
        block3.appendChild(document.createTextNode("Page "));
        block3.appendChild(document.createElement("fo:page-number"));
        block3.appendChild(document.createTextNode(" of "));
        Element endReference = document
                .createElement("fo:page-number-citation");
        endReference.setAttribute("ref-id", "theEnd");
        block3.appendChild(endReference);

        staticContent.appendChild(block3);

        return staticContent;
    }

    private Node createTableOfContents(List<ModelElement> list) {
        // <fo:block id="table-of-contents">Table of Contents</fo:block>
        // @formatter:off
        // <fo:block space-before="1em" border-style="inset" border-width="thin"
        // border-before-width.conditionality="retain"
        // border-after-width.conditionality="retain"
        // border-color="black" background-color="#EEEEEE" padding="1em"
        // start-indent="1em" end-indent="1em">
        // <fo:block font-size="1.3em" font-weight="bold" space-before="0.5em"
        // space-after="0.5em" keep-with-next.within-page="always">Table Of
        // Contets</fo:block>
        // <fo:block text-align-last="justify" margin-left="0em"
        // space-before="0.5em" font-size="1em" font-weight="bold">Chapter
        // 1<fo:leader leader-pattern="dots" />
        // <fo:page-number-citation ref-id="ID-CH-1" />
        // </fo:block>
        // <fo:block text-align-last="justify" margin-left="1em"
        // space-before="0.3em" font-size="0.9em">Contets 1-1
        // sample-01<fo:leader leader-pattern="dots" />
        // <fo:page-number-citation ref-id="ID-CH-11" />
        // </fo:block>
        // <fo:block text-align-last="justify" margin-left="2em"
        // space-before="0.1em" font-size="0.9em">Contets 1-1-1<fo:leader
        // leader-pattern="dots" />
        // <fo:page-number-citation ref-id="ID-CH-111" />
        // </fo:block>
        // <fo:block text-align-last="justify" margin-left="0em"
        // space-before="0.5em" font-size="1em" font-weight="bold">Chapter
        // 2<fo:leader leader-pattern="dots" />
        // <fo:page-number-citation ref-id="ID-CH-2" />
        // </fo:block>
        // </fo:block>
        // @formatter:on

        Element blockTOC = document.createElement("fo:block");
        blockTOC.setAttribute("space-before", "0pt");
        blockTOC.setAttribute("space-after", "5pt");
        // blockTOC.setAttribute("border-style", "inset");
        // blockTOC.setAttribute("border-width", "thin");
        // blockTOC.setAttribute("border-before-width.conditionality",
        // "retain");
        // blockTOC.setAttribute("border-after-width.conditionality", "retain");
        // blockTOC.setAttribute("border-color", "black");
        // blockTOC.setAttribute("background-color", "#EEEEEE");
        blockTOC.setAttribute("padding", "1em");
        blockTOC.setAttribute("start-indent", "0.5em");
        blockTOC.setAttribute("end-indent", "0.5em");
        blockTOC.appendChild(createTOCHeader());

//          for (Chapter chapter : Specification.requirements.keySet()) {
//                String chapterTitle = chapter.getTitle();
//                blockTOC.appendChild(createTOCChapterEntry(chapterTitle));
//          }

        // List<ModelElement> fieldList = list.stream().filter(el -> el.getCategory().equals(Field.CATEGORY)).collect(Collectors.toList());

        // Model elements except fields
        Element elementHeaderNode = createTOCHeader();
        elementHeaderNode.setTextContent("Model Elements");
        elementHeaderNode.setAttribute("space-before", "1pt");
        blockTOC.appendChild(elementHeaderNode);
        for (ModelElement element : list) {
           // if (!fieldList.contains(element)) {
                String chapterTitle = element.getType();
                Node tocEntry = createTOCChapterEntry(chapterTitle, "");
                blockTOC.appendChild(tocEntry);
           // }
        }

        /* Field elements
        Element fieldHeaderNode = createTOCHeader();
        fieldHeaderNode.setTextContent("Field Model Elements");
        blockTOC.appendChild(fieldHeaderNode);
        for (ModelElement element : fieldList) {
            String chapterTitle = element.getType();
            Node tocEntry = createTOCChapterEntry(chapterTitle, "");
            blockTOC.appendChild(tocEntry);
        }*/

        // Actions
        Element actionHeaderNode = createTOCHeader();
        actionHeaderNode.setTextContent("Actions and Parameters");
        blockTOC.appendChild(actionHeaderNode);
        for (Action action: actionList) {
            String chapterTitle = action.getToolTip();
            Node tocEntry = createTOCChapterEntry(chapterTitle, "A_");
            blockTOC.appendChild(tocEntry);
        }

        // Study Model Changes
        Element modelChangeHeaderNode = createTOCHeader();
        modelChangeHeaderNode.setTextContent("Study Model Changes");
        blockTOC.appendChild(modelChangeHeaderNode);

        Node tocEntry = createTOCChapterEntry("Model Update", "MU_");
        blockTOC.appendChild(tocEntry);

        return blockTOC;
    }

    private Element createTOCHeader() {
        // <fo:block font-size="1.3em" font-weight="bold" space-before="0.5em"
        // space-after="0.5em" keep-with-next.within-page="always">Table Of
        // Contets</fo:block>
        Element block = document.createElement("fo:block");
        block.setAttribute("font-size", "1.3em");
        block.setAttribute("font-weight", "bold");
        block.setAttribute("space-before", "0.5em");
        block.setAttribute("space-after", "0.5em");
        block.setAttribute("keep-with-next.within-page", "always");
        block.setTextContent(TABLE_OF_CONTENTS);
        return block;
    }

    private Node createTOCChapterEntry(String chapterTitle, String prefix) {
        // <fo:block text-align-last="justify" margin-left="0em"
        // space-before="0.5em" font-size="1em" font-weight="bold">Chapter
        // 1<fo:leader leader-pattern="dots" />
        // <fo:page-number-citation ref-id="ID-CH-1" />
        // </fo:block>
        Element block = document.createElement("fo:block");
        block.setAttribute("text-align-last", "justify");
        block.setAttribute("margin-left", "0em");
        block.setAttribute("space-before", "0.5em");
        block.setAttribute("font-size", "1em");
        block.setAttribute("font-weight", "bold");

        Element link = makeLink(chapterTitle, prefix);

        block.appendChild(link);
        Element leader = document.createElement("fo:leader");
        leader.setAttribute("leader-pattern", "dots");
        block.appendChild(leader);
        Element pageNumber = document.createElement("fo:page-number-citation");
        pageNumber.setAttribute("ref-id", prefix+chapterTitle);
        block.appendChild(pageNumber);
        return block;
    }

    static List<List<String>> getUpstreamTypeList(ModelElement element, List<ModelElement> list, String downstream) {
        List<List<String>> parentTypeList = new ArrayList<>();

        String type = element.getType();

        for (ModelElement parentElement : list) {

            boolean accepted = ((ModelElement)parentElement).getChildTypes().contains(type); // acceptsAsChildType(type);

            if (accepted) {
/*
                if (parentElement.getType().equals(Study.TYPE)) {

                    List<String> pair = new ArrayList<>();

                    pair.add(parentElement.getType());

                    pair.add(element.getType());

                    parentTypeList.add(pair);

                } else*/
                if (parentElement.getType().equals(type)) {

                } else if ((":"+downstream+":").contains(":"+type+":")) {

                } else {

                    List<List<String>> grandParentList = getUpstreamTypeList((ModelElement)parentElement, list, type+":"+downstream);

                    for (List<String> gp: grandParentList) {

                        List<String> newList = new ArrayList<>();

                        newList.addAll(gp);

                        newList.add(element.getType());

                        parentTypeList.add(newList);

                    }

                }

            }

        }

        return parentTypeList;

    }

}
