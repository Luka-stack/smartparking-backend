package pl.ttpsc.smartparking.persistence.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.ttpsc.smartparking.persistence.mapper.PlateEntitySerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "plates")
@JsonSerialize(using = PlateEntitySerializer.class)
public class PlateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String plateStr;

    @OneToMany(mappedBy = "plate")
    private Set<AccessEntity> accesses;

    public PlateEntity() {
    }

    public PlateEntity(Long id, String name, String lastName, String plateStr, Set<AccessEntity> accesses) {
        this.id = id;
        this.firstName = name;
        this.lastName = lastName;
        this.plateStr = plateStr;
        this.accesses = accesses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlateStr() {
        return plateStr;
    }

    public void setPlateStr(String plateStr) {
        this.plateStr = plateStr;
    }

    public Set<AccessEntity> getAccesses() {
        return accesses;
    }

    public void setAccesses(Set<AccessEntity> accesses) {
        this.accesses = accesses;
    }
}
