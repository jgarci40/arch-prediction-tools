package gmu.edu.sdalab.util;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ehsan on 10/20/14.
 */
public final class FileListing {
    /**
     * Demonstrate use.
     *
     * @param aArgs - <tt>aArgs[0]</tt> is the full name of an existing
     * directory that can be read.
     */
    public static void main(String... aArgs) throws FileNotFoundException {
        //File startingDirectory= new File(aArgs[0]);
        File startingDirectory= new File("/Users/Ehsan/Workspace/ArcadeProjects/PDFBox/pdfbox-1.1.0");

        List<File> files = FileListing.getFileListing(startingDirectory, ".java");

//        //print out all file names, in the the order of File.compareTo()
//        for(File file : files ){
//            System.out.println(file);
//        }
    }

    /**
     * Recursively walk a directory tree and return a List of all
     * Files found; the List is sorted using File.compareTo().
     *
     * @param aStartingDir is a valid directory, which can be read.
     */
    static public List<File> getFileListing(
            File aStartingDir
    ) throws FileNotFoundException {
        validateDirectory(aStartingDir);
        List<File> result = getFileListingNoSort(aStartingDir,null);
        Collections.sort(result);
        return result;
    }

    /**
     * Recursively walk a directory tree and return a List of files matching the FilenameFilter; the List is sorted using File.compareTo().
     *
     * @param aStartingDir is a valid directory, which can be read.
     */
    static public List<File> getFileListing(
            File aStartingDir, String extension
    ) throws FileNotFoundException {
        validateDirectory(aStartingDir);
        List<File> result = getFileListingNoSort(aStartingDir,extension);
        Collections.sort(result);
        return result;
    }

    // PRIVATE //
    static private List<File> getFileListingNoSort(
            File aStartingDir, String extension
    ) throws FileNotFoundException {
        List<File> result = new ArrayList<File>();
        File[] filesAndDirs = aStartingDir.listFiles();
        List<File> filesDirs = Arrays.asList(filesAndDirs);
        for(File file : filesDirs) {
            if (extension == null) {
                try {
                    if (Files.isSymbolicLink(file.toPath())) { // if the file is a symbolic link
                        if (file.getCanonicalFile().exists()) { // check if the file exists
                            result.add(file); //always add, even if directory
                        }
                        else {
                            // don't add it
                        }
                    }
                    else {
                        result.add(file); //always add if not symbolic, even if directory
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
            else if (file.getName().endsWith(extension)) {
                result.add(file);
            }
            if ( file.isDirectory() ) {
                //must be a directory
                //recursive call!
                List<File> deeperList = getFileListingNoSort(file,extension);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    /**
     * Directory is valid if it exists, does not represent a file, and can be read.
     */
    static private void validateDirectory (
            File aDirectory
    ) throws FileNotFoundException {
        if (aDirectory == null) {
            throw new IllegalArgumentException("Directory should not be null.");
        }
        if (!aDirectory.exists()) {
            throw new FileNotFoundException("Directory does not exist: " + aDirectory);
        }
        if (!aDirectory.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: " + aDirectory);
        }
        if (!aDirectory.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
        }
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
