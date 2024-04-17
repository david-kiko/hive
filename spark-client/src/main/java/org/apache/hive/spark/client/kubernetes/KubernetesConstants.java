package org.apache.hive.spark.client.kubernetes;

public class KubernetesConstants {
  public static final String CREATED_BY_ANNOTATION = "created-by";

  public static final String SESSION_ID = "sessionId";

  public static final String SPARK_APP_ID_LABEL = "spark-app-selector";
  public static final String SPARK_APP_TAG_LABEL = "spark-app-tag";
  public static final String SPARK_ROLE_LABEL = "spark-role";
  public static final String SPARK_EXEC_ID_LABEL = "spark-exec-id";

  public static final String SPARK_ROLE_DRIVER = "driver";
  public static final String SPARK_ROLE_EXECUTOR = "executor";

  public static final String SPARK_POD_LABELS_PREFIX = "spark.kubernetes.pod";
  public static final String SPARK_DRIVER_LABELS_PREFIX = "spark.kubernetes.driver.label";
  public static final String SPARK_EXECUTOR_LABELS_PREFIX = "spark.kubernetes.executor.label";

  public static final String SPARK_MIN_MEMORY_OVERHEAD = "384m";
}
