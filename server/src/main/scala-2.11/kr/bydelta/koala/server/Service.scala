package kr.bydelta.koala.server

import colossus.core.ServerContext
import colossus.protocols.http.HttpMethod._
import colossus.protocols.http.UrlParsing._
import colossus.protocols.http.server.RequestHandler
import colossus.protocols.http.{Http, HttpRequest, HttpResponse}
import colossus.protocols.http.HttpMethod.{Get, Post, Put}
import colossus.protocols.http.UrlParsing.Root
import colossus.protocols.http.server.RequestHandler
import colossus.service.Callback
import colossus.service.GenRequestHandler.PartialHandler
import kr.bydelta.koala.traits.{CanCompileDict, CanDepParse, CanTag}

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
      Callback.successful(request.notFound(Json.failure("There is no such path in the server.")))
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

      Json.success(para.map(parser.parse))
    } catch {
      case e: Throwable =>
        Json.failure(e.getStackTrace.mkString("\n"))
    }
    Callback.successful(request.ok(json))
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

      Json.success(para)
    } catch {
      case e: Throwable =>
        Json.failure(e.getStackTrace.mkString("\n"))
    }
    Callback.successful(request.ok(json))
  }

  /**
    * Handle dictionary insert request.
    *
    * @param request Request instance
    * @return Response callback
    */
  def dictReq(request: HttpRequest): Callback[HttpResponse] = {
    try {
      val list = Json.parseDictJson(request.body.bytes.utf8String)
      dict.addUserDictionary(list: _*)
      Callback.successful(request.ok(Json.success()))
    } catch {
      case _: Throwable =>
        Callback.successful(request.badRequest(Json.failure("Invalid JSON format!")))
    }
  }
}
