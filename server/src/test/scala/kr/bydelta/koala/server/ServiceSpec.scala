package kr.bydelta.koala.server

import akka.actor.ActorSystem
import colossus.core.ServerContext
import colossus.protocols.http.{HttpBody, HttpRequest, HttpService}
import colossus.service._
import colossus.testkit._
import kr.bydelta.koala.kkma.{Dictionary, Parser, Tagger}
import kr.bydelta.koala.traits.{CanDepParse, CanTag, CanUserDict}
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
    override val dict: CanUserDict = Dictionary

    override def getTagger: CanTag[_] = new Tagger

    override def getParser: CanDepParse = new Parser
  }

  "TaggerService" should {
    "generate correct tag response" in {
      val connection = MockConnection.server(
        server.getServiceInitializer(null).onConnect.asInstanceOf[(ServerContext) => HttpService])
      val response = connection.typedHandler.handle(
        HttpRequest.post("/tag").withBody(HttpBody("나는 먹는다"))
      )

      val expectedOutput =
        """{"success":true,"data":[[{"word":"나는","in":[{"morph":"나","tag":"NP"},{"morph":"는","tag":"JX"}]},""" +
          """{"word":"먹는다","in":[{"morph":"먹","tag":"VV"},{"morph":"는","tag":"EP"},{"morph":"다","tag":"EF"}]}]]}"""
      CallbackAwait.result(response, 1.minute).body.bytes.utf8String must_== expectedOutput
    }

    "generate correct parse response" in {
      val connection = MockConnection.server(
        server.getServiceInitializer(null).onConnect.asInstanceOf[(ServerContext) => HttpService])
      val response = connection.typedHandler.handle(
        HttpRequest.post("/parse").withBody(HttpBody("나는 먹는다"))
      )

      val expectedOutput =
        """{"success":true,"data":[[{"word":"나는","depRel":"Object","rawDep":"(주어,목적)대상","children":[],"in":[{"morph":"나","tag":"NP"},{"morph":"는","tag":"JX"}]},""" +
          """{"word":"먹는다","depRel":"Conjunctive","rawDep":"연결","children":[0],"in":[{"morph":"먹","tag":"VV"},{"morph":"는","tag":"EP"},{"morph":"다","tag":"EF"}]}]]}"""
      CallbackAwait.result(response, 1.minute).body.bytes.utf8String must_== expectedOutput
    }

    "provide dictionary's put action" in {
      val connection = MockConnection.server(
        server.getServiceInitializer(null).onConnect.asInstanceOf[(ServerContext) => HttpService])
      val response = connection.typedHandler.handle(
        HttpRequest.put("/dict").withBody(HttpBody("""[{"morph":"개취","tag":"NNP"}]"""))
      )

      CallbackAwait.result(response, 1.minute).body.bytes.utf8String must_== """{"success":true}"""
      Dictionary.isDicChanged must beTrue
      Dictionary.userdic.reset()
      Dictionary.userdic.readLine() must_== "개취/NNP"
    }
  }
}
