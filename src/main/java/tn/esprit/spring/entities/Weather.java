package tn.esprit.spring.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
public class Weather implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long numWeather;
    private boolean snowy;
    private boolean windy;
    public Weather(boolean snowy, boolean windy) {
        this.snowy = snowy;
        this.windy = windy;
    }
}
