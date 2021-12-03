package learning

import zio._
import zio.duration._

object Fibers extends zio.App {

  def printThread = s"[${Thread.currentThread().getName}]"

  val zmol = ZIO.succeed(42)

  val showerTime = ZIO.succeed("I am taking a shower")
  val boilingWater = ZIO.succeed("Boiling water for my coffee!")
  val preparingCoffee = ZIO.succeed("Preparing coffee!")

  def concurrent() = for {
    _ <- showerTime.debug(printThread).fork
    _ <- boilingWater.debug(printThread)
    _ <- preparingCoffee.debug(printThread)
  } yield ()

  def concurrent2() = for {
    showerFiber <- showerTime.debug(printThread).fork
    boilingFiber <- boilingWater.debug(printThread).fork
    zf <- showerFiber.zip(boilingFiber).join.debug(printThread)
    _ <- ZIO.succeed(s"result done ${zf}").debug(printThread) *> preparingCoffee.debug(printThread)
  } yield ()

  val callFromAlice = ZIO.succeed("Alice is calling")
  val boilingWaterWithTime = boilingWater.debug(printThread) *> ZIO.sleep(2.seconds) *>
    ZIO.succeed(s"Boiled water done!")

  def interruptable() = for {
    _ <- showerTime.debug(printThread)
    boilingFiber <- boilingWaterWithTime.debug(printThread).fork
    _ <- callFromAlice.debug(printThread) *> boilingFiber.interrupt.debug(printThread)
    _ <- ZIO.succeed("Screw my coffee! Going to a cafe with Alice!").debug(printThread)
  } yield ()

  def run(args: List[String]) = interruptable().exitCode
}
