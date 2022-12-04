package gmu.edu.sdalab.ConvertFileDataToSubsystemData;

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
public class Convert {
    private static class FileMetrics{
        String name;
        int IMC;
        int CMC;
        int NCF;
        int bugCount;
        int numberOfCommits;
        int LOC;
        int BCO;
        int SPF;
        int BDC;
        int BUO;
        int CountClassCoupled;
        int MaxInheritanceTree;
        int PercentLackOfCohesion;
        int SumCyclomatic;
        int incomingDep;
        int outgoingDep;
        int internalEdges;
        int externalEdges;
        int edgesInto;
        int edgesOutOf;

        FileMetrics(String f, int I, int C, int N, int b, int _numberOfCommits, int _LOC ,int _CoCountClassCoupled, int _MaxInheritanceTree, int _PercentLackOfCohesion, int _SumCyclomatic){
            name = f;
            IMC = I;
            CMC = C;
            NCF = N;
            bugCount = b;
            numberOfCommits = _numberOfCommits;
            LOC = _LOC;
            CountClassCoupled = _CoCountClassCoupled;
            MaxInheritanceTree = _MaxInheritanceTree;
            PercentLackOfCohesion = _PercentLackOfCohesion;
            SumCyclomatic = _SumCyclomatic;
        }

        @Override
        public String toString(){
            return name + " " + IMC + " " + CMC + " " + NCF + " " + bugCount + " " + LOC + " " + CountClassCoupled + " " + MaxInheritanceTree + " " + PercentLackOfCohesion + " " + SumCyclomatic;
        }
    }

    private static Map<String, FileMetrics> subsystemMetricsList;
    private static Map<String, FileMetrics> metricsList;
    private static Map<String, String> fileToclusterMap;
    private static Map<String, List<Integer>> clustersMetrics;//The list of dependencies between clusters
    private static Map<String, List<Integer>> smellsList;


    public static String main(String _metricsFileLocation, String _clustersFileLocation, String _clustersSmellFileLocation, String _clustersMetricsFileLocation){
//        String metricsFileLocation = args[0];
//        String clustersFileLocation = args[1];

        String metricsFileLocation = _metricsFileLocation;
        String clustersFileLocation = _clustersFileLocation;
        String clustersMetricsFileLocation = _clustersMetricsFileLocation;
        String clustersSmellFileLocation = _clustersSmellFileLocation;

        fileToclusterMap = FileToClusterMapping.makeClusters(clustersFileLocation);


        makeMetricsList(metricsFileLocation);
        makeClustersMetricsList(clustersMetricsFileLocation);
        makeClustersSmellsList(clustersSmellFileLocation);




        String[] splittedPath = metricsFileLocation.split("\\/");
        String outPutFilePath = "./Output/SubsystemData/";
        String fileName = splittedPath[splittedPath.length-1];

        convertToSubsystemData();
        addClusterMetrics();
        addSmellMetrics();
        //System.out.println(subsystemMetricsList);
        printToFile(outPutFilePath, fileName);

        return outPutFilePath;
    }

    private static void convertToSubsystemData(){
        subsystemMetricsList = new HashMap<>();
        Iterator it = metricsList.keySet().iterator();
        while (it.hasNext()){
            String fileName = (String) it.next();
            FileMetrics myMetric = metricsList.get(fileName);
            //FileMetrics myMetric = new FileMetrics(fileName, tempList.get(0), tempList.get(1), tempList.get(2), tempList.get(3));
            String clusterName = fileToclusterMap.get(fileName);
            if (subsystemMetricsList.containsKey(clusterName))
            {
                FileMetrics tempMetric = subsystemMetricsList.get(clusterName);
                tempMetric.IMC += myMetric.IMC;
                tempMetric.CMC += myMetric.CMC;
                tempMetric.NCF += myMetric.NCF;
                tempMetric.bugCount += myMetric.bugCount;
                tempMetric.LOC += myMetric.LOC;
                tempMetric.numberOfCommits += myMetric.numberOfCommits;
                tempMetric.CountClassCoupled += myMetric.CountClassCoupled;
                tempMetric.MaxInheritanceTree += myMetric.MaxInheritanceTree;
                tempMetric.PercentLackOfCohesion =+ myMetric.PercentLackOfCohesion;
                tempMetric.SumCyclomatic =+ myMetric.SumCyclomatic;
//                if (tempMetric.BCO > 0 | myMetric.BCO > 0)
//                    tempMetric.BCO = 1;
//                if (tempMetric.SPF > 0 | myMetric.SPF > 0)
//                    tempMetric.SPF = 1;
//                if (tempMetric.BDC > 0 | myMetric.BDC > 0)
//                    tempMetric.BDC = 1;
//                if (tempMetric.BUO > 0 | myMetric.BUO > 0)
//                    tempMetric.BUO = 1;


                subsystemMetricsList.put(clusterName, tempMetric);

            }
            else
            {
                myMetric.name = clusterName;
                subsystemMetricsList.put(clusterName, myMetric);
            }

        }


    }

    public static void addSmellMetrics(){
        Iterator<String> it = subsystemMetricsList.keySet().iterator();
        while (it.hasNext()) {
            String clusterName = it.next();
            FileMetrics tempMetric = subsystemMetricsList.get(clusterName);
            if (smellsList.containsKey(clusterName)){
                tempMetric.BCO = smellsList.get(clusterName).get(0);
                tempMetric.SPF = smellsList.get(clusterName).get(1);
                tempMetric.BDC = smellsList.get(clusterName).get(2);
                tempMetric.BUO = smellsList.get(clusterName).get(3);
            }
            else{
                tempMetric.BCO = 0;
                tempMetric.SPF = 0;
                tempMetric.BDC = 0;
                tempMetric.BUO = 0;
            }
            subsystemMetricsList.put(clusterName, tempMetric);
        }

    }

    public static void addClusterMetrics(){

        Iterator<String> it = subsystemMetricsList.keySet().iterator();
        while (it.hasNext()){
            String clusterName = it.next();
            FileMetrics tempMetric = subsystemMetricsList.get(clusterName);

            if (clustersMetrics.containsKey(clusterName))
            {
                tempMetric.incomingDep = clustersMetrics.get(clusterName).get(0);
                tempMetric.outgoingDep = clustersMetrics.get(clusterName).get(1);
                tempMetric.internalEdges = clustersMetrics.get(clusterName).get(2);
                tempMetric.externalEdges = clustersMetrics.get(clusterName).get(3);
                tempMetric.edgesInto = clustersMetrics.get(clusterName).get(4);
                tempMetric.edgesOutOf = clustersMetrics.get(clusterName).get(5);
            }
            else
            {
                tempMetric.incomingDep = 0;
                tempMetric.outgoingDep = 0;
                tempMetric.internalEdges = 0;
                tempMetric.externalEdges = 0;
                tempMetric.edgesInto = 0;
                tempMetric.edgesOutOf = 0;
            }

            subsystemMetricsList.put(clusterName, tempMetric);
        }

    }






    public static void makeClustersSmellsList(String fileName){

        File testFile = new File(fileName);
        String content = FileListing.getContents(testFile);
        String[] rows = content.split("\n");
        ArrayList<Integer> tempList;
        smellsList = new HashMap<String, List<Integer>>();
        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split("\\s+");
            String temp = row[0].replaceAll("(\\r|\\n)", "");
            int index = 0;
            switch (row[1].toLowerCase()){
                case "bco": index = 0;
                    break;
                case "spf": index = 1;
                    break;
                case "bdc": index = 2;
                    break;
                case "buo": index = 3;
                    break;
            }
            if (smellsList.containsKey(temp)){
                tempList = new ArrayList(smellsList.get(temp));
                tempList.set(index, tempList.get(index) + 1);
            }
            else {
                tempList = new ArrayList<>();
                for (int j = 0; j < 4; j++)
                    tempList.add(0);
                tempList.set(index, 1);
            }
            smellsList.put(temp, tempList);

        }

    }

    public static void makeClustersMetricsList(String fileName){
        File testFile = new File(fileName);
        String content = FileListing.getContents(testFile);
        String[] rows = content.split("\n");
        //System.out.println(clusters[1]);
        clustersMetrics = new HashMap<String, List<Integer>>();
        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split("\\s+");
            String temp = row[0].replaceAll("(\\r|\\n)", "");
            ArrayList<Integer> tempList = new ArrayList<>();
            tempList.add(Integer.parseInt(row[1]));
            tempList.add(Integer.parseInt(row[2]));
            tempList.add(Integer.parseInt(row[3]));
            tempList.add(Integer.parseInt(row[4]));
            tempList.add(Integer.parseInt(row[5]));
            tempList.add(Integer.parseInt(row[6]));
            clustersMetrics.put(temp, tempList);
        }

    }


    public static void makeMetricsList(String fileName){
        File testFile = new File(fileName);
        String content = FileListing.getContents(testFile);
        String[] rows = content.split("\n");
        //System.out.println(clusters[1]);
        metricsList = new HashMap<String, FileMetrics>();

        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split("\\s+");
            String temp = row[0].replaceAll("(\\r|\\n)", "");
            metricsList.put(temp, new FileMetrics(temp, Integer.parseInt(row[1]), Integer.parseInt(row[2]), Integer.parseInt(row[3]), Integer.parseInt(row[4]), Integer.parseInt(row[5]), Integer.parseInt(row[6]), Integer.parseInt(row[7]), Integer.parseInt(row[8]), Integer.parseInt(row[9]), Integer.parseInt(row[10])));
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
            Iterator it = subsystemMetricsList.keySet().iterator();
            while (it.hasNext()){
                String clusterName = (String) it.next();
                FileMetrics myMetric = subsystemMetricsList.get(clusterName);
                //out.write(clusterName + " " + myMetric.IMC + " " + myMetric.CMC + " " + myMetric.NCF + " " + myMetric.bugCount + " " + myMetric.LOC + " " + myMetric.BCO + " " + myMetric.SPF + " " + myMetric.BDC + " " + myMetric.BUO);
                out.write(clusterName+ " " + myMetric.IMC + " " + myMetric.CMC + " " + myMetric.NCF + " " + myMetric.bugCount + " " + myMetric.numberOfCommits + " " + myMetric.LOC + " " + myMetric.BCO + " " + myMetric.SPF + " " + myMetric.BDC + " "
                        + myMetric.BUO + " " + myMetric.CountClassCoupled + " " + myMetric.MaxInheritanceTree + " " + myMetric.PercentLackOfCohesion + " " + myMetric.SumCyclomatic + " " + myMetric.incomingDep +
                        " " + myMetric.outgoingDep + " " + myMetric.internalEdges + " " + myMetric.externalEdges + " " + myMetric.edgesInto + " " + myMetric.edgesOutOf);
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




}
