
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
 * @version 1.3
 */
public class Date
        implements Printable, Comparable<Date>
{
    public static final int CURRENT_YEAR = 2025;
    public static final int MINIMUM_YEAR = 1800;

    private static final int EVEN_CHECK = 2;
    private static final int EVEN = 0;
    private static final int ODD = 1;

    private static final int YEAR_PLACEMENT = 1000;
    private static final int MILLENNIA_CHECK = 100;
    private static final int REMOVE_MILLENNIA = 1000;
    private static final int MILLENNIA_2000S = 20;
    private static final int MILLENNIA_1800S = 18;
    private static final int LEAP_YEAR_CHECK = 4;
    private static final int LEAP_YEAR = 0;

    private static final int MINIMUM_MONTH = 1;
    private static final int MAXIMUM_MONTH = 12;
    private static final int MONTH_PLACEMENT = 100;
    private static final int MONTH_MIDYEAR_SWITCH = 7;

    private static final int MINIMUM_DAY = 1;
    private static final int MAXIMUM_DAY_LONG = 31;
    private static final int MAXIMUM_DAY_SHORT = 30;
    private static final int MAXIMUM_DAY_FEBRUARY = 28;
    private static final int MAXIMUM_DAY_FEB_LEAP_YEAR = 29;

    private static final int JANUARY_NUM = 1;
    private static final int FEBRUARY_NUM = 2;
    private static final int MARCH_NUM = 3;
    private static final int APRIL_NUM = 4;
    private static final int MAY_NUM = 5;
    private static final int JUNE_NUM = 6;
    private static final int JULY_NUM = 7;
    private static final int AUGUST_NUM = 8;
    private static final int SEPTEMBER_NUM = 9;
    private static final int OCTOBER_NUM = 10;
    private static final int NOVEMBER_NUM = 11;
    private static final int DECEMBER_NUM = 12;

    private static final int JANUARY_CODE = 1;
    private static final int FEBRUARY_CODE = 4;
    private static final int MARCH_CODE = 4;
    private static final int APRIL_CODE = 0;
    private static final int MAY_CODE = 2;
    private static final int JUNE_CODE = 5;
    private static final int JULY_CODE = 0;
    private static final int AUGUST_CODE = 3;
    private static final int SEPTEMBER_CODE = 6;
    private static final int OCTOBER_CODE = 1;
    private static final int NOVEMBER_CODE = 4;
    private static final int DECEMBER_CODE = 6;

    private static final int SATURDAY_CODE = 0;
    private static final int SUNDAY_CODE = 1;
    private static final int MONDAY_CODE = 2;
    private static final int TUESDAY_CODE = 3;
    private static final int WEDNESDAY_CODE = 4;
    private static final int THURSDAY_CODE = 5;
    private static final int FRIDAY_CODE = 6;

    private static final int NUM_OF_DAYS_IN_WEEK = 7;
    private static final int DAY_OF_WEEK_STEP3 = 4;
    private static final int EXTRA_CALC_2000S = 6;
    private static final int EXTRA_CALC_1800S = 2;
    private static final int EXTRA_CALC_LEAP_YEAR = 6;

    private final int year;
    private final int month;
    private final int day;

    /**
     * Constructs a date object with a year, month and day and validates
     * that each value is valid.
     *
     * @param year the date object's year.
     * @param month the date object's month.
     * @param day the date object's day.
     */
    public Date(final int year,
                final int month,
                final int day)
    {
        validateYear(year);
        validateMonth(month);
        validateDay(year, month, day);


        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Returns the date object's day.
     * @return day, the date object's day.
     */
    public int getDay() { return day; }

    /**
     * Returns the date object's month.
     * @return month, the date object's month.
     */
    public int getMonth() { return month; }

    /**
     * Returns the date object's year.
     * @return year, the date object's year.
     */
    public int getYear() { return year; }

    /**
     * Converts the date object's year, month and day into a YYYYMMDD format.
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
     * @return the date as a String Month Day, Year
     */
    public String getMDY() {
        final StringBuilder sb;

        sb = new StringBuilder();

        switch (month)
        {
            case JANUARY_NUM -> sb.append("January");
            case FEBRUARY_NUM -> sb.append("February");
            case MARCH_NUM -> sb.append("March");
            case APRIL_NUM -> sb.append("April");
            case MAY_NUM -> sb.append("May");
            case JUNE_NUM -> sb.append("June");
            case JULY_NUM -> sb.append("July");
            case AUGUST_NUM -> sb.append("August");
            case SEPTEMBER_NUM -> sb.append("September");
            case OCTOBER_NUM -> sb.append("October");
            case NOVEMBER_NUM -> sb.append("November");
            case DECEMBER_NUM -> sb.append("December");
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
     * @return dayOfTheWeek, the day of the week as a number from 0-6 which converts to Saturday-Friday.
     */
    public int getDayOfTheWeek()
    {
        final int yearDigits;
        final int maxDays;
        final int monthCode;
        int dayOfTheWeek;

        yearDigits = getLastTwoDigitsOfYear(year);
        maxDays = getMaximumDaysInMonth(month);
        monthCode = getMonthCode(month);

        dayOfTheWeek = dayOfTheWeekCalculation(yearDigits, maxDays, monthCode, year, month);

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
     * @param d the object to be compared.
     * @return difference as an int
     */
    @Override
    public int compareTo(Date d)
    {
        if(d == null)
        {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if(getYear() != d.getYear())
        {
            return getYear() - d.getYear();
        }
        else if(getMonth() != d.getMonth())
        {
            return getMonth() - d.getMonth();
        }
        else
        {
            return getDay() - d.getDay();
        }
    }

    /**
     * Returns the day of the week in its word form or String form.
     * @return the day of the week as a word
     */
    public String DayOfTheWeekStr() {
        final String dayStr;

        switch (this.getDayOfTheWeek())
        {
            case SATURDAY_CODE -> dayStr = "Saturday";
            case SUNDAY_CODE -> dayStr = "Sunday";
            case MONDAY_CODE -> dayStr = "Monday";
            case TUESDAY_CODE -> dayStr = "Tuesday";
            case WEDNESDAY_CODE -> dayStr = "Wednesday";
            case THURSDAY_CODE -> dayStr = "Thursday";
            case FRIDAY_CODE -> dayStr = "Friday";
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

        if (year / MILLENNIA_CHECK == MILLENNIA_2000S)
        {
            dayOfTheWeek += EXTRA_CALC_2000S;
        }

        else if (year / MILLENNIA_CHECK == MILLENNIA_1800S)
        {
            dayOfTheWeek += EXTRA_CALC_1800S;
        }

        else if (isLeapYear(year) && month <= FEBRUARY_NUM)
        {
            dayOfTheWeek += EXTRA_CALC_LEAP_YEAR;
        }
        dayOfTheWeek += yearDigits / MAXIMUM_MONTH;
        dayOfTheWeek += yearDigits % MAXIMUM_MONTH;
        dayOfTheWeek += (yearDigits % MAXIMUM_MONTH) / DAY_OF_WEEK_STEP3;
        dayOfTheWeek += maxDays;
        dayOfTheWeek += monthCode;
        dayOfTheWeek %= NUM_OF_DAYS_IN_WEEK;
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

        if (month <= MONTH_MIDYEAR_SWITCH)
        {
            if (month % EVEN_CHECK == ODD)
            {
                maximumDays = MAXIMUM_DAY_LONG;
            }
            else if (month % EVEN_CHECK == EVEN)
            {
                maximumDays = MAXIMUM_DAY_SHORT;
            }
        }
        else
        {
            if (month % EVEN_CHECK == EVEN)
            {
                maximumDays = MAXIMUM_DAY_LONG;
            }
            else
            {
                maximumDays = MAXIMUM_DAY_SHORT;
            }
        }

        return maximumDays;
    }

    private static int getMonthCode(final int month)
    {
        int monthCode;
        switch (month)
        {
            case JANUARY_NUM -> monthCode = JANUARY_CODE;
            case FEBRUARY_NUM -> monthCode = FEBRUARY_CODE;
            case MARCH_NUM -> monthCode = MARCH_CODE;
            case APRIL_NUM -> monthCode = APRIL_CODE;
            case MAY_NUM -> monthCode = MAY_CODE;
            case JUNE_NUM -> monthCode = JUNE_CODE;
            case JULY_NUM -> monthCode = JULY_CODE;
            case AUGUST_NUM -> monthCode = AUGUST_CODE;
            case SEPTEMBER_NUM -> monthCode = SEPTEMBER_CODE;
            case OCTOBER_NUM -> monthCode = OCTOBER_CODE;
            case NOVEMBER_NUM -> monthCode = NOVEMBER_CODE;
            case DECEMBER_NUM -> monthCode = DECEMBER_CODE;
            default -> throw new IllegalArgumentException("month is not a valid month in the year (1-12)");
        }
        return monthCode;
    }

    private static void validateYear(final int year)
    {
        if (year < MINIMUM_YEAR ||
                year > CURRENT_YEAR)
        {
            throw new IllegalArgumentException("Invalid Year " + year);
        }
    }

    private static void validateMonth(final int month)
    {
        if (month < MINIMUM_MONTH ||
                month > MAXIMUM_MONTH) {
            throw new IllegalArgumentException("Invalid Month " + month);
        }
    }

    private static void validateDay(final int year,
                                    final int month,
                                    final int day)
    {
        if (day < MINIMUM_DAY)
        {
            throw new IllegalArgumentException("Invalid day " + day);
        }
        else if (day > getMaximumDaysInMonth(month))
        {
            throw new IllegalArgumentException("Invalid day " + day + " for month");
        }
        else if (month == FEBRUARY_NUM)
        {
            if (isLeapYear(year) &&
                    day > MAXIMUM_DAY_FEB_LEAP_YEAR)
            {
                throw new IllegalArgumentException("Invalid day " + day + " for Feb");
            }
            else if (isLeapYear(year) &&
                    day > MAXIMUM_DAY_FEBRUARY)
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
