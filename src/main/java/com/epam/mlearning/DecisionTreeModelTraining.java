package com.epam.mlearning;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.hive.HiveContext;

import java.util.HashMap;
import java.util.Map;

public class DecisionTreeModelTraining {
    private static final Integer numClasses = 2;
    private static final String impurity = "gini";
    private static final Integer maxDepth = 5;
    private static final Integer maxBins = 32;

    public static void main(String[] args) throws InterruptedException {
        String modelSavePath = args[0];
        SparkSession spark = SparkSession
                .builder()
                .appName("ML")
                .enableHiveSupport()
                .getOrCreate();

        HiveContext hiveContext = new HiveContext(spark.sparkContext());
        Dataset<Row> denormalized = hiveContext.sql("select * from logs3").cache();

        //Категоризация
        Map<String, Long> osMap = denormalized.map((MapFunction<Row, String>) row -> row.getString(row.fieldIndex("os")), Encoders.STRING())
                .distinct().toJavaRDD().zipWithUniqueId().collectAsMap();

        Map<String, Long> deviceMap = denormalized.map((MapFunction<Row, String>) row -> row.getString(row.fieldIndex("device")), Encoders.STRING())
                .distinct().toJavaRDD().zipWithUniqueId().collectAsMap();

        Map<String, Long> browserMap = denormalized.map((MapFunction<Row, String>) row -> row.getString(row.fieldIndex("browser")), Encoders.STRING())
                .distinct().toJavaRDD().zipWithUniqueId().collectAsMap();

        Map<String, Long> keywordMap = denormalized.map((MapFunction<Row, String>) row -> row.getString(row.fieldIndex("keyword")), Encoders.STRING())
                .distinct().toJavaRDD().zipWithUniqueId().collectAsMap();

        JavaRDD<LabeledPoint> vector = denormalized.toJavaRDD()
                .map((Function<Row, LabeledPoint>) row -> {
                    double click = row.getInt(row.fieldIndex("stream_id")) == 11 ? 1.0 : 0.0;
                    return new LabeledPoint(click,
                            Vectors.dense(osMap.get(row.getString(row.fieldIndex("os"))).doubleValue(),
                                    deviceMap.get(row.getString(row.fieldIndex("device"))).doubleValue(),
                                    keywordMap.get(row.getString(row.fieldIndex("keyword"))).doubleValue(),
                                    browserMap.get(row.getString(row.fieldIndex("browser"))).doubleValue(),
                                    (double)row.getInt(row.fieldIndex("city")),
                                    (double)row.getInt(row.fieldIndex("ad_slot_width")),
                                    (double)row.getInt(row.fieldIndex("ad_slot_height")),
                                    (double)row.getInt(row.fieldIndex("ad_slot_visibility")),
                                    (double)row.getInt(row.fieldIndex("ad_slot_format")),
                                    (double) row.getInt(row.fieldIndex("city"))));
                });


        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        categoricalFeaturesInfo.put(0, osMap.size());
        categoricalFeaturesInfo.put(1, deviceMap.size());
        categoricalFeaturesInfo.put(2, keywordMap.size());
        categoricalFeaturesInfo.put(3, browserMap.size());


        DecisionTreeModel model = DecisionTree.trainClassifier(vector, numClasses,
                categoricalFeaturesInfo, impurity, maxDepth, maxBins);

        model.save(spark.sparkContext(), modelSavePath);
    }
}
