package com.Griffin.SchoolSafetySite;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class casualtyOutput {
    private String casualtyOutputString;
    private String date;
    private String time;
    private String ageSexDetails;
    private String lat;
    private String lon;


}
