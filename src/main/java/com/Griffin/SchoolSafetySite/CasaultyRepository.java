package com.Griffin.SchoolSafetySite;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CasaultyRepository extends CrudRepository<Casualty, Long>{
    List<Casualty> findAllByAccidentIndex(String AccidentIndex);

}


