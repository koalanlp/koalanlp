package kr.bydelta.koala.server

import colossus.core.ServerContext
import colossus.protocols.http.UrlParsing._
import colossus.protocols.http.{Http, HttpRequest, HttpResponse, RequestHandler}
import colossus.protocols.http.HttpMethod.{Get, Post, Put}
import colossus.protocols.http.UrlParsing.Root
import colossus.service.Callback
import colossus.service.GenRequestHandler.PartialHandler
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Morpheme, Relationship}
import kr.bydelta.koala.traits.{CanCompileDict, CanDepParse, CanTag}
import play.api.libs.json.{JsArray, JsObject, Json}

/**
  * Tagger service provider class
  *
  * @param context Server context of Service
  * @param tagger  Tagger instance
  * @param parser  Parser instance
  * @param dict    Dictionary instance (singleton object)
  */
class Service(context: ServerContext,
              val tagger: CanTag,
              val parser: CanDepParse,
              val dict: CanCompileDict) extends RequestHandler(context) {

  override def handle: PartialHandler[Http] = {
    /* Handle Put on /dict, with body [{"morph":"형태소","tag":"품사}...] */
    case request@Put on Root / "dict" => dictReq(request)
    case request@Post on Root / "dict" => dictReq(request)

    /* Handle Post on /tag, with body of a paragraph */
    case request@Post on Root / "tag" => tagReq(request)
    case request@Get on Root / "tag" => tagReq(request)
    case request@Put on Root / "tag" => tagReq(request)

    /* Handle Post on /parse, with body of a paragraph */
    case request@Post on Root / "parse" => parseReq(request)
    case request@Get on Root / "parse" => parseReq(request)
    case request@Put on Root / "parse" => parseReq(request)

    case request =>
      Callback.successful(request.notFound("""{"success":false,"message":"There is no such path in the server."}"""))
  }

  /**
    * Handle parse request.
    *
    * @param request Request instance
    * @return Response callback with parsed result
    */
  def parseReq(request: HttpRequest): Callback[HttpResponse] = {
    val json = try {
      val para = tagger.tag(request.body.bytes.utf8String)

      Json.obj(
        "success" -> true,
        "data" ->
          JsArray(
            para.map {
              sentence =>
                val newSent = parser.parse(sentence)

                Json.obj(
                  "root" -> newSent.root.dependents.map {
                    case r@Relationship(_, fTag, target) =>
                      Json.obj(
                        "rel" -> fTag,
                        "rawRel" -> r.rawRel,
                        "childID" -> target
                      )
                  },
                  "words" -> JsArray(
                    newSent.map {
                      word =>
                        Json.obj(
                          "word" -> word.surface,
                          "children" -> word.dependents.map {
                            case r@Relationship(_, fTag, target) =>
                              Json.obj(
                                "rel" -> fTag,
                                "rawRel" -> r.rawRel,
                                "childID" -> target
                              )
                          },
                          "in" -> JsArray(
                            word.map {
                              case Morpheme(surf, tag) => Json.obj(
                                "morph" -> surf,
                                "tag" -> tag
                              )
                            }
                          )
                        )
                    }
                  )
                )
            }
          )
      )
    } catch {
      case e: Throwable =>
        Json.obj(
          "success" -> false,
          "message" -> e.getStackTrace.mkString("\n")
        )
    }
    Callback.successful(request.ok(json.toString))
  }

  /**
    * Handle tag request.
    *
    * @param request Request instance
    * @return Response callback with tagged result
    */
  def tagReq(request: HttpRequest): Callback[HttpResponse] = {
    val json = try {
      val para = tagger.tag(request.body.bytes.utf8String)

      Json.obj(
        "success" -> true,
        "data" ->
          JsArray(
            para.map {
              sentence =>
                Json.obj(
                  "words" -> JsArray(
                    sentence.map {
                      word =>
                        Json.obj(
                          "word" -> word.surface,
                          "in" -> JsArray(
                            word.map {
                              case Morpheme(surf, tag) => Json.obj(
                                "morph" -> surf,
                                "tag" -> tag
                              )
                            }
                          )
                        )
                    }
                  )
                )
            }
          )
      )
    } catch {
      case e: Throwable =>
        Json.obj(
          "success" -> false,
          "message" -> e.getStackTrace.mkString("\n")
        )
    }
    Callback.successful(request.ok(json.toString))
  }

  /**
    * Handle dictionary insert request.
    *
    * @param request Request instance
    * @return Response callback
    */
  def dictReq(request: HttpRequest): Callback[HttpResponse] = {
    try {
      val arr = Json.parse(request.body.bytes.toArray).as[JsArray]
      val list = arr.value.map {
        raw =>
          val item = raw.as[JsObject]
          val morph = (item \ "morph").as[String]
          val tag = POS withName (item \ "tag").as[String]
          morph -> tag
      }
      dict.addUserDictionary(list: _*)
      Callback.successful(request.ok("""{"success":true}"""))
    } catch {
      case _: Throwable =>
        Callback.successful(request.badRequest("""{"success":false,"message":"Invalid JSON format!"}"""))
    }
  }
}
