package pl.ttpsc.smartparking.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "plate")
public class PlateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String plateStr;

    @OneToMany(mappedBy = "plate")
    @JsonIgnore
    private Set<AccessEntity> access;

    public PlateEntity() {
    }

    public PlateEntity(Long id, String name, String lastName, String plateStr, Set<AccessEntity> access) {
        this.id = id;
        this.firstName = name;
        this.lastName = lastName;
        this.plateStr = plateStr;
        this.access = access;
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

    public Set<AccessEntity> getAccess() {
        return access;
    }

    public void setAccess(Set<AccessEntity> access) {
        this.access = access;
    }
}
