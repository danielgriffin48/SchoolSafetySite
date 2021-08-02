package com.Griffin.SchoolSafetySite;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@Data
public class SchoolAccidentLink {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    Long Id;

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
    String accidentIndex;
    String date;

    @ElementCollection(targetClass=Casualty.class)
    private List<Casualty> casualtyList = new ArrayList<>();

    public void setCasualtyList(List<Casualty> casualtyList) {
        this.casualtyList = casualtyList;
    }
}
