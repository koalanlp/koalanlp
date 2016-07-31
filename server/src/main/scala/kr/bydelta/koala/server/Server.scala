package kr.bydelta.koala.server

import akka.actor.ActorSystem
import colossus._
import colossus.core.{Server => CServer}
import kr.bydelta.koala.traits.{CanDepParse, CanTag, CanUserDict}

/**
  * Trait for server.
  */
trait Server {
  /**
    * Port number
    */
  val port: Int
  /**
    * Dictionary
    *
    * @return Dictionary object
    */
  val dict: CanUserDict

  /**
    * Tagger generator
    *
    * @return a tagger
    */
  def getTagger: CanTag[_]

  /**
    * Parser generator
    *
    * @return a parser
    */
  def getParser: CanDepParse

  /**
    * Main method for execution
    *
    * @param args Arguments.
    */
  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem()
    implicit val io = IOSystem()
    implicit val getTagger = () => this.getTagger
    implicit val getParser = () => this.getParser
    implicit val dict = this.dict

    CServer.start("taggerServer", port) {
      worker => new ServiceInitializer(worker)
    }
  }
}
