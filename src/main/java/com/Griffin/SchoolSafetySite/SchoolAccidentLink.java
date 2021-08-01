package com.Griffin.SchoolSafetySite;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
public class SchoolAccidentLink {
    @Id
    @GeneratedValue
    Long Id;
    String accidentIndex;
    String schoolURN;
    Double distance;
    String lat;
    String lon;
    int accidentSeverity;
    int numberOfVehicles;
    int numberOfCasualities;
    int dayOfWeekNumber;
    String dayOfWeekString;
    String time;

}
