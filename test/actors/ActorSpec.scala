package actors

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, MustMatchers, OptionValues, WordSpecLike}

abstract class ActorSpec extends TestKit(ActorSystem("test"))
  with WordSpecLike with MustMatchers with OptionValues with BeforeAndAfterAll {

  override def afterAll {
    shutdown()
  }
}
