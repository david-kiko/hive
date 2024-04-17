HADOOP_HOME=/opt/hadoop

export HADOOP_HEAPSIZE=""
jvm_mem_percent=70.0
if [ -n "$JVM_HEAP_MEM_PERCENT" ]; then
    jvm_mem_percent=$JVM_HEAP_MEM_PERCENT
fi
echo "jvm_mem_percent $jvm_mem_percent"

HADOOP_CLIENT_OPTS="$HADOOP_CLIENT_OPTS -XX:+UseContainerSupport -XX:MaxRAMPercentage=$jvm_mem_percent"