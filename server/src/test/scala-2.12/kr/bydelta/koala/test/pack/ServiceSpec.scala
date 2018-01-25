package kr.bydelta.koala.test.pack

import colossus.core.ServerRef
import colossus.protocols.http.{HttpBody, HttpCode, HttpRequest}
import colossus.testkit.HttpServiceSpec
import kr.bydelta.koala.kkma.{Dictionary, Parser, Tagger}
import kr.bydelta.koala.server.Server
import kr.bydelta.koala.traits.{CanCompileDict, CanDepParse, CanTag}
import org.json.JSONObject

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
  val ref: ServerRef = server.getHttpServerRef

  override def service: ServerRef = ref

  override def requestTimeout: FiniteDuration = 1.minute

  "TaggerService" should {
    "generate correct tag response" in {
      def expectedOutput(str: String): Boolean =
        try {
          val json = new JSONObject(str)
          if (!json.getBoolean("success")) false
          else {
            val data = json >>> "data" >> 0 >>> "words"
            assert(data.length() == 2)

            val firstWord = data >> 0
            assert(firstWord ? "surface" == "나는")
            assert((firstWord >>> "dependents").length() == 0)
            assert((firstWord >>> "morphemes").length() == 2)
            assert((firstWord >>> "morphemes" >> 0) ? "surface" == "나")
            assert((firstWord >>> "morphemes" >> 0) ? "tag" == "NP")
            assert((firstWord >>> "morphemes" >> 1) ? "surface" == "는")
            assert((firstWord >>> "morphemes" >> 1) ? "tag" == "JX")

            val secondWord = data >> 1
            assert(secondWord ? "surface" == "먹는다")
            assert((secondWord >>> "dependents").length() == 0)
            assert((secondWord >>> "morphemes").length() == 3)
            assert((secondWord >>> "morphemes" >> 0) ? "surface" == "먹")
            assert((secondWord >>> "morphemes" >> 0) ? "tag" == "VV")
            assert((secondWord >>> "morphemes" >> 1) ? "surface" == "는")
            assert((secondWord >>> "morphemes" >> 1) ? "tag" == "EP")
            assert((secondWord >>> "morphemes" >> 2) ? "surface" == "다")
            assert((secondWord >>> "morphemes" >> 2) ? "tag" == "EF")

            true
          }
        } catch {
          case _: Throwable => false
        }

      expectCodeAndBodyPredicate(HttpRequest.post("/tag").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
      expectCodeAndBodyPredicate(HttpRequest.get("/tag").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
      expectCodeAndBodyPredicate(HttpRequest.put("/tag").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
    }

    "generate correct parse response" in {
      def expectedOutput(str: String): Boolean =
        try {
          val json = new JSONObject(str)
          if (!json.getBoolean("success")) false
          else {
            val root = json >>> "data" >> 0 >>> "root"
            assert((root >> 0) ? "rel" == "Conjunctive")
            assert((root >> 0).getInt("childID") == 1)
            val data = json >>> "data" >> 0 >>> "words"
            assert(data.length() == 2)

            val firstWord = data >> 0
            assert(firstWord ? "surface" == "나는")
            assert((firstWord >>> "dependents").length() == 0)
            assert((firstWord >>> "morphemes").length() == 2)
            assert((firstWord >>> "morphemes" >> 0) ? "surface" == "나")
            assert((firstWord >>> "morphemes" >> 0) ? "tag" == "NP")
            assert((firstWord >>> "morphemes" >> 1) ? "surface" == "는")
            assert((firstWord >>> "morphemes" >> 1) ? "tag" == "JX")

            val secondWord = data >> 1
            assert(secondWord ? "surface" == "먹는다")
            assert((secondWord >>> "dependents").length() == 1)
            assert((secondWord >>> "dependents" >> 0) ? "rel" == "Object")
            assert((secondWord >>> "dependents" >> 0).getInt("childID") == 0)
            assert((secondWord >>> "morphemes").length() == 3)
            assert((secondWord >>> "morphemes" >> 0) ? "surface" == "먹")
            assert((secondWord >>> "morphemes" >> 0) ? "tag" == "VV")
            assert((secondWord >>> "morphemes" >> 1) ? "surface" == "는")
            assert((secondWord >>> "morphemes" >> 1) ? "tag" == "EP")
            assert((secondWord >>> "morphemes" >> 2) ? "surface" == "다")
            assert((secondWord >>> "morphemes" >> 2) ? "tag" == "EF")

            true
          }
        } catch {
          case _: Throwable => false
        }

      expectCodeAndBodyPredicate(HttpRequest.post("/parse").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
      expectCodeAndBodyPredicate(HttpRequest.get("/parse").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
      expectCodeAndBodyPredicate(HttpRequest.put("/parse").withBody(HttpBody("나는 먹는다")), HttpCode(200), expectedOutput)
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
