package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

public class True extends AbstractCheck {
    public True() {
        super("True");
        message("Is 'true'");
    }

    @Override
    public boolean evaluate(ModelElement element, Attribute<?> attribute) {
        return true;
    }

    public String getScript() {
        return "function getTrue(check, element, key) {" +
                "	return true;" +
                "}";
    }
}
