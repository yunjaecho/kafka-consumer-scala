import java.util.concurrent.Executors

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object FutureTest extends App {
  print("general")
  general
  println("--------------------")
  print("synchronize")
  synchronize
  println("--------------------")
  print("future")
  future
  println("--------------------")
  print("futureJoin")
  futureJoin

  def future: Unit = {
    implicit  val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

    val futures = ArrayBuffer.empty[Future[Int]]
    for (i <- 1 to 10) {
      futures += Future {randomInt(100, 200)}
    }

    val numbers = ArrayBuffer.empty[Int]

    for (f <- futures) {
      numbers += Await.result(f, Duration.Inf)
    }

    println(numbers)
  }

  def futureJoin = {
    implicit val ec = ExecutionContext
      .fromExecutor(Executors.newFixedThreadPool(10))

    val futures = List.fill(10) { Future { randomInt(100, 200) } }
    val f = Future.sequence(futures)
    val numbers = Await.result(f, Duration.Inf)
    println(numbers)
  }


  def randomInt(from: Int, to: Int): Int = {
    println(Thread.currentThread().getName)
    Thread.sleep(1000)
    (from + (to - from + 1) * Math.random()).toInt
  }

  def general: Unit = {
    val list = List.fill(10) { randomInt(100, 200)}
    println(list)
  }

  def synchronize = {
    val buf = ArrayBuffer.empty[Int]
    val threads = ArrayBuffer.empty[Thread]

    for (i <- 1 to 10) {
      val thread = new Thread(() => {
        val n = randomInt(100, 200)
        buf.synchronized(buf += n)
      })
      thread.start()
      threads += thread
    }

    for (thread <- threads)
      thread.join()
    println(buf.toList)

  }
}
