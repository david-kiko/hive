package org.apache.hive.spark.client.kubernetes;

import io.fabric8.kubernetes.api.model.Pod;

public class KubernetesApplication {
  private Pod driverPod;

  KubernetesApplication(Pod driverPod) {
    this.driverPod = driverPod;
  }

  public String getApplicationTag() {
    return this.driverPod.getMetadata().getLabels().get(KubernetesConstants.SPARK_APP_TAG_LABEL);
  }

  public String getApplicationId() {
    return this.driverPod.getMetadata().getLabels().get(KubernetesConstants.SPARK_APP_ID_LABEL);
  }

  public String getApplicationNamespace() {
    return this.driverPod.getMetadata().getNamespace();
  }

  public Pod getApplicationPod() {
    return this.driverPod;
  }

}
