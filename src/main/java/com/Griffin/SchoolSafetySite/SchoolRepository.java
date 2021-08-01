package com.Griffin.SchoolSafetySite;

import org.springframework.data.repository.CrudRepository;

public interface SchoolRepository extends CrudRepository<School, Long> {
    School findAllByURN(String URN);
    Iterable<School> findAllByEstablishmentNameContaining(String searchItem);
}
