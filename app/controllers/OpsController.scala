package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class OpsController @Inject() extends Controller {
  def read = Action {
    Ok(Json.obj("version" -> ""))
  }
}
