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

        if (accessEntity.getDateFrom() != null) {
            jsonGenerator.writeStringField("dateFrom", accessEntity.getDateFrom().toString());
        }

        if (accessEntity.getDateTo() != null) {
            jsonGenerator.writeStringField("dateTo", accessEntity.getDateTo().toString());
        }

        jsonGenerator.writeObjectFieldStart("plate");
        jsonGenerator.writeNumberField("id", accessEntity.getPlate().getId());
        jsonGenerator.writeStringField("firstName", accessEntity.getPlate().getFirstName());
        jsonGenerator.writeStringField("lastName", accessEntity.getPlate().getLastName());
        jsonGenerator.writeStringField("plateStr", accessEntity.getPlate().getPlateStr());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }
}
