package kr.bydelta.koala.kryo

import com.twitter.chill.{KryoBase, ScalaKryoInstantiator}
import kr.bydelta.koala.data.{Morpheme, Relationship, Sentence, Word}

/**
  * An Kryo Instantiator using ScalaKryoInstantiator.
  */
object KryoWrap {
  private val instantiator = new ScalaKryoInstantiator

  /**
    * Instantiate Kryo instance which serializers are registered in.
    *
    * @return Kryo instance.
    */
  def kryo: KryoBase = {
    val k = instantiator.newKryo()
    k.addDefaultSerializer(classOf[Morpheme], MorphemeSerializer)
    k.addDefaultSerializer(classOf[Word], WordSerializer)
    k.addDefaultSerializer(classOf[Sentence], SentenceSerializer)
    k.addDefaultSerializer(classOf[Relationship], RelationSerializer)
    k
  }
}
