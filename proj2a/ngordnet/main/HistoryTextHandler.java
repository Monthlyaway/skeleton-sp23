package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap map;
    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        StringBuilder response = new StringBuilder();
        for (String word : words) {
            response.append(word).append(": ");
            TimeSeries ts = map.weightHistory(word, startYear, endYear);
            response.append(ts.toString());
        }
        response.append("\n");

        return response.toString();
    }
}
