package com.Griffin.SchoolSafetySite;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/urn")
public class URNViewController {
    private SchoolRepository schoolRepository;
    private SchoolAccidentLinkRepository schoolAccidentLinkRepository;
    private CasaultyRepository casaultyRepository;

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
    public ModelAndView showURNView(@PathVariable("URN") String URN)
    {
        ModelAndView mav = new ModelAndView(("urn"));
        mav.addObject("school", schoolRepository.findAllByURN(URN));
        List<SchoolAccidentLink> schoolAccidentLinkSet = new ArrayList<>();
        schoolAccidentLinkSet.addAll(schoolAccidentLinkRepository.findAllBySchoolURN(URN));

        for (SchoolAccidentLink schoolAccidentLink : schoolAccidentLinkSet)
        {
            schoolAccidentLink.setCasualtyList(casaultyRepository.findAllByAccidentIndex(schoolAccidentLink.getAccidentIndex()));
        }
        mav.addObject("accidents", schoolAccidentLinkSet);

        return mav;
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

    @CrossOrigin(origins = "*") //todo change this
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

    @GetMapping("/search/acc/all")
    public @ResponseBody Iterable<Casualty> allCasualties()
    {
        return casaultyRepository.findAll();
    }

}
