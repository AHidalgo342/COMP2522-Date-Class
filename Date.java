/**
 * Date represents a date including of a year, month and day. It has
 * methods to retrieve the specified year, month or day and methods to
 * return the entire date or the specific day of the week.
 *
 * @author Ervin S
 * @author Carl M
 * @author Jonathan Y
 * @author Alex H
 * @author Felix N
 * @author Glenn D
 * @author Szymon Z
 * @author Daniel C
 * @version 1.6
 */
public class Date implements Printable, Comparable<Date>
{
    /** Minimum and maximum year constraints */
    public static final int YEAR_CURRENT = 2025;
    public static final int YEAR_MINIMUM = 1800;

    /** Even/odd check-related constants */
    private static final int EVEN_CHECK = 2;
    private static final int EVEN       = 0;
    private static final int ODD        = 1;

    /** Leap year-related constants */
    private static final int YEAR_PLACEMENT   = 1000;
    private static final int MILLENNIA_CHECK  = 100;
    private static final int REMOVE_MILLENNIA = 1000;
    private static final int MILLENNIA_2000S  = 20;
    private static final int MILLENNIA_1800S  = 18;
    private static final int LEAP_YEAR_CHECK  = 4;
    private static final int LEAP_YEAR        = 0;

    /** Month-related constants */
    private static final int MONTH_MINIMUM        = 1;
    private static final int MONTH_MAXIMUM        = 12;
    private static final int MONTH_PLACEMENT      = 100;
    private static final int MONTH_MIDYEAR_SWITCH = 7;

    /** Minimum and maximum day constraints */
    private static final int DAY_MINIMUM                    = 1;
    private static final int DAY_MAXIMUM_LONG               = 31;
    private static final int DAY_MAXIMUM_SHORT              = 30;
    private static final int DAY_FEBRUARY_MAXIMUM           = 28;
    private static final int DAY_FEBRUARY_MAXIMUM_LEAP_YEAR = 29;

    /** Numeric codes for months */
    private static final int NUM_JANUARY   = 1;
    private static final int NUM_FEBRUARY  = 2;
    private static final int NUM_MARCH     = 3;
    private static final int NUM_APRIL     = 4;
    private static final int NUM_MAY       = 5;
    private static final int NUM_JUNE      = 6;
    private static final int NUM_JULY      = 7;
    private static final int NUM_AUGUST    = 8;
    private static final int NUM_SEPTEMBER = 9;
    private static final int NUM_OCTOBER   = 10;
    private static final int NUM_NOVEMBER  = 11;
    private static final int NUM_DECEMBER  = 12;

    /** Month codes, used in the getDayOfWeek() formula */
    private static final int MONTH_CODE_JANUARY   = 1;
    private static final int MONTH_CODE_FEBRUARY  = 4;
    private static final int MONTH_CODE_MARCH     = 4;
    private static final int MONTH_CODE_APRIL     = 0;
    private static final int MONTH_CODE_MAY       = 2;
    private static final int MONTH_CODE_JUNE      = 5;
    private static final int MONTH_CODE_JULY      = 0;
    private static final int MONTH_CODE_AUGUST    = 3;
    private static final int MONTH_CODE_SEPTEMBER = 6;
    private static final int MONTH_CODE_OCTOBER   = 1;
    private static final int MONTH_CODE_NOVEMBER  = 4;
    private static final int MONTH_CODE_DECEMBER  = 6;

    /** Numeric codes for days */
    private static final int CODE_SATURDAY  = 0;
    private static final int CODE_SUNDAY    = 1;
    private static final int CODE_MONDAY    = 2;
    private static final int CODE_TUESDAY   = 3;
    private static final int CODE_WEDNESDAY = 4;
    private static final int CODE_THURSDAY  = 5;
    private static final int CODE_FRIDAY    = 6;

    /** getDayOfWeek() extra additions */
    private static final int EXTRA_CALC_2000S     = 6;
    private static final int EXTRA_CALC_1800S     = 2;
    private static final int EXTRA_CALC_LEAP_YEAR = 6;

    /** Number of days in a week */
    private static final int NUM_DAYS_IN_WEEK     = 7;

    /** Number to be multiplied in step 3 of getDayOfWeek() formula */
    private static final int DAY_OF_WEEK_STEP3    = 4;

    private final int year;
    private final int month;
    private final int day;

    /**
     * Constructs a date object with a year, month and day and validates
     * that each value is valid.
     *
     * @param year  the date object's year.
     * @param month the date object's month.
     * @param day   the date object's day.
     */
    public Date(final int year,
                final int month,
                final int day)
    {
        validateYear(year);
        validateMonth(month);
        validateDay(year,
                    month,
                    day);


        this.year  = year;
        this.month = month;
        this.day   = day;
    }

    /**
     * Returns the date object's day.
     *
     * @return day, the date object's day.
     */
    public int getDay()
    {
        return day;
    }

    /**
     * Returns the date object's month.
     *
     * @return month, the date object's month.
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * Returns the date object's year.
     *
     * @return year, the date object's year.
     */
    public int getYear()
    {
        return year;
    }

    /**
     * Converts the date object's year, month and day into a YYYYMMDD format.
     *
     * @return YYYYMMDD, the date object's year, month and day in YYYYMMDD format.
     */
    public int getYYYYMMDD()
    {
        int YYYYMMDD;

        YYYYMMDD = year * YEAR_PLACEMENT;
        YYYYMMDD += month * MONTH_PLACEMENT;
        YYYYMMDD += day;

        return YYYYMMDD;
    }


    /**
     * Converts the date object's year, month and day integers into a String Month Day, Year format.
     * ex. January 1, 2025
     *
     * @return the date as a String Month Day, Year
     */
    public String getMDY()
    {
        final StringBuilder sb;

        sb = new StringBuilder();

        switch(month)
        {
            case NUM_JANUARY   -> sb.append("January");
            case NUM_FEBRUARY  -> sb.append("February");
            case NUM_MARCH     -> sb.append("March");
            case NUM_APRIL     -> sb.append("April");
            case NUM_MAY       -> sb.append("May");
            case NUM_JUNE      -> sb.append("June");
            case NUM_JULY      -> sb.append("July");
            case NUM_AUGUST    -> sb.append("August");
            case NUM_SEPTEMBER -> sb.append("September");
            case NUM_OCTOBER   -> sb.append("October");
            case NUM_NOVEMBER  -> sb.append("November");
            case NUM_DECEMBER  -> sb.append("December");
            default -> throw new IllegalArgumentException("month is not a valid month in the year (1-12)");
        }

        sb.append(" ");
        sb.append(day);
        sb.append(", ");
        sb.append(year);

        return sb.toString();
    }

    /**
     * Calculates the day of the week of a given date. It does a 7-step calculation to
     * convert a date to a day of the week.
     * <p>
     * <li>Step 1: calculates the number of twelves in the last two digits of the year.</li>
     * <li>Step 2: calculates the remainder of the step 1.</li>
     * <li>Step 3: calculates the number of fours in step 2.</li>
     * <li>Step 4: Add steps 1 - 3 and add the amount of days in the month.</li>
     * <li>Step 5: Add the month code. (referring to jfmamjjasond: 144025036146) </li>
     * <li>Step 6: Find the remainder of the current number divided by seven.</li>
     * <li>Step 7: Convert the final number to the corresponding day of the week, going
     * from 0-6 to Saturday-Friday.</li>
     * </p>
     * <p>
     * Additional calculations are added for:
     * <li>January/February during leap years, add 6 at the start.</li>
     * <li>All dates in the 2000's, add 6 at the start.</li>
     * <li>All dates in the 1800's add 2 at the start.</li>
     * </p>
     *
     * @return dayOfTheWeek, the day of the week as a number from 0-6 which converts to Saturday-Friday.
     */
    public int getDayOfTheWeek()
    {
        final int yearDigits;
        final int maxDays;
        final int monthCode;
        final int dayOfTheWeek;

        yearDigits = getLastTwoDigitsOfYear(year);
        maxDays    = getMaximumDaysInMonth(month);
        monthCode  = getMonthCode(month);

        dayOfTheWeek = dayOfTheWeekCalculation(yearDigits,
                                               maxDays,
                                               monthCode,
                                               year,
                                               month);

        return dayOfTheWeek;
    }

    /**
     * Prints this Date to console.
     * See {@code getMDY()}
     */
    @Override
    public void display()
    {
        System.out.println(getMDY());
    }

    /**
     * Compares this Date to the given Date.
     * if both years are equal, then returns difference in months.
     * if months are equal, it returns the difference in days.
     *
     * @param compareDate the object to be compared.
     * @return difference as an int
     */
    @Override
    public int compareTo(final Date compareDate)
    {
        if(compareDate == null)
        {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if(getYear() != compareDate.getYear())
        {
            return getYear() - compareDate.getYear();
        }

        if(getMonth() != compareDate.getMonth())
        {
            return getMonth() - compareDate.getMonth();
        }
        else
        {
            return getDay() - compareDate.getDay();
        }
    }

    /**
     * Returns the day of the week in its word form or String form.
     *
     * @return the day of the week as a word
     */
    public String DayOfTheWeekStr()
    {
        final String dayStr;

        switch(this.getDayOfTheWeek())
        {
            case CODE_SATURDAY ->  dayStr = "Saturday";
            case CODE_SUNDAY ->    dayStr = "Sunday";
            case CODE_MONDAY ->    dayStr = "Monday";
            case CODE_TUESDAY ->   dayStr = "Tuesday";
            case CODE_WEDNESDAY -> dayStr = "Wednesday";
            case CODE_THURSDAY ->  dayStr = "Thursday";
            case CODE_FRIDAY ->    dayStr = "Friday";
            default -> dayStr = "0";
        }

        return dayStr;
    }

    private static int dayOfTheWeekCalculation(final int yearDigits,
                                               final int maxDays,
                                               final int monthCode,
                                               final int year,
                                               final int month)
    {
        int dayOfTheWeek;

        dayOfTheWeek = 0;

        if(year / MILLENNIA_CHECK == MILLENNIA_2000S)
        {
            dayOfTheWeek += EXTRA_CALC_2000S;
        }
        else if(year / MILLENNIA_CHECK == MILLENNIA_1800S)
        {
            dayOfTheWeek += EXTRA_CALC_1800S;
        }
        else if(isLeapYear(year) && month <= NUM_FEBRUARY)
        {
            dayOfTheWeek += EXTRA_CALC_LEAP_YEAR;
        }

        dayOfTheWeek += yearDigits / MONTH_MAXIMUM;
        dayOfTheWeek += yearDigits % MONTH_MAXIMUM;
        dayOfTheWeek += (yearDigits % MONTH_MAXIMUM) / DAY_OF_WEEK_STEP3;
        dayOfTheWeek += maxDays;
        dayOfTheWeek += monthCode;
        dayOfTheWeek %= NUM_DAYS_IN_WEEK;

        return dayOfTheWeek;
    }

    private static int getLastTwoDigitsOfYear(final int year)
    {
        int lastTwoDigits;

        lastTwoDigits = (year / MILLENNIA_CHECK) * REMOVE_MILLENNIA;

        return lastTwoDigits;
    }

    private static int getMaximumDaysInMonth(final int month)
    {
        int maximumDays;

        maximumDays = 0;

        if(month <= MONTH_MIDYEAR_SWITCH)
        {
            if(month % EVEN_CHECK == ODD)
            {
                maximumDays = DAY_MAXIMUM_LONG;
            }
            else if(month % EVEN_CHECK == EVEN)
            {
                maximumDays = DAY_MAXIMUM_SHORT;
            }
        }
        else
        {
            if(month % EVEN_CHECK == EVEN)
            {
                maximumDays = DAY_MAXIMUM_LONG;
            }
            else
            {
                maximumDays = DAY_MAXIMUM_SHORT;
            }
        }

        return maximumDays;
    }

    private static int getMonthCode(final int month)
    {
        int monthCode;

        switch(month)
        {
            case NUM_JANUARY   -> monthCode = MONTH_CODE_JANUARY;
            case NUM_FEBRUARY  -> monthCode = MONTH_CODE_FEBRUARY;
            case NUM_MARCH     -> monthCode = MONTH_CODE_MARCH;
            case NUM_APRIL     -> monthCode = MONTH_CODE_APRIL;
            case NUM_MAY       -> monthCode = MONTH_CODE_MAY;
            case NUM_JUNE      -> monthCode = MONTH_CODE_JUNE;
            case NUM_JULY      -> monthCode = MONTH_CODE_JULY;
            case NUM_AUGUST    -> monthCode = MONTH_CODE_AUGUST;
            case NUM_SEPTEMBER -> monthCode = MONTH_CODE_SEPTEMBER;
            case NUM_OCTOBER   -> monthCode = MONTH_CODE_OCTOBER;
            case NUM_NOVEMBER  -> monthCode = MONTH_CODE_NOVEMBER;
            case NUM_DECEMBER  -> monthCode = MONTH_CODE_DECEMBER;

            default -> throw new IllegalArgumentException("month is not a valid month in the year (1-12)");
        }
        return monthCode;
    }

    private static void validateYear(final int year)
    {
        if(year < YEAR_MINIMUM || year > YEAR_CURRENT)
        {
            throw new IllegalArgumentException("Invalid Year " + year);
        }
    }

    private static void validateMonth(final int month)
    {
        if(month < MONTH_MINIMUM || month > MONTH_MAXIMUM)
        {
            throw new IllegalArgumentException("Invalid Month " + month);
        }
    }

    private static void validateDay(final int year,
                                    final int month,
                                    final int day)
    {
        if(day < DAY_MINIMUM)
        {
            throw new IllegalArgumentException("Invalid day " + day);
        }

        if(day > getMaximumDaysInMonth(month))
        {
            throw new IllegalArgumentException("Invalid day " + day + " for month");
        }

        if(month == NUM_FEBRUARY)
        {
            if(isLeapYear(year) && day > DAY_FEBRUARY_MAXIMUM_LEAP_YEAR)
            {
                throw new IllegalArgumentException("Invalid day " + day + " for Feb");
            }
            else if(isLeapYear(year) && day > DAY_FEBRUARY_MAXIMUM)
            {
                throw new IllegalArgumentException("Invalid day " + day + " for Feb");
            }
        }
    }

    private static boolean isLeapYear(int year)
    {
        boolean leapYear;

        leapYear = year % LEAP_YEAR_CHECK == LEAP_YEAR;

        return leapYear;
    }
}
