package com.Griffin.SchoolSafetySite;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/urn")
public class URNViewController {
    private SchoolRepository schoolRepository;
    private SchoolAccidentLinkRepository schoolAccidentLinkRepository;

    URNViewController(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
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

}
