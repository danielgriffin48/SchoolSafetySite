package com.Griffin.SchoolSafetySite;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Component
public class DevDataLoader implements ApplicationRunner {
    private SchoolRepository schoolRepository;
    private SchoolAccidentLinkRepository schoolAccidentLinkRepository;
    @Autowired
    public DevDataLoader(SchoolRepository schoolRepository)
    {
        this.schoolRepository = schoolRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        String schoolFileLocation = "/home/griffin/SchoolCrashData-2.0/SchoolFile.csv";
        Iterable<CSVRecord> fileParser = this.openNewParser(schoolFileLocation);
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
            this.schoolRepository.save(n);
        }

        String schoolAccidentLinkFileLocation = "/home/griffin/SchoolCrashData-2.0/SchoolAccLinks.csv";
        Iterable<CSVRecord> fileParser2 = this.openNewParser(schoolAccidentLinkFileLocation);
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

            this.schoolAccidentLinkRepository.save(n);

        }
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
