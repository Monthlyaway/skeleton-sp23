package ngordnet.ngrams;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.In;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 * <p>
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    // TODO: Add any necessary static/instance variables.
    private Map<String, TimeSeries> wordsMap;
    TimeSeries countsSeries;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In wordsFile = new In(wordsFilename);
        In countsFile = new In(countsFilename);

        wordsMap = new HashMap<>();
        countsSeries = new TimeSeries();

        while (wordsFile.hasNextLine()) {
            String word = wordsFile.readString();
            int year = wordsFile.readInt();
            double count = wordsFile.readDouble();
            wordsFile.readLine();

            if (wordsMap.get(word) == null) {
                TimeSeries ts = new TimeSeries();
                ts.put(year, count);
                wordsMap.put(word, ts);
            } else {
                TimeSeries presentTS = wordsMap.get(word);
                presentTS.put(year, count);
                wordsMap.put(word, presentTS);
            }
        }

        while (countsFile.hasNextLine()) {
            String line = countsFile.readLine();
            String[] tokens = line.split(",");
            int year = Integer.parseInt(tokens[0].trim());
            double countOfWords = Double.parseDouble(tokens[1].trim());

            countsSeries.put(year, countOfWords);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!validateYearsAug(startYear, endYear)) {
            throw new IllegalArgumentException("startYear < MIN_YEAR || endYear > MAX_YEAR");
        }

        TimeSeries originalSeries = wordsMap.get(word);
        return new TimeSeries(originalSeries, startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        return new TimeSeries(wordsMap.get(word));
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(countsSeries);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!validateYearsAug(startYear, endYear)) {
            throw new IllegalArgumentException("startYear < MIN_YEAR || endYear > MAX_YEAR");
        }

        TimeSeries ts = new TimeSeries(wordsMap.get(word), startYear, endYear);
        return ts.dividedBy(countsSeries);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries weight = new TimeSeries(wordsMap.get(word));
        return weight.dividedBy(countsSeries);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries sum = new TimeSeries();
        TimeSeries result;

        /*
         * ALl the variables referenced in lambda scope are implicitly final, which means they
         * can not be changed after initialization.
         */
        for (String word : words) {
            TimeSeries weight = weightHistory(word, startYear, endYear);
            sum = sum.plus(weight);
        }
        return sum;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }

    private boolean validateYearsAug(int startYear, int endYear) {
        return startYear >= MIN_YEAR && endYear <= MAX_YEAR;
    }
}
