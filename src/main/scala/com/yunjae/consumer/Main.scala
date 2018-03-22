package com.yunjae.consumer

object Main extends App {
  val scanner = new java.util.Scanner(System.in)
  print("brokers : ")
  val brokers = scanner.nextLine()
  print("topic : ")
  val topic = scanner.nextLine()
  print("groupId : ")
  val groupId = scanner.nextLine()

  scanner.close()

  val consumer = new Consumer(brokers = brokers, topic = topic, groupId = groupId)
  consumer.receiveMessages()


}
