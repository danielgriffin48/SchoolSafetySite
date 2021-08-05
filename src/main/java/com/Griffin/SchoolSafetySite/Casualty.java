package com.Griffin.SchoolSafetySite;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;


/**
 * This represents the casualty of accidents. There should be a single instance of each casualty for each accident, but
 * there can be multiple casualties for a single accident. It is also possible that each accident and as such each
 * casualty can be associated with multiple schools.
 *
 * When importing data a check should be made to isIncludeInDatabase() to decide whether this should be included. This
 * MUST be done before anything else as other methods, including getters, rely on this being checked.
 */
@Entity
@NoArgsConstructor
@Data
public class Casualty {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    private long id;

    private String accidentIndex;

    // casualtyClass 1 - driver or rider, 2 passenger, 3 pedestrian
    private String casualtyClass;

    //1 male, 2 female, -1 missing
    private int sexOfCasualty;

    private int ageOfCasualty;
    // severity - 1 fatal, 2 serious, 3 slight
    private int casualtySeverity;

    // 0 - pedestrian
    private String casualtyType;

    //0	Not a Pedestrian
    //1	Crossing on pedestrian crossing facility
    //2	Crossing in zig-zag approach lines
    //3	Crossing in zig-zag exit lines
    //4	Crossing elsewhere within 50m. of pedestrian crossing
    //5	In carriageway, crossing elsewhere
    //6	On footway or verge
    //7	On refuge, central island or central reservation
    //8	In centre of carriageway - not on refuge, island or central reservation
    //9	In carriageway, not crossing
    //10	Unknown or other
    //-1	Data missing or out of range
    private String pedestrianLocation;
    private String ageSexDetails;
    private String casualtyOutputString;

    /**
     * Generates the string that should be displayed when this casualty is called. It assumes that
     * isIncludeInDatabase has removed any items with missing values or that should not be included. Any changes to
     * those should be refelected here. At presents it assumes that the age and gender are known.
     */
    private void setOutputString() {
        this.ageSexDetails = String.format("An %s year old %s", this.getAgeOfCasualty(), this.getSexOfCasualty());
        this.casualtyOutputString = String.format("An %s year old %s was %s %s", this.getAgeOfCasualty(), this.getSexOfCasualty(), this.getCasualtySeverity(),
                this.getCrossingRoad());
    }

    private String getCrossingRoad()
    {

        int pedLocation = Integer.parseInt(this.getPedestrianLocation());
        if (pedLocation == 1)
        {
            return "while using a pedestrian crossing.";
        }
        if ( pedLocation >= 2 && pedLocation <= 5)
        {
            return "while crossing the road";
        }
        if ( pedLocation == 6)
        {
            return "while on the path.";
        }
        else {
            return ".";
        }
    }

    public String getCasualtySeverity() {
        if (this.casualtySeverity == 1 )
        {
            return "fatally injured";
        }
        if (this.casualtySeverity == 2) {
            return "seriously injured";
        }
        else {
            return "injured";
        }
    }

    public String getSexOfCasualty() {

        if (this.sexOfCasualty == 1)
        {
            if (this.ageOfCasualty < 18)
            {
                return "boy";
            }
            else {
                return "man";
            }
        }
        else {
            if (this.ageOfCasualty < 18)
            {
                return "girl";
            }
            else {
                return "women";
            }
        }

    }

    /**
     * Determines whether this should be included in the database. Should only be included if the data is in the correct
     * format and the value is appropriate to be included.
     * For instance, the age must be known and be able to be parsed as an integer. If either of these are not true,
     * it should be removed. This should be updated in tandem with setOutputString as that relies on this doing its job.
     * @return whether this casualty meets the criteria to be included. If it is to be included the outputString is set.
     */
    public boolean isIncludeInDatabase()
    {
        // if age is unknown or is not an integer


        if (this.ageOfCasualty == -1)
        {
            return false;
        }

        if (!this.isAPedestrian()) {
            return false;
        }
        this.setOutputString();
        return true;
    }


    private boolean isAPedestrian()
    {
        int pedestrianAsNumber;
        try {
            pedestrianAsNumber = Integer.parseInt(this.getPedestrianLocation());
        }
        catch (NumberFormatException e) {
            return false;
        }

        if (pedestrianAsNumber == 0 || pedestrianAsNumber == 10 || pedestrianAsNumber == -1)
        {
            return false;
        }

        return true;
    }








}
