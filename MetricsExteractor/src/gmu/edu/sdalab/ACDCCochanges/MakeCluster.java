package gmu.edu.sdalab.ACDCCochanges;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ehsan
 * Date: 4/18/12
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MakeCluster {
    public static List<List<String>> makeClusterList(String fileName) {
        File testFile = new File(fileName);
        //System.out.println("Original file contents: " + getContents(testFile));
        String content = getContents(testFile);
        String[] clusters = content.split("\n");
        List<List<String>> clusterList = new ArrayList<List<String>>();
        int i = 0;
        //System.out.println("clusters.length:" + clusters.length);
        for (String s : clusters) {
            String[] cluster = s.split(", ");
            clusterList.add(new ArrayList<String>());
            for (String ss : cluster)
                clusterList.get(i).add(ss.replaceAll("(\\r|\\n)", ""));
            i++;
        }
        for (int j = 0; j < clusterList.size(); j++) {
            String temp = clusterList.get(j).get(0).substring(clusterList.get(j).get(0).indexOf("=") + 2);
            clusterList.get(j).remove(0);
            clusterList.get(j).add(0, temp);
        }

        return clusterList;

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
