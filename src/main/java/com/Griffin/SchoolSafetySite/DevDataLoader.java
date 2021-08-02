package com.Griffin.SchoolSafetySite;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

//@Component
public class DevDataLoader { //implements ApplicationRunner {
    private SchoolRepository schoolRepository;
    private SchoolAccidentLinkRepository schoolAccidentLinkRepository;
    private CasaultyRepository casaultyRepository;
    @Autowired
    public DevDataLoader(SchoolRepository schoolRepository,
                         SchoolAccidentLinkRepository schoolAccidentLinkRepository,
                         CasaultyRepository casaultyRepository)
    {
        this.schoolRepository = schoolRepository;
        this.schoolAccidentLinkRepository = schoolAccidentLinkRepository;
        this.casaultyRepository = casaultyRepository;
    }


    //@Override
    public void run(ApplicationArguments args) throws Exception {

        //String schoolCasualtyFileLocation = "/home/griffin/SchoolCrashData-2.0/TestData/CasTest.csv";
        String casualtyFolder = "/home/griffin/SchoolCrashData-2.0/AccidentCasualtyData/Casualties";
        File accFolder = new File(casualtyFolder);
        String[] casaultyFileLocations = accFolder.list();
        for (String casualtyFileLocation : casaultyFileLocations) {
            this.addCasualtiesFromFile(casualtyFolder + "/" + casualtyFileLocation);
        }

        //String schoolFileLocation = "/home/griffin/SchoolCrashData-2.0/TestData/SchoolTestData.csv";
        String schoolFileLocation = "/home/griffin/SchoolCrashData-2.0/SchoolFile-2.csv";
        Iterable<CSVRecord> fileParser = this.openNewParser(schoolFileLocation);
        List<School> schoolList = new ArrayList<>();
        // for record
        for(CSVRecord record : fileParser)
        {
            School n = new School();
                n.setURN(record.get(0));
                n.setLACode(record.get(1));
                n.setLAName(record.get(2));
                n.setLat(record.get(110));
                n.setLon(record.get(111));
            n.setEstablishmentName(record.get(4));
            n.setEstablishmentTypeGroup(record.get(6));
            n.setPhaseOfEducation(record.get(12));
            n.setDistrictAdministrative(record.get(85));

            schoolList.add(n);
        }
        this.schoolRepository.saveAll(schoolList);

        //String schoolAccidentLinkFileLocation = "/home/griffin/SchoolCrashData-2.0/TestData/SchoolAccLinkTest.csv";
        String schoolAccidentLinkFileLocation = "/home/griffin/SchoolCrashData-2.0/SchoolAccLinks.csv";
        Iterable<CSVRecord> fileParser2 = this.openNewParser(schoolAccidentLinkFileLocation);
        List<SchoolAccidentLink> salList = new ArrayList<>();
        // for record
        for(CSVRecord record : fileParser2)
        {
            SchoolAccidentLink n = new SchoolAccidentLink();
            n.setAccidentIndex(record.get(0));
            n.setSchoolURN(record.get(1));
            n.setDistance(Double.parseDouble(record.get(2)));
            n.setLat(record.get(3));
            n.setLon(record.get(4));
            n.setAccidentSeverity(Integer.parseInt(record.get(5)));
            n.setNumberOfVehicles(Integer.parseInt(record.get(6)));
            n.setNumberOfCasualities(Integer.parseInt(record.get(7)));
            n.setDayOfWeekNumber(Integer.parseInt(record.get(8)));
            n.setTime(record.get(9));
            n.setDayOfWeekString(record.get(10));
            n.setDate(record.get(11));

            salList.add(n);
        }

        schoolAccidentLinkRepository.saveAll(salList);

    }
    private void addCasualtiesFromFile(String schoolCasualtyFileLocation) throws IOException {
        Iterable<CSVRecord> fileParser3 = this.openNewParser(schoolCasualtyFileLocation);
        List<Casualty> casList = new ArrayList<>();
        // for record
        for(CSVRecord record : fileParser3)
        {
            Casualty cas = new Casualty();
            cas.setAccidentIndex(record.get(0));
            cas.setSexOfCasualty(Integer.parseInt(record.get(4)));
            cas.setAgeOfCasualty(Integer.parseInt(record.get(5)));
            cas.setCasualtySeverity(Integer.parseInt(record.get(7)));
            cas.setPedestrianLocation(record.get(8));
            cas.setCasualtyType(record.get(13));
            if (cas.isIncludeInDatabase())
            {
                casList.add(cas);
            }
        }
        casaultyRepository.saveAll(casList);
    }
    private Iterable<CSVRecord> openNewParser(String CSVFileLocation) throws IOException {
        Reader CSVFileReader;

        try {
            CSVFileReader = new FileReader(CSVFileLocation);
            return CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(CSVFileReader);

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
