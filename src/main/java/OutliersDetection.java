import java.util.*;

public class OutliersDetection {

    private static List<Double> outliers;
    private final static String NOT_FOUND = "No outliers found.";
    private final static String FOUND = "Outliers found ";
    private final static String MESSAGE = "\nCheck output folder >> result.csv to see processed datapoints with outliers removed.";

    public static void detectOutliers() {
        List<Double> dataPoints = CsvFileHandler.readDataPointsFromCsv();
        List<Double> outliers = getOutliers(dataPoints);
        printOutliers(outliers);
        Map<String, Double> recordsWithoutOutliers = removeOutliers(CsvFileHandler.records, outliers);
        CsvFileHandler.writeDataPointsToCsv(recordsWithoutOutliers);
    }

    private static Map<String, Double> removeOutliers(Map<String, Double> dataPoints, List<Double> outliers) {
        dataPoints.entrySet().removeIf(entry -> (outliers.contains(entry.getValue())));
        return dataPoints;
    }

    public static List<Double> getOutliers( List<Double> input) {
        Collections.sort(input);
        List<Double> output = new ArrayList<>();
        List<Double> data1 = new ArrayList<>();
        List<Double> data2 = new ArrayList<>();
        if (input.size() % 2 == 0) {
            data1 = input.subList(0, input.size() / 2);
            data2 = input.subList(input.size() / 2, input.size());
        } else {
            data1 = input.subList(0, input.size() / 2);
            data2 = input.subList(input.size() / 2 + 1, input.size());
        }
        double q1 = getMedian(data1);
        double q3 = getMedian(data2);
        double iqr = q3 - q1;
        double lowerFence = q1 - 1.5 * iqr;
        double upperFence = q3 + 1.5 * iqr;
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i) < lowerFence || input.get(i) > upperFence)
                output.add(input.get(i));
        }
        return output;
    }

    private static double getMedian(List<Double> data) {
        if (data.size() % 2 == 0)
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        else
            return data.get(data.size() / 2);
    }

    private static void printOutliers(List<Double> data) {
        if (data.isEmpty())
            System.out.println(NOT_FOUND);
        else
            System.out.println(FOUND + data + MESSAGE);
    }

}



