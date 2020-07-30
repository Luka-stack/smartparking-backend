package pl.ttpsc.smartparking.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "plates")
public class PlateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String plateNum;

    @OneToMany(mappedBy = "plate", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({ "plate" })
    private Set<AccessEntity> accesses;

    public PlateEntity() {
    }

    public PlateEntity(Long id, String name, String lastName, String plateNum, Set<AccessEntity> accesses) {

        this.id = id;
        this.firstName = name;
        this.lastName = lastName;
        this.plateNum = plateNum;
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

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public Set<AccessEntity> getAccesses() {
        return accesses;
    }

    public void setAccesses(Set<AccessEntity> accesses) {
        this.accesses = accesses;
    }
}
