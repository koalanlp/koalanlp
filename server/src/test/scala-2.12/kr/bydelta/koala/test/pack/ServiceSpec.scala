package kr.bydelta.koala.test.pack

import colossus.core.ServerRef
import colossus.protocols.http.{HttpBody, HttpCode, HttpRequest}
import colossus.testkit.HttpServiceSpec
import kr.bydelta.koala.kkma.{Dictionary, Parser, Tagger}
import kr.bydelta.koala.server.Server
import kr.bydelta.koala.traits.{CanCompileDict, CanDepParse, CanTag}

import scala.concurrent.duration._

/**
  * Created by bydelta on 16. 7. 31.
  */
class ServiceSpec extends HttpServiceSpec {

  val server = new Server {
    override val port: Int = 8080
    override val dict: CanCompileDict = Dictionary

    override def getTagger: CanTag = new Tagger

    override def getParser: CanDepParse = new Parser
  }
  val ref = server.getHttpServerRef

  override def service: ServerRef = ref

  override def requestTimeout: FiniteDuration = 1.minute

  "TaggerService" should {
    "generate correct tag response" in {
      val expectedOutput =
        """{"success":true,"data":[{"words":[{"word":"나는","in":[{"morph":"나","tag":"NP"},{"morph":"는","tag":"JX"}]},""" +
          """{"word":"먹는다","in":[{"morph":"먹","tag":"VV"},{"morph":"는","tag":"EP"},{"morph":"다","tag":"EF"}]}]}]}"""

      expectCodeAndBody(HttpRequest.post("/tag").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
      expectCodeAndBody(HttpRequest.get("/tag").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
      expectCodeAndBody(HttpRequest.put("/tag").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
    }

    "generate correct parse response" in {
      val expectedOutput =
        """{"success":true,"data":[{"root":[{"rel":"Conjunctive","rawRel":"연결","childID":1}],"words":[{"word":"나는","children":[],"in":[{"morph":"나","tag":"NP"},{"morph":"는","tag":"JX"}]},""" +
          """{"word":"먹는다","children":[{"rel":"Object","rawRel":"(주어,목적)대상","childID":0}],"in":[{"morph":"먹","tag":"VV"},{"morph":"는","tag":"EP"},{"morph":"다","tag":"EF"}]}]}]}"""

      expectCodeAndBody(HttpRequest.post("/parse").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
      expectCodeAndBody(HttpRequest.get("/parse").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
      expectCodeAndBody(HttpRequest.put("/parse").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
    }

    "provide dictionary's put action" in {
      expectCodeAndBody(HttpRequest.put("/dict").withBody(HttpBody("""[{"morph":"개취","tag":"NNP"}]""")),
        HttpCode(200),
        """{"success":true}""")

      Dictionary.isDicChanged mustBe true

      expectCodeAndBody(HttpRequest.post("/dict").withBody(HttpBody("""[{"morph":"취존","tag":"NNP"}]""")),
        HttpCode(200),
        """{"success":true}""")

      Dictionary.isDicChanged mustBe true
      Dictionary.userdic.reset()

      Dictionary.userdic.readLine() mustEqual "개취/NNP"
      Dictionary.userdic.readLine() mustEqual "취존/NNP"
    }

    "handle illegal access" in {
      expectCode(HttpRequest.put("/index"), HttpCode(404))
    }

    "handle malformed JSON" in {
      expectCode(HttpRequest.put("/dict").withBody(HttpBody("[")), HttpCode(400))
    }
  }
}
