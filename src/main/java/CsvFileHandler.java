import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class CsvFileHandler {

    static Map<String, Double> records = new HashMap<>();
    final static String headers = "Date,Price";
    final static String PRICE = "Price";
    final static String FILE_NOT_FOUND = "file not found!";

    public static List<Double> readDataPointsFromCsv() {
        String line;
        String splitBy = ",";
        List<Double> dataPoints = new ArrayList<>();
        try {
            File file = new CsvFileHandler().getFileFromResource("Outliers.csv");

            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(splitBy);
                if(!data[1].equals(PRICE)){ //ignore headers
                    records.put(data[0], Double.valueOf(data[1]));
                    dataPoints.add(Double.valueOf(data[1]));
                }
            }
        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return dataPoints;
    }


    public static void writeDataPointsToCsv(Map<String, Double> recordsWithoutOutliers){
        String eol = System.getProperty("line.separator");
        recordsWithoutOutliers.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue())
                .forEachOrdered(x -> records.put(x.getKey(), x.getValue()));

        try (Writer writer = new FileWriter("output\\result.csv")) {
            writer.append(headers).append(eol);
            for (Map.Entry<String, Double> entry : records.entrySet()) {
                writer.append(entry.getKey())
                        .append(',')
                        .append(entry.getValue().toString())
                        .append(eol);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException(FILE_NOT_FOUND + fileName);
        } else {
            return new File(resource.toURI());
        }
    }

}
