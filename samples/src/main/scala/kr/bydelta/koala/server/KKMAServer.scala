package kr.bydelta.koala.server

import kr.bydelta.koala.kkma.{Dictionary, Parser, Tagger}

object KKMAServer extends Server {
  override val port: Int = 8080
  override val dict = Dictionary

  override def getTagger = new Tagger

  override def getParser = new Parser
}
