import jdk.internal.jline.internal.TestAccessible;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutliersDetectionTest {
    
    @Test
    public void test_readDataPointsFromCsv(){
        List<Double> dataPoints =  CsvFileHandler.readDataPointsFromCsv();
        List<Double> outliers = OutliersDetection.getOutliers(dataPoints);
        OutliersDetection.detectOutliers();
        int no_of_dataPoints_without_outliers = dataPoints.size() - outliers.size();
        Assert.assertTrue(no_of_dataPoints_without_outliers==readOutputCsvFile().size());
    }

    private List<Double> readOutputCsvFile(){
        String line = "";
        String splitBy = ",";
        List<Double> dataPoints_without_outliers = new ArrayList<>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("output\\result.csv"));
            while ((line = br.readLine()) != null)
            {
                String[] records = line.split(splitBy);
                if(!records[1].equals("Price"))
                    dataPoints_without_outliers.add(Double.valueOf(records[1]));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return dataPoints_without_outliers;
    }
}
