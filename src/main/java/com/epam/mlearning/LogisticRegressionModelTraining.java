package com.epam.mlearning;

import com.epam.mlearning.model.LogRow;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Tuple2;

import java.util.Map;

public class LogisticRegressionModelTraining {

    public static final String NONE = "none";

    public static void main(String[] args) {
        String pathToTrainingData = args[0];
        String pathToKeywordData = args[1];
        String modelSavePath = args[2];

        SparkConf sparkConf = new SparkConf().setAppName("Model training");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<String> logsFromFileRDD = sc.textFile(pathToTrainingData);
        JavaRDD<String> keywords = sc.textFile(pathToKeywordData);
        Broadcast<Map<String, String>> br = sc.broadcast(keywords.mapToPair((PairFunction<String, String, String>) x -> {String[] splits = x.split("\\t");
           return  new Tuple2<>(splits[0], splits[1]);}).collectAsMap());

        JavaRDD<LabeledPoint> labeledPoints = logsFromFileRDD
                .map(line -> {
                    String[] row = line.split("\\t");
                    return new LogRow(
                            row[2], //ipinyou
                            row[3],//useragent
                            Double.parseDouble(row[5]),//region
                            Double.parseDouble(row[6]),//city
                            row[8],//domain
                            Double.parseDouble(row[12]), //ad params
                            Double.parseDouble(row[13]),
                            Double.parseDouble(row[14]),
                            Double.parseDouble(row[15]),
                            br.value().getOrDefault(row[20], NONE),//tags
                            Double.parseDouble(row[21]));})//stream
                .filter(row -> (row.getStreamId() == 1 || row.getStreamId() == 11)
                        && !row.getIPinYouId().equals("null"))
                .map(row -> {
                    UserAgent ua = UserAgent.parseUserAgentString(row.getUserAgent());
                    return new LabeledPoint(row.getStreamId() == 1 ? 0: 1,
                            Vectors.dense(
                            ua.getOperatingSystem().getName().hashCode(),
                            ua.getOperatingSystem().getDeviceType().hashCode(),
                            ua.getBrowser().getName().hashCode(),
                            row.getRegion(),
                            row.getCity(),
                            row.getDomain().hashCode(),
                            row.getAdSlotWidth(),
                            row.getAdSlotHeight(),
                            row.getAdSlotVisibility(),
                            row.getAdSlotFormat(),
                            row.getUserTags().hashCode()));});

        /*JavaRDD<LabeledPoint>[] splits =labeledPoints.randomSplit(new double[]{0.6, 0.4});
        JavaRDD<LabeledPoint> training = splits[0];
        JavaRDD<LabeledPoint> test = splits[1];*/

        JavaRDD<LabeledPoint> training = labeledPoints;

        LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
                .setNumClasses(2)
                .run(training.rdd());
        model.setThreshold(0.2);
        model.save(sc.sc(), modelSavePath);

       /* JavaRDD<Tuple2<Object, Object>> treePredictionAndLabels = test.map (x -> {
            Double prediction = model.predict(x.features());
            return new Tuple2<>(prediction, x.label());
        });

        MulticlassMetrics treeMetrics = new MulticlassMetrics(treePredictionAndLabels.rdd());
        System.out.println("Precision = " + treeMetrics.precision());

        BinaryClassificationMetrics baiesBcMetrics = new BinaryClassificationMetrics(treePredictionAndLabels.rdd());
        System.out.println("Area under ROC = " + baiesBcMetrics.areaUnderROC());

        System.out.println(treePredictionAndLabels.filter(x -> (double)x._1 > 0).count());*/
    }

}
