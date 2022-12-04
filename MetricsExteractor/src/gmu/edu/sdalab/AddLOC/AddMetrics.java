package gmu.edu.sdalab.AddLOC;

import gmu.edu.sdalab.util.FileListing;
import gmu.edu.sdalab.util.FileToClusterMapping;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ehsan
 * Date: 2/10/14
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddMetrics {
    private static class FileMetrics{
        String fileName;
        int IMC;
        int CMC;
        int NCF;
        int bugCount;
        int numberOfCommits;
        int LOC;
        int CountClassCoupled;
        int MaxInheritanceTree;
        int PercentLackOfCohesion;
        int SumCyclomatic;


        FileMetrics(String f, int I, int C, int N, int b, int _numberOfCommits){
            fileName = f;
            IMC = I;
            CMC = C;
            NCF = N;
            bugCount = b;
            numberOfCommits = _numberOfCommits;
            //LOC = l;
        }
    }
    private static String date;

    private static Map<String, List<Integer>> changeHistoryMetricsList;
    private static List<FileMetrics> outputList;
    private static Map<String, List<Integer>> understandMetricsLIst;
    private static Map<String, String> fileToclusterMap;
    private static Map<String, Integer> clustersBugCount;

    //private static String changeHistoryFileLocation;
    //private static String understandOutputFileLocation;
    public static String main(String _changeHistoryFileLocation, String _understandOutputFileLocation, String _clustersFileLocation){
//        String changeHistoryFileLocation = args[0];
//        String understandOutputFileLocation = args[1];
//        String arcadeFileLocation = args[2];
//        prefixStr = args[3];

        String changeHistoryFileLocation = _changeHistoryFileLocation;
        String understandOutputFileLocation = _understandOutputFileLocation;
        String clustersFileLocation = _clustersFileLocation;

        fileToclusterMap = FileToClusterMapping.makeClusters(clustersFileLocation);



        understandMetricsLIst = new HashMap<String, List<Integer>>();
        understandMetricsLIst = makeUndrestandMetricsList(understandOutputFileLocation);


        makeChangeHistoryMetricsList(changeHistoryFileLocation);


        String[] splittedPath = changeHistoryFileLocation.split("\\/");
        String outPutFilePath = "./Output/FileData/";
        String fileName = splittedPath[splittedPath.length-1];

        makeOutputList();
        printToFile(outPutFilePath, fileName);
        printClusterBugFile(outPutFilePath, fileName);

        return outPutFilePath + fileName;
    }

    private static void makeOutputList(){
        clustersBugCount = new HashMap<>();
        boolean flag;
        outputList = new ArrayList<FileMetrics>();
        Iterator<String> it = changeHistoryMetricsList.keySet().iterator();
        while (it.hasNext()){
            flag = true;
            String fileName = it.next();
            ArrayList<Integer> tempList = new ArrayList(changeHistoryMetricsList.get(fileName));
            FileMetrics myMetric = new FileMetrics(fileName, tempList.get(0), tempList.get(1), tempList.get(2), tempList.get(3), tempList.get(4));
            if (understandMetricsLIst.containsKey(fileName))
            {
                myMetric.CountClassCoupled = understandMetricsLIst.get(fileName).get(0);
                myMetric.LOC = understandMetricsLIst.get(fileName).get(1);
                myMetric.MaxInheritanceTree = understandMetricsLIst.get(fileName).get(2);
                myMetric.PercentLackOfCohesion = understandMetricsLIst.get(fileName).get(3);
                myMetric.SumCyclomatic = understandMetricsLIst.get(fileName).get(4);


                String clusterName = fileToclusterMap.get(fileName);
                if (clustersBugCount.containsKey(clusterName)){
                    int bugCount = clustersBugCount.get(clusterName);
                    clustersBugCount.put(clusterName, bugCount + tempList.get(3));
                }
                else
                    clustersBugCount.put(clusterName, tempList.get(3));

            }
            else
                flag = false;


            if (flag)
                outputList.add(myMetric);


        }

    }

    private static void printClusterBugFile(String myPath, String fileName) {
        BufferedWriter out = null;
        try {
            File folderPath = new File(myPath);

            if (!folderPath.exists()) {
                if (folderPath.mkdirs()) {
                    System.out.println("Directory is created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }

            myPath = myPath + "ClustersBugs-" + fileName;

            FileWriter outFile = new FileWriter(myPath);
            //PrintWriter out = new PrintWriter(outFile);
            out = new BufferedWriter(outFile);
            Iterator<String> it = clustersBugCount.keySet().iterator();
            while (it.hasNext()){
                String clusterName = it.next();
                String clusterNameAndVersion = fileName.substring(0, fileName.length() - 4) + "-" + clusterName;
                out.write(clusterNameAndVersion + " " +  clustersBugCount.get(clusterName));
                out.newLine();

            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }




    private static void printToFile(String myPath, String fileName) {
        BufferedWriter out = null;
        try {
            File folderPath = new File(myPath);

            if (!folderPath.exists()) {
                if (folderPath.mkdirs()) {
                    System.out.println("Directory is created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }

            myPath = myPath + fileName;

            FileWriter outFile = new FileWriter(myPath);
            //PrintWriter out = new PrintWriter(outFile);
            out = new BufferedWriter(outFile);
            Iterator<FileMetrics> it = outputList.iterator();
            while (it.hasNext()){
                FileMetrics myMetric = it.next();
                String clusterName = fileName.substring(0, fileName.length() - 4) + "-" + fileToclusterMap.get(myMetric.fileName);
                out.write(myMetric.fileName + " " + myMetric.IMC + " " + myMetric.CMC + " " + myMetric.NCF + " " + myMetric.bugCount + " " + myMetric.numberOfCommits + " " + myMetric.LOC + " " + myMetric.CountClassCoupled + " " + myMetric.MaxInheritanceTree + " " + myMetric.PercentLackOfCohesion + " " + myMetric.SumCyclomatic + " " + clusterName + " " + date);
                out.newLine();

            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void makeChangeHistoryMetricsList(String fileName){
        File testFile = new File(fileName);
        String content = FileListing.getContents(testFile);
        String[] rows = content.split("\n");
        //System.out.println(clusters[1]);
        String[] dateRow = rows[0].split("\\s+");
        date = dateRow[0].substring(8, dateRow[0].indexOf('-'));

        changeHistoryMetricsList = new HashMap<String, List<Integer>>();
        for (int i = 1; i < rows.length; i++) {
            String[] row = rows[i].split("\\s+");
            String temp = row[0].replaceAll("(\\r|\\n)", "");
            ArrayList<Integer> tempList = new ArrayList<>();
            tempList.add(Integer.parseInt(row[1]));
            tempList.add(Integer.parseInt(row[2]));
            tempList.add(Integer.parseInt(row[3]));
            tempList.add(Integer.parseInt(row[4]));
            tempList.add(Integer.parseInt(row[5]));
            changeHistoryMetricsList.put(temp, tempList);
        }

    }

    public static Map<String, List<Integer>> makeUndrestandMetricsList(String fileName) {
        File testFile = new File(fileName);
        String content = FileListing.getContents(testFile);
        String[] rows = content.split("\n");
        Hashtable<String, List<Integer>> filesList = new Hashtable<String, List<Integer>>();
        for (String row : rows) {
            String[] eachRow = row.split(",");
            if (eachRow[0].contains("Class"))
            {
                String temp = eachRow[1].replaceAll("(\\r|\\n|\")", "");
                ArrayList<Integer> tempList = new ArrayList<>();
                tempList.add(Integer.parseInt(eachRow[2]));
                tempList.add(Integer.parseInt(eachRow[3]));
                tempList.add(Integer.parseInt(eachRow[4]));
                tempList.add(Integer.parseInt(eachRow[5]));
                tempList.add(Integer.parseInt(eachRow[6]));
                filesList.put(temp, tempList);
            }

        }

        return filesList;

    }


}
