package com.petrov.dimitar.googlesearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;

@Service
public class SearchService {

    private static final String GOOGLE_LOCATION = "https://www.google.co.uk/search";

    public Optional<SearchResult> search(@RequestParam("q") String query) throws IOException {
        String url = GOOGLE_LOCATION + "?q=" + query;

        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(5000)
                .get();

        Elements results = document.select("div#search").select("h3.r > a");

        if (results.size() < 2) {
            return Optional.empty();
        }

        String href = parseUrlFromAttr(results.get(1).attr("href"));
        String text = results.get(1).text();

        return Optional.of(new SearchResult(href, text));
    }

    private String parseUrlFromAttr(String href) {
        return href.substring(href.indexOf("=") + 1, href.indexOf("&"));
    }

}
