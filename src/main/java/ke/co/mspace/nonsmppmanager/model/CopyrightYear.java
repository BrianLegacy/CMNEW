
package ke.co.mspace.nonsmppmanager.model;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Norrey Osako
 */
public class CopyrightYear {
    private int year;

    /**
     * Creates a new instance of CopyrightYear
     */
    public CopyrightYear() {
    }

    public int getYear() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);

        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
