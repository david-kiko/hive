package org.apache.hive.spark.client.kubernetes;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparkKubernetesClient {

  private static final Logger LOG = LoggerFactory.getLogger(SparkKubernetesClient.class);

  private static KubernetesClient getClient(Map<String, String> sparkConf) throws KubernetesClientException {
    String namespace = sparkConf.get("spark.kubernetes.namespace");
    String masterUrl = sparkConf.get("spark.master");

    Config config = new ConfigBuilder()
        .withMasterUrl(masterUrl)
        .build();

    return new KubernetesClient(new DefaultKubernetesClient(config), namespace);
  }

  /**
   * Get the application id by the session id on kubernetes.
   *
   * @param sparkConf
   * @param sessionId
   * @return
   */
  public static String getApplicationIdBySessionId(Map<String, String> sparkConf, String sessionId) {
    Map<String, String> labels = new HashMap<>();
    labels.put(KubernetesConstants.SESSION_ID, sessionId);
    labels.put(KubernetesConstants.SPARK_ROLE_LABEL, KubernetesConstants.SPARK_ROLE_DRIVER);

    List<KubernetesApplication> applications;
    try (KubernetesClient client = getClient(sparkConf)) {
      applications = client.getApplications(labels);
    }
    if (CollectionUtils.isEmpty(applications)) {
      LOG.warn("No application found with the session id: " + sessionId);
      return null;
    }
    if (applications.size() > 1) {
      LOG.warn("Found more than one application with the same session id: " + sessionId);
    }
    return applications.get(0).getApplicationId();
  }

  public static boolean cleanupDanglingPodBySessionId(Map<String, String> sparkConf, String sessionId) {
    Map<String, String> labels = new HashMap<>();
    labels.put(KubernetesConstants.SESSION_ID, sessionId);
    labels.put(KubernetesConstants.SPARK_ROLE_LABEL, KubernetesConstants.SPARK_ROLE_DRIVER);

    boolean result = true;
    try (KubernetesClient client = getClient(sparkConf)) {
      List<KubernetesApplication> applications = client.getApplications(labels);
      for (KubernetesApplication app : applications) {
        result &= client.killApplication(app);
      }
    }
    return result;
  }
}
