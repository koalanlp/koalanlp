package kr.bydelta.koala.server

import akka.actor.ActorSystem
import colossus.IOSystem
import colossus.core.ServerRef
import colossus.protocols.http.server.HttpServer
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
  def getTagger: CanTag

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

    logger info "Server initializing"
    getHttpServerRef
  }

  // $COVERAGE-ON$
  /**
    * Return a server instance.
    *
    * @param io IOSystem (implicit)
    * @return Reference of a ServerRef Instance
    */
  def getHttpServerRef(implicit io: IOSystem): ServerRef =
  HttpServer.basic("taggerServer", port, context => new Service(context, getTagger, getParser, dict))
}
