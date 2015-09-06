package sir.wellington.commons.test.dates;

import java.time.Instant;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.Date;

/**
 * Generation methods for {@code Date}s
 *
 * @author SirWellington
 */
public class Dates
{

    /**
     * A Date representing now, when this method is called.
     *
     * @return
     */
    public static Date now()
    {
        return new Date();
    }

    /**
     * A date representing an instant in the past
     *
     * @param days The days ago from now
     *
     * @return
     */
    public static Date daysAgo(int days)
    {
        Instant instant = Instant.now().minus(days, DAYS);
        return new Date(instant.toEpochMilli());
    }

    /**
     * A date representing an instant in the future
     *
     * @param days The days ahead from now
     *
     * @return
     */
    public static Date daysAhead(int days)
    {
        Instant instant = Instant.now().plus(days, DAYS);
        return new Date(instant.toEpochMilli());
    }

    /**
     * A date representing an instant in the past
     *
     * @param hours The hours ago from now
     *
     * @return
     */
    public static Date hoursAgo(int hours)
    {
        Instant instant = Instant.now().minus(hours, HOURS);
        return new Date(instant.toEpochMilli());
    }

    /**
     * A date representing an instant in the future
     *
     * @param hours The hours ahead from now
     *
     * @return
     */
    public static Date hoursAhead(int hours)
    {
        Instant instant = Instant.now().plus(hours, HOURS);
        return new Date(instant.toEpochMilli());
    }

    /**
     * A date representing an instant in the past
     *
     * @param minutes The minutes ago from now
     *
     * @return
     */
    public static Date minutesAgo(int minutes)
    {
        Instant instant = Instant.now().minus(minutes, MINUTES);
        return new Date(instant.toEpochMilli());
    }

    /**
     * A date representing an instant in the future
     *
     * @param minutes the minutes ago ahead from now
     *
     * @return
     */
    public static Date minutesAhead(int minutes)
    {
        Instant instant = Instant.now().plus(minutes, MINUTES);
        return new Date(instant.toEpochMilli());
    }

}
