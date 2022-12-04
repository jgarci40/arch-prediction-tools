package gmu.edu.sdalab.util;

import java.io.*;

/**
 * Created by Ehsan on 9/1/14.
 */
public class ConcatLOCFiles {
    //private static String myPath = "/Users/Ehsan/Workspace/IdeaProjects/ACDCCoChanges/out/artifacts/ACDCCoChanges_jar/Output";
    //private static String myPath = "/Users/Ehsan/Workspace/IdeaProjects/PackageCoChanges/out/artifacts/PackageCoChanges_jar/Output";
    //private static String myPath = "/Users/Ehsan/Workspace/IdeaProjects/SVNkit/out/artifacts/SVNkit_jar/Output";
    //private static String myPath = "/Users/Ehsan/Workspace/IdeaProjects/BunchCoChanges/out/artifacts/BunchCoChanges_jar/Output";
    //private static String myPath = "/Users/Ehsan/Workspace/IdeaProjects/AddLOC/out/artifacts/AddLOC_jar/Output";
    //private static String myPath = "/Users/Ehsan/Workspace/IdeaProjects/ConvertFileDataToSubsystemData/out/artifacts/ConvertFileDataToSubsystemData_jar/Output";
    private static String resultFileName = "ResultsMetrics.txt";

    private static String myPath;
    public static void main(String _myPath){
        myPath = _myPath;
        concatSolutionInFolder(myPath);

    }

    private static void concatSolutionInFolder(String path) {
        File folder = new File(path);
        File[] combinationFolders = folder.listFiles();
        String temp = "";
        for (File f: combinationFolders)
        {
            if (f.getName().equals(resultFileName))
                f.delete();
            else
            if (f.getName().endsWith("txt")) {
                //System.out.println(f);
                temp += getContents(f);
            }
        }
        printToFile(myPath, temp);

        //System.out.println(temp);

    }

    public static String getContents(File aFile) {
        //...checks on aFile are elided
        StringBuilder contents = new StringBuilder();
        //boolean firstLine = true;

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
                    //firstLine = false;

            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }

    private static void printToFile(String myPath, String output) {
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

            myPath = myPath + "/" + resultFileName;

            FileWriter outFile = new FileWriter(myPath);
            //PrintWriter out = new PrintWriter(outFile);
            out = new BufferedWriter(outFile);
            //System.out.println(myPath);
            out.write(output);

            out.newLine();


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
