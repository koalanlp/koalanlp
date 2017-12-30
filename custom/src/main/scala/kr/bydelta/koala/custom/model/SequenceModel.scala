package kr.bydelta.koala.custom.model

import java.io.InputStream

import kr.bydelta.koala.traits.CanTag
import org.platanios.tensorflow.api._
import org.platanios.tensorflow.api.learn.layers.rnn.cell.BasicLSTMCell

/**
  * Created by bydelta on 17. 8. 19.
  * Based on Neural Turing Machine.
  */
class SequenceModel(val batchSize: Int) {
  private val input = tf.learn.Input(UINT8, Shape(batchSize, 32))
  private val trainInput = tf.learn.Input(UINT8, Shape(batchSize, 32))
  private val rnnCell = ???
}

class SequenceModelTrainer(references: CanTag*) {
  def train(stream: InputStream) = {

  }
}