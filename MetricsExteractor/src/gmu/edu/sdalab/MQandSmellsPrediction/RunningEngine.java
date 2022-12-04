package gmu.edu.sdalab.MQandSmellsPrediction;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Ehsan on 4/5/15.
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
        String smellsNextReleaseFileLocation = args[8];
        String clustersMetricNextReleaseFileLocation = args[9];
        String clustersMappingFileLocation = args[10];
        if (new File(subsystems).isDirectory()) {
            subsystems = gmu.edu.sdalab.util.CreateRSFFromPackages.main(subsystems, prefixStr);
        }


        String changeHistoryFileName  = gmu.edu.sdalab.ACDCCochanges.RepositoryACDCFile.main(startDateStr, subsystems, url, prefixStr, patternStr);
        //System.out.print(changeHistoryFileName);
        String fileDataPath = gmu.edu.sdalab.AddLOC.AddMetrics.main(changeHistoryFileName, understandOutputFileLocation, subsystems);
        String systemDataPath = gmu.edu.sdalab.MQandSmellsPrediction.makeSubsytemData.main(fileDataPath, subsystems,smellsFileLocation, clustersMetricFileLocation, smellsNextReleaseFileLocation, clustersMetricNextReleaseFileLocation, clustersMappingFileLocation);
        gmu.edu.sdalab.util.ConcatLOCFiles.main(systemDataPath);
    }
}
