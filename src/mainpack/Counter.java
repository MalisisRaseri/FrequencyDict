package mainpack;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.UnicodeBlock.CYRILLIC;
import static java.lang.Character.UnicodeBlock.of;

public class Counter {

        private static final String REPORT = "report-by-alph.txt";
        private static final String INVERTED_REPORT = "report-by-alph-rev.txt";
        private static final String FREQUENCY_REPORT = "report-by-freq.txt";
        private static Map<String, Integer> DICTIONARY = new HashMap<>();


        public void processFile(String file) throws IOException {
            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String s;
                while(( s = reader.readLine() ) != null) {
                    s = s.toLowerCase();
                    int p = 0;
                    while(p < s.length()) {

                        while(p < s.length() && of(s.charAt(p)) != CYRILLIC)
                            p++;

                        int st = p;

                        while(p < s.length() && (of(s.charAt(p)) == CYRILLIC || s.charAt(p) == '-'))
                            p++;

                        if(st < p) {
                            String w = s.substring(st, p);

                            if(DICTIONARY.containsKey(w))
                                DICTIONARY.put(w, DICTIONARY.get(w) + 1);
                            else
                                DICTIONARY.put(w, 1);

                        }
                    }
                }
            }
        }

        public void saveReports() throws FileNotFoundException {
            Map.Entry<String, Integer>[] data = DICTIONARY.entrySet().toArray(new Map.Entry[0]);
            int wordsTotal = 0;
            for (Map.Entry<String, Integer> e : data) {
                wordsTotal += e.getValue();
            }

            Arrays.sort(data, Map.Entry.comparingByKey());
            saveReport(REPORT, data, wordsTotal);

            Arrays.sort(data,
                    (e1, e2) -> compareStringsByBackDict(e1.getKey(), e2.getKey()));
            saveReport(INVERTED_REPORT, data, wordsTotal);

            Arrays.sort(data,
                    (e1, e2) -> {
                        int r = -e1.getValue().compareTo(e2.getValue());
                        if(r != 0)
                            return r;
                        return e1.getKey().compareTo(e2.getKey());
                    });
            saveReport(FREQUENCY_REPORT, data, wordsTotal);
        }

        private void saveReport(String fileName, Map.Entry<String, Integer>[] data, int wordsTotal) throws FileNotFoundException {
            try(PrintWriter pw = new PrintWriter(fileName)) {
                for (Map.Entry<String, Integer> e : data) {
                    pw.println(e.getKey() + ", " + e.getValue() + ", "
                            + ((float)e.getValue() / wordsTotal)*100 + "%");
                }
            }
        }

        private static int compareStringsByBackDict(String s1, String s2) {
            int p1 = s1.length() - 1,
                    p2 = s2.length() - 1;
            while(p1 >= 0 && p2 >= 0) {
                int r = Character.compare(s1.charAt(p1), s2.charAt(p2));
                if(r != 0)
                    return r;
                p1--;
                p2--;
            }
            return p1 - p2;
        }
    }


