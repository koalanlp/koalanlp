package kr.bydelta.koala.server

import akka.actor.ActorSystem
import colossus.core.ServerContext
import colossus.protocols.http.server.RequestHandler
import colossus.protocols.http.{HttpBody, HttpRequest}
import colossus.service._
import colossus.testkit._
import kr.bydelta.koala.kkma.{Dictionary, Parser, Tagger}
import kr.bydelta.koala.traits.{CanCompileDict, CanDepParse, CanTag}
import org.specs2.mutable._

import scala.concurrent.duration._

/**
  * Created by bydelta on 16. 7. 31.
  */
class ServiceSpec extends Specification {
  implicit val actorSystem = ActorSystem()
  implicit val callbackExec: CallbackExecutor = FakeIOSystem.testExecutor
  val server = new Server {
    override val port: Int = 8080
    override val dict: CanCompileDict = Dictionary

    override def getTagger: CanTag[_] = new Tagger

    override def getParser: CanDepParse = new Parser
  }

  "TaggerService" should {
    "generate correct tag response" in {
      val connection = MockConnection.server(
        server.getServiceInitializer(null).onConnect.asInstanceOf[(ServerContext) => RequestHandler])
      val respPost = connection.typedHandler.handleRequest(
        HttpRequest.post("/tag").withBody(HttpBody("나는 먹는다"))
      )
      val respGet = connection.typedHandler.handleRequest(
        HttpRequest.get("/tag").withBody(HttpBody("나는 먹는다"))
      )
      val respPut = connection.typedHandler.handleRequest(
        HttpRequest.put("/tag").withBody(HttpBody("나는 먹는다"))
      )

      val expectedOutput =
        """{"success":true,"data":[{"words":[{"word":"나는","in":[{"morph":"나","tag":"NP"},{"morph":"는","tag":"JX"}]},""" +
          """{"word":"먹는다","in":[{"morph":"먹","tag":"VV"},{"morph":"는","tag":"EP"},{"morph":"다","tag":"EF"}]}]}]}"""
      CallbackAwait.result(respPost, 1.minute).body.bytes.utf8String must_== expectedOutput
      CallbackAwait.result(respGet, 1.minute).body.bytes.utf8String must_== expectedOutput
      CallbackAwait.result(respPut, 1.minute).body.bytes.utf8String must_== expectedOutput
    }

    "generate correct parse response" in {
      val connection = MockConnection.server(
        server.getServiceInitializer(null).onConnect.asInstanceOf[(ServerContext) => RequestHandler])
      val respPost = connection.typedHandler.handleRequest(
        HttpRequest.post("/parse").withBody(HttpBody("나는 먹는다"))
      )
      val respGet = connection.typedHandler.handleRequest(
        HttpRequest.get("/parse").withBody(HttpBody("나는 먹는다"))
      )
      val respPut = connection.typedHandler.handleRequest(
        HttpRequest.put("/parse").withBody(HttpBody("나는 먹는다"))
      )

      val expectedOutput =
        """{"success":true,"data":[{"root":[{"rel":"Conjunctive","rawRel":"연결","childID":1}],"words":[{"word":"나는","children":[],"in":[{"morph":"나","tag":"NP"},{"morph":"는","tag":"JX"}]},""" +
          """{"word":"먹는다","children":[{"rel":"Object","rawRel":"(주어,목적)대상","childID":0}],"in":[{"morph":"먹","tag":"VV"},{"morph":"는","tag":"EP"},{"morph":"다","tag":"EF"}]}]}]}"""
      CallbackAwait.result(respPost, 1.minute).body.bytes.utf8String must_== expectedOutput
      CallbackAwait.result(respGet, 1.minute).body.bytes.utf8String must_== expectedOutput
      CallbackAwait.result(respPut, 1.minute).body.bytes.utf8String must_== expectedOutput
    }

    "provide dictionary's put action" in {
      val connection = MockConnection.server(
        server.getServiceInitializer(null).onConnect.asInstanceOf[(ServerContext) => RequestHandler])
      val response = connection.typedHandler.handleRequest(
        HttpRequest.put("/dict").withBody(HttpBody("""[{"morph":"개취","tag":"NNP"}]"""))
      )

      CallbackAwait.result(response, 1.minute).body.bytes.utf8String must_== """{"success":true}"""
      Dictionary.isDicChanged must beTrue

      val response2 = connection.typedHandler.handleRequest(
        HttpRequest.post("/dict").withBody(HttpBody("""[{"morph":"취존","tag":"NNP"}]"""))
      )

      CallbackAwait.result(response2, 1.minute).body.bytes.utf8String must_== """{"success":true}"""
      Dictionary.isDicChanged must beTrue
      Dictionary.userdic.reset()

      Dictionary.userdic.readLine() must_== "개취/NNP"
      Dictionary.userdic.readLine() must_== "취존/NNP"
    }

    "handle illegal access" in {
      val connection = MockConnection.server(
        server.getServiceInitializer(null).onConnect.asInstanceOf[(ServerContext) => RequestHandler])
      val response = connection.typedHandler.handleRequest(
        HttpRequest.put("/index")
      )

      CallbackAwait.result(response, 1.minute).body.bytes.utf8String must be startingWith """{"success":false"""
    }

    "handle malformed JSON" in {
      val connection = MockConnection.server(
        server.getServiceInitializer(null).onConnect.asInstanceOf[(ServerContext) => RequestHandler])

      CallbackAwait.result(connection.typedHandler.handleRequest(
        HttpRequest.put("/dict").withBody(HttpBody("["))
      ), 1.minute).body.bytes.utf8String must be startingWith
        """{"success":false"""
    }
  }
}
