package tn.esprit.spring.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
public class Skier implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numSkier;
	String firstName;
	String lastName;
	LocalDate dateOfBirth;
	String city;
	// Nouveaux attributs pour le suivi de performance
	private double totalDistance; // en kilomètres
	private double totalTime;     // en heures
	private int level;            // Niveau du skieur

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	Subscription subscription;

	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "excursion",
			joinColumns = @JoinColumn(name = "numSkier"),
			inverseJoinColumns = @JoinColumn(name = "numPiste"))
	private Set<Piste> pistes;


	@OneToMany(mappedBy = "skier")
	Set<Registration> registrations;


	// Getters et Setters pour les nouveaux attributs
	public double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}





}
