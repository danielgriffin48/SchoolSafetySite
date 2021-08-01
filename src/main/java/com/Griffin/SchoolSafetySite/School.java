package com.Griffin.SchoolSafetySite;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
public class School {
    @Id
    @GeneratedValue
    private Long id;
    private String URN;
    private String LACode;
    private String LAName;
    private String establishmentName;
    private String establishmentTypeGroup;
    private String phaseOfEducation;
    private String address; //combined from street, locality, address, town, postcode
    private String districtAdministrative;
    private String administrativeWard;
    private String parliamentaryConstituency;
    private String urbanRural;
    private String easting;
    private String northing;
    private String lat = null;
    private String lon = null;
    private String geohash = null;
}
