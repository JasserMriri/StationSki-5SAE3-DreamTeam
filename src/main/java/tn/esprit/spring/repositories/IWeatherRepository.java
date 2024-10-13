package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Weather;

import java.util.Optional;

@Repository
public interface IWeatherRepository extends CrudRepository<Weather,Long> {
    // Exemple de méthode pour récupérer la dernière météo enregistrée
    @Query("SELECT w FROM Weather w ORDER BY w.numWeather DESC")
    Optional<Weather> findLatestWeather();
}
