package pl.ttpsc.smartparking.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import pl.ttpsc.smartparking.persistence.mapper.LocalDateDeserializer;
import pl.ttpsc.smartparking.persistence.mapper.LocalDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "access")
public class AccessEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_from")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateFrom;

    @Column(name = "date_to")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateTo;

    @ManyToOne
    @JsonIgnore
    private PlateEntity plates;

    public AccessEntity() {
    }

    public AccessEntity(Long id, LocalDate dateFrom, LocalDate dateTo, PlateEntity plates) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.plates = plates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public PlateEntity getPlates() {
        return plates;
    }

    public void setPlates(PlateEntity plates) {
        this.plates = plates;
    }
}
