package kr.bydelta.koala.kryo

import com.twitter.chill.ScalaKryoInstantiator
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}

/**
  * Created by bydelta on 16. 8. 5.
  */
object KryoWrap {
  private val instantiator = new ScalaKryoInstantiator

  def kryo = {
    val k = instantiator.newKryo()
    k.addDefaultSerializer(classOf[Morpheme], MorphemeSerializer)
    k.addDefaultSerializer(classOf[Word], WordSerializer)
    k.addDefaultSerializer(classOf[Sentence], SentenceSerializer)
    k
  }
}
