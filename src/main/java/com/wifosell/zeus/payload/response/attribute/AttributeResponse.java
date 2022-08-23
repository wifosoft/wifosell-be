package com.wifosell.zeus.payload.response.attribute;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

@Getter
public class AttributeResponse extends BasicEntityResponse {
    private final String name;
    private final String value;

    public AttributeResponse(Attribute attribute) {
        super(attribute);
        this.name = attribute.getName();
        this.value = attribute.getValue();
    }
}
