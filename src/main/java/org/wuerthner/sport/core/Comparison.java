package org.wuerthner.sport.core;

import org.wuerthner.sport.api.ModelElement;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Comparison {
    private final ModelElement tree1;
    private final ModelElement tree2;

    public Comparison(ModelElement tree1, ModelElement tree2) {
        this.tree1 = tree1;
        this.tree2 = tree2;
    }

    public boolean compare(ModelElement tree1, ModelElement tree2) {
        boolean result = true;
        result = compareAttributes(tree1, tree2, Optional.empty());
        result = result && compareChildren(tree1, tree2, Optional.empty());
        return result;
    }

    public Delta diff() {
        Delta delta = new Delta();
        diff(tree1, tree2, Optional.of(delta));
        return delta;
    }


    private boolean diff(ModelElement tree1, ModelElement tree2, Optional<Delta> optionalDelta) {
        boolean result = true;
        result = compareAttributes(tree1, tree2, optionalDelta);
        boolean ok = compareChildren(tree1, tree2, optionalDelta);
        result = result && ok;
        return result;
    }

    private boolean compareAttributes(ModelElement tree1, ModelElement tree2, Optional<Delta> optionalDelta) {
        boolean result = false;
        if(tree1 == null && tree2 == null){
            return true;
        }

        try{
            result = tree1.getAttributeMap().size() == tree2.getAttributeMap().size();
            //		if (!result) {
            //			optionalDelta.ifPresent(delta -> {
            //				delta.addAttributeDifference(tree1, tree2);
            //			});
            //		}
            if (result || optionalDelta.isPresent()) {
                for (Map.Entry<String,String> entry : tree1.getAttributeMap().entrySet()) {
                    String attributeId = entry.getKey();
                    String value1 = entry.getValue();
                    String value2 = tree2.getAttributeMap().get(attributeId);
                    boolean eq = Objects.equals(value1, value2);
                    if (!eq) {
                        optionalDelta.ifPresent(delta -> {
                            delta.addValueDifference(tree1, tree2, attributeId, value1, value2);
                        });
                    }
                    result = result && eq;
                    if (!result && !optionalDelta.isPresent()) {
                        break;
                    }
                }
            }
            if (result || optionalDelta.isPresent()) {
                for (Map.Entry<String,String> entry : tree2.getAttributeMap().entrySet()) {
                    String attributeId = entry.getKey();
                    String value1 = entry.getValue();
                    String value2 = tree1.getAttributeMap().get(attributeId);
                    boolean eq = Objects.equals(value1, value2);
                    if (!eq) {
                        optionalDelta.ifPresent(delta -> {
                            delta.addValueDifference(tree1, tree2, attributeId, value2, value1);
                        });
                    }
                    result = result && eq;
                    if (!result && !optionalDelta.isPresent()) {
                        break;
                    }
                }
            }
        }catch(NullPointerException n){}

        return result;
    }

    private boolean compareChildren(ModelElement tree1, ModelElement tree2, Optional<Delta> optionalDelta) {
        List<? extends ModelElement> children1 = tree1.getChildren();
        List<? extends ModelElement> children2 = tree2.getChildren();
        boolean result = children1.size()==children2.size();
//		if (!result) {
//			optionalDelta.ifPresent(delta -> {
//				delta.addSizeDifference(tree1, tree2);
//			});
//		}
        for (ModelElement child1 : children1) {
            Optional<ModelElement> child2 = tree2.lookupByTypeAndId(child1.getType(), child1.getId(), 1);
            if (result || optionalDelta.isPresent()) {
                boolean ok = child2.isPresent();
                if (!ok) {
                    optionalDelta.ifPresent(delta -> {
                        delta.addOnlyInFirstDifference(child1);
                    });
                }
                result = result && ok;
            }
            if (optionalDelta.isPresent()) {
                boolean ok = child2.isPresent() ? diff(child1, child2.get(), optionalDelta) : false;
                result = result && ok;
            } else {
                result = result && compare(child1, child2.get());
            }
        }
        for (ModelElement child2 : children2) {
            if (child2.getId()==null) {
                throw new RuntimeException("ID of element type '" + child2.getType() +"' in parent '"+child2.getParent().getId()+"' is null!");
            }
            Optional<ModelElement> child1 = tree1.lookupByTypeAndId(child2.getType(), child2.getId(), 1);
            if (result || optionalDelta.isPresent()) {
                boolean ok = child1.isPresent();
                if (!ok) {
                    optionalDelta.ifPresent(delta -> {
                        // delta.add("Element '" + child2.getId()+"':"+child2.getType() + " is present in tree2, but missing in tree1");
                        delta.addOnlyInSecondDifference(child2);
                    });
                }
                result = result && ok;
            }
            if (optionalDelta.isPresent()) {
                boolean ok = child1.isPresent() ? diff(child1.get(), child2, optionalDelta) : false;
                result = result && ok;
            } else {
                result = result && compare(child1.get(), child2);
            }
        }
        return result;
    }
}
