package tn.esprit.spring.services;

import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Weather;

import java.util.List;

public interface IPisteServices {

    List<Piste> retrieveAllPistes();

    Piste  addPiste(Piste  piste);

    void removePiste (Long numPiste);

    Piste retrievePiste (Long numPiste);
    String recommendBestPisteForSkier(Skier skier);
    Skier findSkierById(Long skierId);
    Weather getCurrentWeather();  // Déclaration dans l'interface

}
