package com.learnbatch.segmentcodebatch;

import com.learnbatch.segmentcodebatch.model.SegmentCode;
import com.learnbatch.segmentcodebatch.model.UpdatedBy;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.springframework.stereotype.Component;

@Component
public class DataConverter implements Converter {

  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {}

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    SegmentCode segmentCode = new SegmentCode();

    segmentCode.setSk(reader.getAttribute("SK"));

    reader.moveDown();

    String code = reader.getValue();
    segmentCode.setCode(code);

    reader.moveUp();
    reader.moveDown();

    String description = reader.getValue();

    segmentCode.setDescription(description);

    reader.moveUp();
    reader.moveDown();
    segmentCode.setRank(Integer.parseInt(reader.getValue()));

    reader.moveUp();
    reader.moveDown();
    segmentCode.setRequiresMemo(Boolean.parseBoolean(reader.getValue()));

    reader.moveUp();
    reader.moveDown();
    segmentCode.setColor(Integer.parseInt(reader.getValue()));

    reader.moveUp();
    reader.moveDown();
    segmentCode.setFontColor(Integer.parseInt(reader.getValue()));

    reader.moveUp();
    reader.moveDown();

    segmentCode.setDefaultDuration(Integer.parseInt(reader.getValue()));

    reader.moveUp();
    reader.moveDown();
    UpdatedBy updatedBy = new UpdatedBy();
    segmentCode.setUpdatedBy(updatedBy);
    segmentCode.getUpdatedBy().setSk(reader.getAttribute("SK"));

    reader.moveUp();
    reader.moveDown();

    segmentCode.setUpdatedOn(reader.getValue());
    return segmentCode;
  }

  @Override
  public boolean canConvert(Class type) {
    return type.equals(SegmentCode.class);
  }
}
