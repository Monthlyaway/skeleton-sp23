package ngordnet.ngrams;

import net.sf.saxon.functions.Minimax;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        if (startYear < MIN_YEAR || endYear > MAX_YEAR) {
            throw new IllegalArgumentException("startYear < MIN_YEAR || endYear > MAX_YEAR");
        }

        ts.forEach((year, value) -> {
            if (year >= startYear && year <= endYear) {
                this.put(year, value);
            }
        });
    }

    /**
     * Returns all years for this TimeSeries (in any order). To convert a Set to a List in Java, you can use the ArrayList constructor that takes a Collection as its argument.
     */
    public List<Integer> years() {
        // keySet() won't return null
        return new ArrayList<>(this.keySet());
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Integer> listOfYears = this.years();
        List<Double> listOfData = new ArrayList<>();

        for (Integer year : listOfYears) {
            listOfData.add(this.get(year));
        }

        return listOfData;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     * <p>
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries rhs) {
        TimeSeries sum = new TimeSeries(this);

        List<Integer> rhsYears = rhs.years();
        for (int rhsYear : rhsYears) {
            Double thisData = sum.get(rhsYear);
            Double rhsData = rhs.get(rhsYear);
            if (thisData == null) {
                sum.put(rhsYear, rhsData);
            } else {
                sum.put(rhsYear, thisData + rhsData);
            }
        }
        return sum;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     * <p>
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries rhs) {
        TimeSeries result = new TimeSeries(this);
        // rhs should have all the years this series has
        if (rhs != null) {
            this.forEach((year, value) -> {
                Double rhsValue = rhs.get(year);
                if (rhsValue == null) {
                    throw new IllegalArgumentException("Year " + year + " not found in rhs Timeseries.");
                }
                result.put(year, value / rhsValue);
            });
        }

        return result;
    }

    public TimeSeries(TimeSeries other) {
        /*
         * If other is null, and you call the constructor public TimeSeries
         * (TimeSeries other), it will invoke the super constructor with null.
         * The TreeMap copy constructor used in this case will create an empty
         * TimeSeries object.
         */
        super(other); // Use the TreeMap copy constructor
    }

}
