package gmu.edu.sdalab.util;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Ehsan on 11/4/14.
 */
public class FileToClusterMapping {
    public static Map<String, String> makeClusters(String fileName){
        if (fileName.endsWith(".rsf") | fileName.endsWith(".prn"))
            return makeClustersList(fileName);
        else
        if (fileName.endsWith(".bunch"))
            return makeClustersListBunch(fileName);
        return null;
    }

    public static Map<String, String> makeClustersListBunch(String fileName){
        File testFile = new File(fileName);
        String content = FileListing.getContents(testFile);
        String[] rows = content.split("\n");
        Map<String, String> fileToclusterMap = new HashMap<String, String>();

        for (int i = 0; i < rows.length; i++) {
            String[] cluster = rows[i].split(", ");
            String[] firstRow = cluster[0].split(" = ");
            String clusterName = firstRow[0].replaceAll("(\\r|\\n)", "");
            String fileN = firstRow[1].replaceAll("(\\r|\\n)", "");
            if (fileToclusterMap.containsKey(fileN))
                throw  new IllegalStateException("File in multiple cluster: " + fileN);
            else
                fileToclusterMap.put(fileN, clusterName);
            for (int j = 1; j < cluster.length; j++){
                fileN = cluster[j].replaceAll("(\\r|\\n)", "");
                if (fileToclusterMap.containsKey(fileN))
                    throw  new IllegalStateException("File in multiple cluster: " + fileN);
                else
                    fileToclusterMap.put(fileN, clusterName);

            }

        }
        return fileToclusterMap;

    }

    public static Map<String, String> makeClustersList(String fileName){
        File testFile = new File(fileName);
        String content = FileListing.getContents(testFile);
        String[] rows = content.split("\n");
        Map<String, String> fileToclusterMap = new HashMap<String, String>();
        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split("\\s+");
            String clusterName = row[1].replaceAll("(\\r|\\n)", "");
            String fileN = row[2].replaceAll("(\\r|\\n)", "");
            if (!fileToclusterMap.containsKey(fileN))
                //throw  new IllegalStateException("File in multiple cluster: " + fileN);
            //else
                fileToclusterMap.put(fileN, clusterName);


        }
        return fileToclusterMap;
    }

}
