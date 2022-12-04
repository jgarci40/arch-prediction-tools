package gmu.edu.sdalab.util;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Ehsan on 10/20/14.
 */
public class CreateRSFFromPackages {
    private static Map<String, List<String>> clusterToFileMap;

    public static String main(String myPath, String prefixStr) throws FileNotFoundException{
        File startingDirectory= new File(myPath);
        clusterToFileMap = new HashMap<>();
        List<File> fileList = FileListing.getFileListing(startingDirectory, ".java");
        //String prefixStr = "/org/apache/pdfbox/";
        for(File file : fileList ){
            String tempStr = file.toString();
            if (tempStr.contains(prefixStr)) {
                tempStr = tempStr.substring(tempStr.indexOf(prefixStr) + 1, tempStr.indexOf(".java"));
                tempStr = tempStr.replace('/', '.');

                String tempPackage = "";
                if (tempStr.indexOf('.', prefixStr.length() - 1) > -1)
                    tempPackage = tempStr.substring(0, tempStr.indexOf('.', prefixStr.length() - 1));
                else
                    tempPackage = tempStr.substring(0, prefixStr.length() - 2);

                if (clusterToFileMap.containsKey(tempPackage)) {
                    ArrayList tempList = new ArrayList(clusterToFileMap.get(tempPackage));
                    tempList.add(tempStr);
                    clusterToFileMap.put(tempPackage, tempList);
                } else
                    clusterToFileMap.put(tempPackage, new ArrayList<String>(Arrays.asList(tempStr)));

                System.out.println(tempStr + "                            " + tempPackage);
            }
        }

        String[] splittedPath = myPath.split("\\/");
        String outPutFilePath = "./Output/RSFFiles/";
        String fileName = splittedPath[splittedPath.length-1] + "_pkgs.rsf";
        printToFile(outPutFilePath, fileName);
        return outPutFilePath + fileName;


    }

    private static void printToFile(String myPath, String fileName) {
        BufferedWriter out = null;
        try {
            File folderPath = new File(myPath);
            System.out.println(folderPath.getAbsolutePath());
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
            Iterator it = clusterToFileMap.keySet().iterator();
            while (it.hasNext()){
                String clusterName = (String) it.next();
                ArrayList<String> tempList = new ArrayList(clusterToFileMap.get(clusterName));

                //out.write(clusterName + " " + myMetric.IMC + " " + myMetric.CMC + " " + myMetric.NCF + " " + myMetric.bugCount + " " + myMetric.LOC + " " + myMetric.BCO + " " + myMetric.SPF + " " + myMetric.BDC + " " + myMetric.BUO);
                Iterator it2 = tempList.iterator();
                while(it2.hasNext()) {
                    out.write("contain " + clusterName + " " + it2.next());
                    out.newLine();
                }

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
