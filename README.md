mvn clean package

/usr/hdp/current/spark2-client/bin/spark-submit 
\--class com.epam.mlearning.LogisticRegressionModelTraining 
\<path-to-jar> <path-to-training-data> <path-to-tags-data>
\<model-save-path>