package kr.bydelta.koala.server

import kr.bydelta.koala.kkma.{Dictionary, Parser, Tagger}
import kr.bydelta.koala.traits.CanCompileDict

object KKMAServer extends Server {
  override val port: Int = 8080
  override val dict: CanCompileDict = Dictionary

  override def getTagger = new Tagger

  override def getParser = new Parser
}
