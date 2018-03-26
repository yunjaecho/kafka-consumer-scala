package com.yunjae.consumer

import java.util.{Collections, Properties}
import java.util.concurrent.{ExecutorService, Executors}

import kafka.utils.Logging
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

class Consumer(brokers: String, topic: String, groupId: String) extends Logging{

  val properties = createConsumerConfig(brokers, groupId)
  val consumer = new KafkaConsumer[String, String](properties)
  var executor: ExecutorService = null

  def shutdown() = {
    if (consumer != null)
      consumer.close();
    if (executor != null)
      executor.shutdown();
  }

  /**
    * Create configuration for the consumer
    *
    * @param brokers
    * @param groupId
    * @return
    */
  def createConsumerConfig(brokers: String, groupId: String): Properties = {
    val properties = new Properties()
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    properties
  }

  def run() = {
    consumer.subscribe(Collections.singletonList(this.topic))

    Executors.newSingleThreadExecutor.execute {
      () => {
        while(true) {
          val records = consumer.poll(1000)

          records.forEach {record =>
            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset())
          }
        }
      }
    }

//    Executors.newSingleThreadExecutor.execute(new Runnable {
//      override def run(): Unit = {
//        while (true) {
//          val records = consumer.poll(1000)
//
//          for (record <- records) {
//            System.out.println(s"Received message: $record")
//          }
//        }
//      }
//    })
  }
}