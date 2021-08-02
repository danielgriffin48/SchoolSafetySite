package com.Griffin.SchoolSafetySite;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SchoolAccidentLinkRepository extends CrudRepository<SchoolAccidentLink, Long>{
    List<SchoolAccidentLink> findAllBySchoolURN(String SchoolURN);
}

