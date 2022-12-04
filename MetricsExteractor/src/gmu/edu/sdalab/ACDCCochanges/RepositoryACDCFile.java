package gmu.edu.sdalab.ACDCCochanges;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Ehsan
 * Date: 8/18/13
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryACDCFile {

    private static ISVNAuthenticationManager authManager;
    private static SVNURL svnUrl;
    private static List<CommitModule> bugEntryList;
    private static List<CommitModule> allEntryListPackage;
    private static List<CommitModule> allEntryListFile;
    private static List<CommitModule> allEntryListPackageBug;
    private static List<CommitModule> entryListNotZero;
    private static List<List<String>> clusterList;
    private static Set<String> allChangedFiles;
    private static Set<String> allFilesHaveBugs;
    private static Map<String, Integer> singleChangedFilesCount;
    private static Map<String, Integer> allFilesBugCount;
    private static Map<String, Integer> allPackagesBugCountMultiple;
    private static Map<String, Integer> coChangedSamePackageCount;
    private static Map<String, Integer> coChangedSamePackageCountNew;
    private static Map<String, Integer> coChangedSamePackageCountMultiple;
    private static Map<String, Integer> coChangedDifferentPackageCount;
    private static Map<String, Integer> coChangedDifferentPackageCountNew;
    private static Map<String, Integer> coChangedDifferentPackageCountMultiple;
    private static Map<String, Integer> numberOfCommits;
    private static Map<String, Integer> numCoChangedSubsystems;
    private static Map<String, Set<Integer>> numCoChangedSubsystemsUnique;
    private static int totalNumChangedFiles;
    private static int totalNumChangedFilesG0L30;
    //    private static int totalNumChangedFilesNotZero;
//    private static int totalNumChangedFilesG0L30NotZero;
    private static int totalNumSamePackage;
    private static int totalNumCommits;
    private static int totalNumSingleChangedFiles;



    private static String startDateStr;
    private static String endDateStr;
    private static String endDateStrBug;
    private static String subsystemFileLocStr;
    private static String prefixStr;
    private static String patternStr;




    //private static final Date startDate;
    public RepositoryACDCFile() {
        totalNumChangedFiles = 0;
        totalNumChangedFilesG0L30 = 0;
        //totalNumSameCluster = 0;
        totalNumCommits = 0;

    }

    public static String main(String _startDateStr, String _ACDCFileLocStr, String _url, String _prefixStr, String _patternStr) {

//        startDateStr = args[0] ;
//        //endDateStr = args[1] ;
//        //endDateStrBug = args[2] ;
//        ACDCFileLocStr = args[1] ;
//        String url = args[2];
//        prefixStr = args[3];
//        patternStr = args[4];
        startDateStr = _startDateStr;
        //endDateStr = args[1] ;
        //endDateStrBug = args[2] ;
        subsystemFileLocStr = _ACDCFileLocStr;
        String url = _url;
        prefixStr = _prefixStr;
        patternStr = _patternStr;
        //String fileName = args[5];

        String[] splittedPath = subsystemFileLocStr.split("\\/");
        String outPutFilePath = "./Output/ChangeHistoryData/";
        String fileName = splittedPath[splittedPath.length-3] + "-" + splittedPath[splittedPath.length-1].substring(0, splittedPath[splittedPath.length-1].length() - 4) + ".txt";
        //outPutFilePath += splittedPath[splittedPath.length-4] + "/" + splittedPath[splittedPath.length-3]  + "/" + splittedPath[splittedPath.length-2]  + "/";
        //entryList = new ArrayList<CommitModule>();
        allEntryListPackage = new ArrayList<CommitModule>();
        allEntryListFile = new ArrayList<CommitModule>();

        if (subsystemFileLocStr.endsWith(".rsf") | subsystemFileLocStr.endsWith(".prn"))
            clusterList = MakeSubsystemACDC.makeClusterList(subsystemFileLocStr);
        else
            if (subsystemFileLocStr.endsWith(".bunch"))
                clusterList = MakeCluster.makeClusterList(subsystemFileLocStr);


        System.out.println("Cluster size:" + clusterList.size());


/*        entryListNotZero = new ArrayList<CommitModule>();
        clusterList = MakeCluster.makeClusterList(ACDCFileLocStr);*/
        //startDate = new Date()
        DAVRepositoryFactory.setup();
        //String name = "guest";

        String name = "anonymous";
        String password = "anonymous";
        //String name = "ekourosh@gmu.edu";
        //String password = "ehsank";


        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
            authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
            repository.setAuthenticationManager(authManager);
            svnUrl = SVNURL.parseURIEncoded(url);
            System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
            System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
            SVNNodeKind nodeKind = repository.checkPath("", -1);
            if (nodeKind == SVNNodeKind.NONE) {
                System.err.println("There is no entry at '" + url + "'.");
                System.exit(1);
            } else if (nodeKind == SVNNodeKind.FILE) {
                System.err.println("The entry at '" + url + "' is a file while a directory was expected.");
                System.exit(1);
            }

            //System.out.println(clusterList);
//            printClusters(clusterList, "C:\\Users\\Ehsan\\Desktop\\ClusterList.txt");
            listEntries(repository);
            parse(allEntryListFile);
            listBugRevisionEntries(repository);
            parseBug(allEntryListPackageBug);
            //System.out.println(">>>>>>>>" + outPutFilePath);
            printToFile(outPutFilePath, fileName);
            //print(allEntryListFile);
            //printHashSet(numCoChangedSubsystemsUnique);
/*            listBugRevisionEntries(repository);

            parseBug(bugEntryList);
            printToFile("C:\\Users\\Ehsan\\Desktop\\CoChanged.txt");*/

/*            System.out.println("totalNumChangedFiles: " + totalNumChangedFiles);
            System.out.println("totalNumChangedFilesG0L30: " + totalNumChangedFilesG0L30);

            System.out.println("numberOfCommits: " + totalNumCommits);

            System.out.println("totalNumSamePackage: " + totalNumSamePackage);
            System.out.println("totalNumSingleChangedFiles: " + totalNumSingleChangedFiles);
            System.out.println("totalNumSingleChangedFiles + totalNumSamePackage: " + new Integer(totalNumSingleChangedFiles + totalNumSamePackage));*/

            //parseBug(allEntryListPackageBug);

/*            print(allEntryListPackage);
            parse(allEntryListPackage);
            print();
            printToFile("C:\\Users\\Ehsan\\Desktop\\CoChanged.txt");*/
        } catch (SVNException svne) {
            //handl e exception
            System.out.println(svne.getMessage());
        }
        return outPutFilePath + fileName;
    }

    public static void listEntries(SVNRepository repository) throws SVNException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = null;
        Date endDate = null;
        Calendar myCalendar = Calendar.getInstance();
        try {

            endDate = formatter.parse(startDateStr);
            myCalendar.setTime(formatter.parse(startDateStr));
            myCalendar.add(Calendar.DATE, -91);  // number of days to add
            startDate = formatter.parse(formatter.format(myCalendar.getTime()));

//            startDate = formatter.parse(startDateStr);
//            myCalendar.setTime(formatter.parse(startDateStr));
//            myCalendar.add(Calendar.DATE, 91);  // number of days to add
//            endDate = formatter.parse(formatter.format(myCalendar.getTime()));

            System.out.println(startDate + "  " + endDate);
            //endDate = formatter.parse(endDateStr);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        numberOfCommits = new HashMap<String, Integer>();

        Collection logEntries = null;
        logEntries = repository.log(new String[]{""}, null, 0, -1, true, false);
        for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();


            if (true) {
                //if (!(m.matches() & m1.matches())) {

                CommitModule myCommitModule = new CommitModule(logEntry.getAuthor(), logEntry.getRevision(), logEntry.getDate(), logEntry.getMessage());
                CommitModule myCommitModulePackage = new CommitModule(logEntry.getAuthor(), logEntry.getRevision(), logEntry.getDate(), logEntry.getMessage());
                List<String> tempList = new ArrayList<String>();
                Set<String> tempSet = new HashSet<>();
                List<String> tempPackageList = new ArrayList<String>();
                //List<String> tempListNotZero = new ArrayList<String>();
                //System.out.println(logEntry.getDate());
                if (logEntry.getDate().compareTo(startDate) >= 0 && logEntry.getDate().compareTo(endDate) < 0) {
                    String s = formatter.format(logEntry.getDate());
                    //System.out.println(s);
                }

                if (logEntry.getChangedPaths().size() > 0 && logEntry.getDate().compareTo(startDate) >= 0 && logEntry.getDate().compareTo(endDate) <= 0) {
                    Set changedPathsSet = logEntry.getChangedPaths().keySet();

                    for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                        SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                        //System.out.println(entryPath.getPath());
                        //if (entryPath.getType() == 'M' & entryPath.getPath().endsWith(".java")) {
                        if (entryPath.getPath().endsWith(".java")) {
                            String tempStr = entryPath.getPath();
                            if (tempStr.contains(prefixStr) && !tempStr.toLowerCase().contains("test")) {
                                //if (tempStr.contains(prefixStr)) {
                                tempStr = tempStr.substring(tempStr.indexOf(prefixStr) + 1, tempStr.indexOf(".java"));
                                tempStr = tempStr.replace('/', '.');
                                //System.out.println(tempStr);
                                String tempPackage="";
                                if (tempStr.indexOf('.', prefixStr.length() - 1) > -1)
                                    tempPackage = tempStr.substring(0, tempStr.indexOf('.', prefixStr.length() - 1));
                                else
                                    tempPackage = prefixStr.replace('/', '.');
                                //System.out.println(tempStr);
                                tempPackageList.add(tempPackage);
                                //tempPackageList.add(tempStr);

                                //System.out.println(tempStr);
                                if (clusterSearch(tempStr) != 0) {
                                    tempSet.add(tempStr);
//                                    {
//                                        tempList.add(tempStr);
//                                        //System.out.println(tempStr + "1111");
//                                        incrementHash(numberOfCommits, tempStr);
//                                    }
                                }
                                //else
                                    //System.out.println(tempStr);
                                totalNumChangedFiles++;
/*                                if (clusterSearch(tempStr) != 0) {
                                    tempListNotZero.add(tempStr);
                                    totalNumChangedFilesNotZero++;
                                }*/
                            }
                        }
                    }

                    for (String s: tempSet) {
                        tempList.add(s);
                        incrementHash(numberOfCommits, s);
                    }

                    //System.out.println("-----------------------------------------");
                    if (tempPackageList.size() > 0 && tempPackageList.size() < 30) {
                        totalNumCommits++;
                        myCommitModulePackage.setModifiedFilesList(tempPackageList);
                        allEntryListPackage.add(myCommitModulePackage);
                    }
                    if (tempList.size() == 1)
                        totalNumSingleChangedFiles++;

                    if (tempList.size() > 1 && tempList.size() < 30) {
                        totalNumChangedFilesG0L30 += tempList.size();
                        myCommitModule.setModifiedFilesList(tempList);
                        allEntryListFile.add(myCommitModule);

                    }

                }
            }
        }

    }

    public static void listBugRevisionEntries(SVNRepository repository) throws SVNException {
        bugEntryList = new ArrayList<CommitModule>();
        allEntryListPackageBug = new ArrayList<CommitModule>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = null;
        Date endDate = null;
        String message;
        Calendar myCalendar = Calendar.getInstance();


//dt = formatter.format(myCalendar.getTime());  // dt is now the new date
        try {
            startDate = formatter.parse(startDateStr);
            myCalendar.setTime(formatter.parse(startDateStr));
            myCalendar.add(Calendar.DATE, 91);  // number of days to add
            endDate = formatter.parse(formatter.format(myCalendar.getTime()));

//            myCalendar.setTime(formatter.parse(startDateStr));
//            myCalendar.add(Calendar.DATE, 91);  // number of days to add
//            startDate = formatter.parse(formatter.format(myCalendar.getTime()));
//            myCalendar.add(Calendar.DATE, 91);  // number of days to add
//            endDate = formatter.parse(formatter.format(myCalendar.getTime()));
            //endDate = formatter.parse(endDateStrBug);
            System.out.println(startDate + " Bugs  " + endDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        Collection logEntries = null;
        logEntries = repository.log(new String[]{""}, null, 0, -1, true, false);
        for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();

            message = logEntry.getMessage();
/*            message = message.replaceAll("[\\n]"," ");
            Pattern p = Pattern.compile(".*([0-9]{3,}|\\bbug[s]?\\b|patch[es]?|bugfix|NullPointerException|\\bNPE\\b|\\bexception[s]?\\b).*", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(message);*/
            //Pattern p = Pattern.compile(".*(\\bXERCESJ\\b|\\bjira\\b|\\bbug[s]?\\b|patch[es]?|bugfix|NullPointerException|\\bNPE\\b|\\bexception[s]?\\b).*", Pattern.CASE_INSENSITIVE);
            Pattern p = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            Pattern p1 = Pattern.compile(".*([0-9]{1,}).*");
            Matcher m = p.matcher(message);
            Matcher m1 = p1.matcher(message);

            if ((m.matches() & m1.matches())) {
                CommitModule myCommitModule = new CommitModule(logEntry.getAuthor(), logEntry.getRevision(), logEntry.getDate(), logEntry.getMessage());
                CommitModule myCommitModulePackage = new CommitModule(logEntry.getAuthor(), logEntry.getRevision(), logEntry.getDate(), logEntry.getMessage());
                List<String> tempList = new ArrayList<String>();
                List<String> tempPackageList = new ArrayList<String>();
                //List<String> tempListNotZero = new ArrayList<String>();
                if (logEntry.getDate().compareTo(startDate) >= 0 && logEntry.getDate().compareTo(endDate) < 0) {
                    String s = formatter.format(logEntry.getDate());
                    //System.out.println(s);
                }

                if (logEntry.getChangedPaths().size() > 0 && logEntry.getDate().compareTo(startDate) >= 0 && logEntry.getDate().compareTo(endDate) <= 0) {

                    Set changedPathsSet = logEntry.getChangedPaths().keySet();

                    for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                        SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                        //System.out.println(entryPath.getPath());
                        //if (entryPath.getType() == 'M' & entryPath.getPath().endsWith(".java")) {
                        if (entryPath.getPath().endsWith(".java")) {
                            String tempStr = entryPath.getPath();
                            if (tempStr.contains(prefixStr)) {
                                tempStr = tempStr.substring(tempStr.indexOf(prefixStr) + 1, tempStr.indexOf(".java"));
                                tempStr = tempStr.replace('/', '.');
                                String tempPackage;
                                if (tempStr.indexOf('.', prefixStr.length() - 1) > -1)
                                    tempPackage = tempStr.substring(0, tempStr.indexOf('.', prefixStr.length() - 1));
                                else
                                    tempPackage = prefixStr.replace('/', '.');
                                //System.out.println(tempPackage);
                                tempPackageList.add(tempPackage);
                                //tempPackageList.add(tempStr);
                                //System.out.println(tempStr);

                                tempList.add(tempStr);
                                totalNumChangedFiles++;
/*                                if (clusterSearch(tempStr) != 0) {
                                    tempListNotZero.add(tempStr);
                                    totalNumChangedFilesNotZero++;
                                }*/
                            }
                        }
                    }

                    //System.out.println("-----------------------------------------");
                    if (tempList.size() > 0 && tempList.size() < 30)
                    {
                        myCommitModulePackage.setModifiedFilesList(tempList);
                        allEntryListPackageBug.add(myCommitModulePackage);
                        //System.out.println(allEntryListPackageBug.size());
                        totalNumCommits++;
                    }
                    if (tempList.size() == 1)
                        totalNumSingleChangedFiles++;


                    if (tempList.size() > 0 && tempList.size() < 30) {
                        totalNumChangedFilesG0L30 += tempList.size();
                        myCommitModule.setModifiedFilesList(tempList);
                        //System.out.print(tempList);
                        bugEntryList.add(myCommitModule);

                    }

                }
            }
        }
        //print(bugEntryList);

/*        parseBug(allEntryListPackageBug);
        print(allEntryListPackageBug);
        printBugToFile("C:\\Users\\Ehsan\\Desktop\\PackageBugs.txt");*/

    }




    public static int clusterSearch(String fileName) {
        Iterator it = clusterList.iterator();
        int clusterNum = 1;
        while (it.hasNext()) {
            List<String> myList = (List<String>) it.next();
            for (int i = 0; i < myList.size(); i++)
                if (myList.get(i).equalsIgnoreCase(fileName))
                    return clusterNum;
            clusterNum++;
        }
        return 0;
    }


    public static void parse(List<CommitModule> myList) {
        allChangedFiles = new HashSet<String>();
        singleChangedFilesCount = new HashMap<String, Integer>();
        coChangedSamePackageCount = new HashMap<String, Integer>();
        coChangedDifferentPackageCount = new HashMap<String, Integer>();
        coChangedSamePackageCountMultiple = new HashMap<String, Integer>();
        coChangedDifferentPackageCountMultiple = new HashMap<String, Integer>();
        numCoChangedSubsystems = new HashMap<String, Integer>();
        numCoChangedSubsystemsUnique = new HashMap<String, Set<Integer>>();
        coChangedSamePackageCountNew = new HashMap<>();
        Iterator it = myList.iterator();
        while (it.hasNext()) {
            CommitModule myCommitModule = (CommitModule) it.next();
            System.out.println(myCommitModule.getModifiedFilesList().size());
            if (myCommitModule.getModifiedFilesList().size() == 1) {
                String temp = myCommitModule.getModifiedFilesList().get(0);
                //System.out.println(temp);
                allChangedFiles.add(temp);
                incrementHash(singleChangedFilesCount, temp);
            } else {

                HashSet tempSet = new HashSet<Integer>();
                List<Integer> commitList = new ArrayList<>();
                for (int i = 0; i < myCommitModule.getModifiedFilesList().size(); i++) {
                    String tempStr = myCommitModule.getModifiedFilesList().get(i);
/*                    String tempPackage="";
                    if (tempStr.indexOf('.', prefixStr.length() - 1) > -1)
                        tempPackage = tempStr.substring(0, tempStr.indexOf('.', prefixStr.length() - 1));
                    else
                        tempPackage = prefixStr.replace('/', '.');*/
                    int clusterNum = clusterSearch(myCommitModule.getModifiedFilesList().get(i));
                    tempSet.add(clusterNum);
                    commitList.add(clusterNum);
/*                    if (clusterNum != 0)
                        tempSet.add(clusterNum);
                    else
                        System.out.println(myCommitModule.getModifiedFilesList().get(i));*/

                    allChangedFiles.add(myCommitModule.getModifiedFilesList().get(i));
                    //tempSet.add(tempPackage);
                    System.out.println(myCommitModule.getModifiedFilesList().get(i));
                }
                System.out.println(commitList);
                if (tempSet.size() == 1)
                    for (int i = 0; i < myCommitModule.getModifiedFilesList().size(); i++){
                        incrementHash(coChangedSamePackageCount, myCommitModule.getModifiedFilesList().get(i));
                        incrementHash(coChangedSamePackageCountNew, myCommitModule.getModifiedFilesList().get(i));
                    }
                else {
                    for (int i = 0; i < myCommitModule.getModifiedFilesList().size(); i++) {
                        incrementHash(coChangedDifferentPackageCount, myCommitModule.getModifiedFilesList().get(i));

                    }
                    for (int i = 0; i < commitList.size(); i++) {
                        int currentCluster = commitList.get(i);
                        boolean flag = false;
                        if (currentCluster > 0) {
                            for (int j = i + 1; j < commitList.size(); j++) {
                                if (commitList.get(j) == currentCluster) {
                                    incrementHash(coChangedSamePackageCountNew, myCommitModule.getModifiedFilesList().get(j));
                                    commitList.set(j, -1);
                                    flag = true;
                                }
                            }
                        }
                        if (flag) {
                            incrementHash(coChangedSamePackageCountNew, myCommitModule.getModifiedFilesList().get(i));
                        }

                    }
/*                    Iterator it2 = tempSet.iterator();
                    while (it2.hasNext()) {
                        String temp = (String) it2.next();
                        incrementHash(coChangedDifferentPackageCount, temp);
                    }*/

                }
                if (tempSet.size() == 1)
                    for (int i = 0; i < myCommitModule.getModifiedFilesList().size(); i++) {
                        for (int j = 0; j < myCommitModule.getModifiedFilesList().size() - 1; j++)
                            incrementHash(coChangedSamePackageCountMultiple, myCommitModule.getModifiedFilesList().get(i));
                    }
                else
                {
                    for (int i = 0; i < myCommitModule.getModifiedFilesList().size(); i++) {
                        for (int j = 0; j < myCommitModule.getModifiedFilesList().size() - 1; j++)
                            incrementHash(coChangedDifferentPackageCountMultiple, myCommitModule.getModifiedFilesList().get(i));

                    }

/*                    for (int i = 0; i < myCommitModule.getModifiedFilesList().size(); i++) {
                        for (int j = 1; j < tempSet.size(); j++)
                            incrementHash(numCoChangedSubsystems, myCommitModule.getModifiedFilesList().get(i));
                        addToHashSet(numCoChangedSubsystemsUnique, tempSet, myCommitModule.getModifiedFilesList().get(i));
                    }*/

                }
            }
            //System.out.println("--------------------------");
        }
        //System.out.println(coChangedDifferentPackageCount.get("org.apache.openjpa.persistence"));
    }

    public static void parseBug(List<CommitModule> myList) {
        allFilesHaveBugs = new HashSet<String>();
        allFilesBugCount = new HashMap<String, Integer>();
        //allPackagesBugCountMultiple = new HashMap<String, Integer>();
        Iterator it = myList.iterator();
        while (it.hasNext()) {
            CommitModule myCommitModule = (CommitModule) it.next();
            //HashSet tempSet = new HashSet<String>();
            for (int i = 0; i < myCommitModule.getModifiedFilesList().size(); i++) {
                allFilesHaveBugs.add(myCommitModule.getModifiedFilesList().get(i));
                // tempSet.add(myCommitModule.getModifiedFilesList().get(i));
                //System.out.println(myCommitModule.getModifiedFilesList().get(i));
                incrementHash(allFilesBugCount, myCommitModule.getModifiedFilesList().get(i));
            }
/*            Iterator it2 = tempSet.iterator();
            while (it2.hasNext()) {
                String temp = (String) it2.next();
                //System.out.println(temp);
                incrementHash(allFilesBugCount, temp);
            }*/
            //System.out.println("--------------------------");
        }
        //System.out.println(coChangedDifferentPackageCount.get("org.apache.openjpa.persistence"));
    }




    public static void incrementHash(Map<String, Integer> myHashtable, String temp) {
        if (myHashtable.containsKey(temp))
            myHashtable.put(temp, myHashtable.get(temp) + 1);
        else
            myHashtable.put(temp, 1);

    }



    private static void print(List<CommitModule> myList) {
        Iterator it = myList.iterator();
        while (it.hasNext()) {
            CommitModule myCommitModule = (CommitModule) it.next();
            for (int i = 0; i < myCommitModule.getModifiedFilesList().size(); i++)
                System.out.println(myCommitModule.getModifiedFilesList().get(i) + " " + clusterSearch(myCommitModule.getModifiedFilesList().get(i)));
            System.out.println("--------------------------");
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
            Iterator it = allChangedFiles.iterator();
            //out.write("Package("+ startDateStr +"-" + endDateStr + ") singleChanged coChangedSamePackage coChangedDifferentPackage coChangedSamePackageMult coChangedDifferentPackageMult BugCount BugCountMult numCoChangedSubsystems numCoChangedSubsystemsUnique\n");
            out.write("Package("+ startDateStr +"-" + endDateStr + ") coChangedSamePackage coChangedDifferentPackage NumCochangedFiles BugCountMult numberOfCommits\n");
            while (it.hasNext()) {
                String packageName = (String) it.next();
                out.write(packageName);
                /*  if (singleChangedFilesCount.containsKey(packageName))
            out.write("  " + singleChangedFilesCount.get(packageName));
        else
            out.write("  " + "0");*/
                if (coChangedSamePackageCountNew.containsKey(packageName))
                    out.write("  " + coChangedSamePackageCountNew.get(packageName));
                else
                    out.write("  " + "0");
                if (coChangedDifferentPackageCount.containsKey(packageName))
                    out.write("  " + coChangedDifferentPackageCount.get(packageName));
                else
                    out.write("  " + "0");
                int count = 0;
                if (coChangedSamePackageCountMultiple.containsKey(packageName))
                    count += coChangedSamePackageCountMultiple.get(packageName);
                if (coChangedDifferentPackageCountMultiple.containsKey(packageName))
                    count += coChangedDifferentPackageCountMultiple.get(packageName);
                out.write("  " + count);

                /*  if (coChangedSamePackageCountMultiple.containsKey(packageName))
            out.write("  " + coChangedSamePackageCountMultiple.get(packageName));
        else
            out.write("  " + "0");
        if (coChangedDifferentPackageCountMultiple.containsKey(packageName))
            out.write("  " + coChangedDifferentPackageCountMultiple.get(packageName));
        else
            out.write("  " + "0");
        if (allFilesBugCount.containsKey(packageName))
            out.write("  " + "1");
        else
            out.write("  " + "0");*/
                if (allFilesBugCount.containsKey(packageName))
                    out.write("  " + allFilesBugCount.get(packageName));
                else
                    out.write("  " + "0");

                if (numberOfCommits.containsKey(packageName))
                    out.write("  " + numberOfCommits.get(packageName));
                else
                    out.write("  " + "0");
                /* if (numCoChangedSubsystems.containsKey(packageName))
            out.write("  " + numCoChangedSubsystems.get(packageName));
        else
            out.write("  " + "0");
        if (numCoChangedSubsystemsUnique.containsKey(packageName))
            out.write("  " + (numCoChangedSubsystemsUnique.get(packageName).size() - 1));
        else
            out.write("  " + "0");*/
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

