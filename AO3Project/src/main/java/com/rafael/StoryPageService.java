package com.rafael;

import java.util.ArrayList;
import java.util.List;

public class StoryPageService {
    private static final int CHARS_PER_PAGE = 2000;

    public static List<String> paginate(String text) {
        List<String> pages = new ArrayList<>();
        String[] words = text.split("\\s+"); // Splits by any whitespace

        StringBuilder currentPage = new StringBuilder();

        for (String word : words) {
            // Check if adding this word + a space exceeds the limit
            if (currentPage.length() + word.length() + 1 > CHARS_PER_PAGE) {
                pages.add(currentPage.toString()); // Save the current page
                currentPage = new StringBuilder(); // Reset
            }

            // Add the word and a space
            currentPage.append(word).append(" ");
        }

        // Don't forget the last partial page!
        if (currentPage.length() > 0) {
            pages.add(currentPage.toString());
        }

        return pages;
    }
}