package pl.ttpsc.smartparking.persistence.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import pl.ttpsc.smartparking.persistence.entity.AccessEntity;
import pl.ttpsc.smartparking.persistence.entity.PlateEntity;

import java.io.IOException;

@JsonComponent
public class PlateEntitySerializer extends JsonSerializer<PlateEntity> {

    @Override
    public void serialize(PlateEntity plateEntity, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        if (plateEntity.getId() != null) {
            jsonGenerator.writeNumberField("id", plateEntity.getId());
        }
        jsonGenerator.writeStringField("firstName", plateEntity.getFirstName());
        jsonGenerator.writeStringField("lastName", plateEntity.getLastName());
        jsonGenerator.writeStringField("plateStr", plateEntity.getPlateStr());

        jsonGenerator.writeArrayFieldStart("accesses");
        if (plateEntity.getAccesses() != null) {
            for (AccessEntity access : plateEntity.getAccesses()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("id", access.getId());
                jsonGenerator.writeStringField("dateFrom", access.getDateFrom().toString());
                jsonGenerator.writeStringField("dateTo", access.getDateTo().toString());
                jsonGenerator.writeEndObject();
            }
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
