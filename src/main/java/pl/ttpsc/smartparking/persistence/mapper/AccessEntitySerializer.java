package pl.ttpsc.smartparking.persistence.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;

import java.io.IOException;

@JsonComponent
public class AccessEntitySerializer extends JsonSerializer<AccessEntity> {

    @Override
    public void serialize(AccessEntity accessEntity, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        if (accessEntity.getId() != null) {
            jsonGenerator.writeNumberField("id", accessEntity.getId());
        }

        jsonGenerator.writeObjectField("dateFrom", accessEntity.getDateFrom());
        jsonGenerator.writeObjectField("dateTo", accessEntity.getDateTo());

        jsonGenerator.writeObjectFieldStart("Access");
        if (accessEntity.getPlate() != null) {
            jsonGenerator.writeNumberField("id", accessEntity.getPlate().getId());
            jsonGenerator.writeStringField("firstName", accessEntity.getPlate().getFirstName());
            jsonGenerator.writeStringField("lastName", accessEntity.getPlate().getLastName());
            jsonGenerator.writeStringField("plateStr", accessEntity.getPlate().getPlateStr());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndObject();
    }
}
