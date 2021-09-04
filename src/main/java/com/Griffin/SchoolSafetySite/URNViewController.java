package com.Griffin.SchoolSafetySite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/urn")
public class URNViewController {
    private SchoolRepository schoolRepository;
    private SchoolAccidentLinkRepository schoolAccidentLinkRepository;
    private CasaultyRepository casaultyRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    URNViewController(SchoolRepository schoolRepository,
                      SchoolAccidentLinkRepository schoolAccidentLinkRepository,
                        CasaultyRepository casaultyRepository) {
        this.schoolRepository = schoolRepository;
        this.schoolAccidentLinkRepository = schoolAccidentLinkRepository;
        this.casaultyRepository = casaultyRepository;
    }

    @GetMapping
    public String showURNError()
    {
        return "urn";
    }

    @GetMapping("/{URN}")
    public ModelAndView showURNView(@PathVariable("URN") String URN) throws SQLException {
        ModelAndView mav = new ModelAndView(("urn"));
        School school =  schoolRepository.findAllByURN(URN);
        mav.addObject("school", school);
        mav.addObject("boroughAccidents", this.numberOfAccidentsOnSchoolRunInBorough(school.getDistrictAdministrative()));
        mav.addObject("constituencyAccidents", this.numberOfAccidentsOnSchoolRunInConstituency(school.getParliamentaryConstituency()));

        mav.addObject("casualties", this.getCasualtyOutputForSchool(URN));

        mav.addObject("MP", this.getMPName(school.getParliamentaryConstituency()));

        return mav;
    }

    private String getMPName(String constituency) throws SQLException {
        String sqlQuery = "SELECT distinct `Name (Display as)`FROM SchoolSafetySite.Mps WHERE Constituency = ?;";
        String MPName = null;
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement p = c.prepareStatement(sqlQuery);
        p.setString(1, constituency);
        ResultSet rs = p.executeQuery();
        while (rs.next())
        {
            MPName = rs.getString("Name (Display as)");
        }
        return MPName;
    }

    private List<casualtyOutput> getCasualtyOutputForSchool(String urn) throws SQLException {
        String sqlQuery = "SELECT casualty.casualty_output_string, school_accident_link.date, school_accident_link.time, casualty.age_sex_details, school_accident_link.lat, school_accident_link.lon" +
                " FROM school " +
                "inner join school_accident_link on school.urn = school_accident_link.schoolurn " +
                "inner join casualty on school_accident_link.accident_index = casualty.accident_index " +
                "where casualty.age_of_casualty < 18 " +
                "AND (left(school_accident_link.time,2) BETWEEN 8 AND 9 OR left(school_accident_link.time,2) BETWEEN 14 AND 15) " +
                "AND school_accident_link.day_of_week_number BETWEEN 2 and 6 " +
                "AND school_accident_link.distance < 600 " +
                "AND school.urn = ?" +
                "order by distance;";

        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement p = c.prepareStatement(sqlQuery);
        p.setString(1, urn);
        ResultSet rs = p.executeQuery();
        List<casualtyOutput> outputObjects = new ArrayList<>();
        while(rs.next())
        {
            outputObjects.add(new casualtyOutput(rs.getString("casualty_output_string"),
                                rs.getString("date"),
                                rs.getString("time"),
                                rs.getString("age_sex_details"),
                                rs.getString("lat"),
                                rs.getString("lon")
                                ));
        }
        c.close();
        return outputObjects;
    }

    /**
     * This gives the number of children that were injured on the school run for a given borough.
     * To be considered for this they have to be a child (0 - 17 years old)
     * Have been injured on a week day
     * where >= 7 time >= 16 on 24 hour clock
     * todo in future it might be worth considering school holidays
     * todo a speed improvement could be made by making the sql query return a number rather than having
     * to loop through the result set
     * @param borough a string representing the name of the borough
     * @return a number representing the number of accidents in that borough
     */
    private int numberOfAccidentsOnSchoolRunInBorough(String borough) throws SQLException {
        String sqlQuery = "SELECT distinct casualty.id\n" +
                "FROM casualty \n" +
                "INNER JOIN school_accident_link ON casualty.accident_index=school_accident_link.accident_index\n" +
                "INNER JOIN school ON school_accident_link.schoolurn = school.urn\n" +
                "WHERE casualty.age_of_casualty < 18\n" +
                " AND left(school_accident_link.time,2) BETWEEN 7 AND 16\n" +
                " AND school_accident_link.day_of_week_number BETWEEN 2 and 6 \n" +
                " and school.district_administrative = ?;";

        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement p = c.prepareStatement(sqlQuery);
        p.setString(1, borough);

        ResultSet rs = p.executeQuery();
        int numberOfRows = 0;
        while(rs.next())
        {
            numberOfRows++;
        }
        c.close(); //todo investigate
        return numberOfRows;
    }

    private int numberOfAccidentsOnSchoolRunInConstituency(String constituency) throws SQLException {
        String sqlQuery = "SELECT distinct casualty.id\n" +
                "FROM casualty \n" +
                "INNER JOIN school_accident_link ON casualty.accident_index=school_accident_link.accident_index\n" +
                "INNER JOIN school ON school_accident_link.schoolurn = school.urn\n" +
                "WHERE casualty.age_of_casualty < 18\n" +
                " AND left(school_accident_link.time,2) BETWEEN 7 AND 16\n" +
                " AND school_accident_link.day_of_week_number BETWEEN 2 and 6 \n" +
                " and school.parliamentary_constituency = ?;";

        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement p = c.prepareStatement(sqlQuery);
        p.setString(1, constituency);

        ResultSet rs = p.executeQuery();
        int numberOfRows = 0;
        while(rs.next())
        {
            numberOfRows++;
        }
        c.close();
        return numberOfRows;
    }

    @GetMapping("/add")
    public String addURNView()
    {
        this.schoolRepository.save(new School());
        return "urn";
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<School> getAllShools()
    {
        return schoolRepository.findAll();
    }

    @CrossOrigin(origins="*")
    @GetMapping("/search/{searchItem}")
    public @ResponseBody Iterable<School> schoolSearchResult(@PathVariable("searchItem") String searchItem)
    {
        return schoolRepository.findAllByEstablishmentNameContaining(searchItem);
    }

    @GetMapping("acc/{id}")
    public@ResponseBody Iterable<SchoolAccidentLink> accidentsByURN(@PathVariable("id") String schoolURN)
    {
        return schoolAccidentLinkRepository.findAllBySchoolURN(schoolURN);
    }

    @GetMapping("/search/acc/{accidentIndex}")
    public @ResponseBody Iterable<Casualty> casualtiesByAccidentIndex(@PathVariable("accidentIndex") String accidentIndex)
    {
        return casaultyRepository.findAllByAccidentIndex(accidentIndex);
    }

    @GetMapping("/wpc/{constituency}")
    public ModelAndView showMPView(@PathVariable("constituency") String constituency) throws SQLException {
        ModelAndView mav = new ModelAndView(("wpc"));

        mav.addObject("casualties", this.getCasualtyOutputForWPC(constituency));
        mav.addObject("constituencyAccidents", this.numberOfAccidentsOnSchoolRunInConstituency(constituency));
        mav.addObject("MP", this.getMPName(constituency));

        return mav;
    }



    private List<casualtyOutput> getCasualtyOutputForWPC(String constituency) throws SQLException {
        String sqlQuery = "SELECT casualty.casualty_output_string, school_accident_link.date, school_accident_link.time, casualty.age_sex_details, school_accident_link.lat, school_accident_link.lon" +
                " FROM school " +
                "inner join school_accident_link on school.urn = school_accident_link.schoolurn " +
                "inner join casualty on school_accident_link.accident_index = casualty.accident_index " +
                "where casualty.age_of_casualty < 18 " +
                "AND (left(school_accident_link.time,2) BETWEEN 8 AND 9 OR left(school_accident_link.time,2) BETWEEN 14 AND 15) " +
                "AND school_accident_link.day_of_week_number BETWEEN 2 and 6 " +
                "AND school_accident_link.distance < 600 " +
                "AND school.parliamentary_constituency = ?" +
                "order by distance;";

        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement p = c.prepareStatement(sqlQuery);
        p.setString(1, constituency);
        ResultSet rs = p.executeQuery();
        List<casualtyOutput> outputObjects = new ArrayList<>();
        while(rs.next())
        {
            outputObjects.add(new casualtyOutput(rs.getString("casualty_output_string"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("age_sex_details"),
                    rs.getString("lat"),
                    rs.getString("lon")
            ));
        }
        c.close();
        return outputObjects;
    }

    @GetMapping("/search/acc/all")
    public @ResponseBody Iterable<Casualty> allCasualties()
    {
        return casaultyRepository.findAll();
    }

}
