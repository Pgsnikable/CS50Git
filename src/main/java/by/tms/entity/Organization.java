package by.tms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = "employees")
@Entity
@Table(name = "organization", schema = "employee_storage")
public class Organization extends BaseEntity<Long> {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "organization")
    private Set<Employee> employees = new HashSet<>();

    public Organization(String name) {
        this.name = name;
    }
}
