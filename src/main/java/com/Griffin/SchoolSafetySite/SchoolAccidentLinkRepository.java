package com.Griffin.SchoolSafetySite;

import org.springframework.data.repository.CrudRepository;

public interface SchoolAccidentLinkRepository extends CrudRepository<SchoolAccidentLink, Long>{
    SchoolAccidentLink findAllBySchoolURN(String SchoolURN);
}

