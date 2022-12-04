package gmu.edu.sdalab.run;

import java.io.File;
import java.io.FileNotFoundException;
import gmu.edu.sdalab.util.*;
import gmu.edu.sdalab.ACDCCochanges.*;

/**
 * Created by Ehsan on 10/18/14.
 */
public class RunningEngine {
    public static void main (String args[]) throws FileNotFoundException {

        String startDateStr = args[0] ;
        String subsystems = args[1] ;
        String url = args[2];
        String prefixStr = args[3];
        String patternStr = args[4];
        String understandOutputFileLocation = args[5];
        String smellsFileLocation = args[6];
        String clustersMetricFileLocation = args[7];
        if (new File(subsystems).isDirectory()) {
            subsystems = CreateRSFFromPackages.main(subsystems, prefixStr);
        }


        String changeHistoryFileName  = RepositoryACDCFile.main(startDateStr, subsystems, url, prefixStr, patternStr);
        //System.out.print(changeHistoryFileName);
        String fileDataPath = gmu.edu.sdalab.AddLOC.AddMetrics.main(changeHistoryFileName, understandOutputFileLocation, subsystems);
        //String fileDataPath = gmu.edu.sdalab.AddLOC.AddMetrics2.main(changeHistoryFileName, understandOutputFileLocation, arcadeSmellsFileLocation, prefixStr, clustersMetricFileLocation);
        String systemDataPath = gmu.edu.sdalab.ConvertFileDataToSubsystemData.Convert.main(fileDataPath, subsystems,smellsFileLocation, clustersMetricFileLocation);
        gmu.edu.sdalab.util.ConcatLOCFiles.main(systemDataPath);
    }
}
