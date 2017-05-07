package services

import javax.inject._

import models.Image
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import play.api.Configuration

@Singleton
class MongoService @Inject()(config: Configuration) {
  val codecRegistry = fromRegistries(fromProviders(classOf[Image]), DEFAULT_CODEC_REGISTRY)

  val client = config.getString("mongo.url").map(MongoClient(_)).getOrElse(MongoClient())
  val database = config.getString("mongo.db").map(client.getDatabase)
    .getOrElse(client.getDatabase("default")).withCodecRegistry(codecRegistry)
}
