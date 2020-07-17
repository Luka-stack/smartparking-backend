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

    private String plate;

    @OneToMany(mappedBy = "plates")
    @JsonIgnore
    private Set<AccessEntity> access;

    public PlateEntity() {
    }

    public PlateEntity(Long id, String name, String lastName, String plate, Set<AccessEntity> access) {
        this.id = id;
        this.firstName = name;
        this.lastName = lastName;
        this.plate = plate;
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

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Set<AccessEntity> getAccess() {
        return access;
    }

    public void setAccess(Set<AccessEntity> access) {
        this.access = access;
    }
}
