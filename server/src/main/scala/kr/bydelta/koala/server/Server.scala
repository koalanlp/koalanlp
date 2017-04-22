package kr.bydelta.koala.server

import akka.actor.ActorSystem
import colossus._
import colossus.core.InitContext
import colossus.core.server.{Server => CServer}
import kr.bydelta.koala.traits.{CanCompileDict, CanDepParse, CanTag}

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
  implicit val dict: CanCompileDict
  /**
    * Generator function for tagger
    */
  implicit val taggerGenerator = () => this.getTagger
  /**
    * Generator function for parser
    */
  implicit val parserGenerator = () => this.getParser
  private[this] val logger = org.log4s.getLogger

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

  // $COVERAGE-OFF$
  /**
    * Main method for execution
    *
    * @param args Arguments.
    */
  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem()
    implicit val io = IOSystem()

    CServer.start("taggerServer", port)(getServiceInitializer)
    logger info "Server initialized"
  }

  // $COVERAGE-ON$

  /**
    * Get Service Initializer
    *
    * @param worker Worker Reference of Colossus
    * @return Service Initializer instance
    */
  def getServiceInitializer(worker: InitContext) = new ServiceInitializer(worker)
}
