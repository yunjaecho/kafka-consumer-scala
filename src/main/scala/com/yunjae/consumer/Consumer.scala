package com.yunjae.consumer

import java.util.Properties

import scala.collection.JavaConverters._
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

class Consumer(brokers: String, topic: String, groupId: String) {

  val consumer = new KafkaConsumer[String, String](configuration)
  consumer.subscribe(List(topic).asJava)

  private def configuration: Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props
  }

  def receiveMessages(): Unit = {
    while (true) {
      val records: ConsumerRecords[String, String] = consumer.poll(1000)
      records.asScala.foreach(record => println(s"Received message: $record"))
    }
  }

}