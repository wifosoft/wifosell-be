package com.wifosell.lazada.modules.image.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "Request")
public class LazadaMigrateImagesRequest {
    @JacksonXmlElementWrapper(localName = "Images")
    @JacksonXmlProperty(localName = "Url")
    List<String> images;

    public LazadaMigrateImagesRequest(List<String> images) {
        this.images = images;
    }

    public String toXml() throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> " + xmlMapper.writeValueAsString(this);
    }
}
