package com.kderyabin.storage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.util.Objects;

@ToString
@Entity
@Table(name = "settings")
public class SettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter @Setter
    private Long id;


    @Getter @Setter
    @Column(length = 30, nullable = false, unique = true)
    private String name;


    @Column @Getter @Setter
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettingEntity)) return false;
        SettingEntity that = (SettingEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value);
    }
}
