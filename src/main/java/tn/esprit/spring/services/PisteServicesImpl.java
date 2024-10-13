package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.SkierLevel;
import tn.esprit.spring.entities.Weather;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.IWeatherRepository;

import java.util.List;
@AllArgsConstructor
@Service
public class PisteServicesImpl implements  IPisteServices{
private IWeatherRepository weatherRepository;
    private IPisteRepository pisteRepository;
private ISkierRepository skierRepository;
    @Override
    public List<Piste> retrieveAllPistes() {
        return pisteRepository.findAll();
    }

    @Override
    public Piste addPiste(Piste piste) {
        return pisteRepository.save(piste);
    }

    @Override
    public void removePiste(Long numPiste) {
        pisteRepository.deleteById(numPiste);
    }

    @Override
    public Piste retrievePiste(Long numPiste) {
        return pisteRepository.findById(numPiste).orElse(null);
    }

    @Override
    public String recommendBestPisteForSkier(Skier skier) {
        // Logique avancée de recommandation de piste
        if (skier.getLevel() == SkierLevel.BEGINNER) {
            return "Piste verte recommandée pour les débutants";
        } else if (skier.getLevel() == SkierLevel.EXPERT) {
            return "Piste noire recommandée pour les experts";
        }

        // Récupération des conditions météorologiques depuis la base de données
        Weather currentWeather = getCurrentWeather();
        if (currentWeather.isSnowy()) {
            return "Piste recommandée : conditions idéales pour skier sur neige.";
        } else if (currentWeather.isWindy()) {
            return "Piste recommandée : éviter les pistes exposées au vent fort.";
        }

        return "Aucune recommandation pour le moment.";
    }

    @Override
    public Skier findSkierById(Long skierId) {
        return skierRepository.findById(skierId).orElse(null);

    }

    @Override
    public Weather getCurrentWeather() {
        // Récupérer les dernières conditions météo de la base de données (ou simuler des valeurs)
        return weatherRepository.findLatestWeather()
                .orElse(new Weather(true, false)); // Simuler : neigeux, non venteux
    }

    // Méthode simulant l'obtention des conditions météo actuelles depuis la base de données

}
