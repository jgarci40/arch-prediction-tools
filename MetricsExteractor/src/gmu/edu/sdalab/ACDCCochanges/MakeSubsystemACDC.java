package gmu.edu.sdalab.ACDCCochanges;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ehsan
 * Date: 8/18/13
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MakeSubsystemACDC {
    private static Map<String, List<String>> myHashtable;

    public static List<List<String>> makeClusterList(String fileName) {
        myHashtable = new HashMap<String, List<String>>();
        File testFile = new File(fileName);
        String content = getContents(testFile);
        String[] rows = content.split("\n");
        //System.out.println(rows.length);
        for (String s : rows) {
            String[] oneRow = s.split("\\s+");
            //System.out.println(oneRow[2]);
            oneRow[2] = oneRow[2].replaceAll("(\\r|\\n)", "");
            if (myHashtable.containsKey(oneRow[1]))
            {
                List<String> tempList = new ArrayList<String>(myHashtable.get(oneRow[1]));
                tempList.add(oneRow[2]);
                myHashtable.put(oneRow[1], tempList);
            }
            else
            {
               List<String> tempList = new ArrayList<String>();
               tempList.add(oneRow[2]);
               myHashtable.put(oneRow[1], tempList);
            }

        }
        List<List<String>> results = new ArrayList<List<String>>(myHashtable.values());
        //System.out.println(results.size());
        return results;

    }

    public static String getContents(File aFile) {
        //...checks on aFile are elided
        StringBuilder contents = new StringBuilder();

        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try {
                String line = null; //not declared within while loop
                /*
                * readLine is a bit quirky :
                * it returns the content of a line MINUS the newline.
                * it returns null only for the END of the stream.
                * it returns an empty String if two newlines appear in a row.
                */
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }
}

