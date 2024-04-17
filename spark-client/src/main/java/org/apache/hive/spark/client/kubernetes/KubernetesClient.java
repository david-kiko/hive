package org.apache.hive.spark.client.kubernetes;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KubernetesClient implements Closeable {
  private static final Logger LOG = LoggerFactory.getLogger(KubernetesClient.class);

  private DefaultKubernetesClient client;
  private String namespaces;

  KubernetesClient(DefaultKubernetesClient client , String namespaces) {
    this.client = client;
    this.namespaces = namespaces;
  }

  public List<KubernetesApplication> getApplications(Map<String, String> labels) {
    return this.client.pods()
        .inNamespace(this.namespaces)
        .withLabels(labels)
        .list()
        .getItems()
        .stream()
        .map(KubernetesApplication::new)
        .collect(Collectors.toList());
  }

  @Override
  public void close(){
    this.client.close();
  }

  public List<KubernetesApplication> getAppByTag(String name, String value) {
    HashMap<String, String> labels = new HashMap<>();
    labels.put(name, value);
    return getApplications(labels);
  }

  public List<KubernetesApplication> getAppByTags(Map<String, String> labels) {
    return getApplications(labels);
  }

  public boolean killApplication(KubernetesApplication app) {
    LOG.info("Deleting pod {}", app.getApplicationPod().getMetadata().getName());
    return client.inNamespace(app.getApplicationNamespace()).pods().delete(app.getApplicationPod());
  }

  public String[] getApplicationLog(KubernetesApplication app, Integer cacheLogSize) {
    String[] result = null;
    try {
      result = this.client.inNamespace(app.getApplicationNamespace()).pods()
        .withName(app.getApplicationPod().getMetadata().getName())
        .tailingLines(cacheLogSize).getLog().split("\n");
    } catch (Exception exp) {

    }
    return result;
  }
}
