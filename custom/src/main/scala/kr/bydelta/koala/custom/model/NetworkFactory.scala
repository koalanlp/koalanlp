package kr.bydelta.koala.custom.model

import java.io.InputStream
import java.util

import kr.bydelta.koala.custom.dissembleKorean
import kr.bydelta.koala.traits.CanTag
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.layers.{GravesBidirectionalLSTM, Layer, RnnOutputLayer}
import org.deeplearning4j.nn.conf.{BackpropType, NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.dataset.api.DataSetPreProcessor
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction

import scala.io.Source
import scala.util.Random

/**
  * Created by bydelta on 17. 8. 27.
  */
object NetworkFactory {
  def createNetworkConf(iterations: Int = 1,
                        learningRate: Double = 0.1,
                        l2: Double = 0.001,
                        tBPTTForward: Int = 100,
                        layers: Seq[Layer]) = {
    val conf = new NeuralNetConfiguration.Builder()
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .iterations(iterations)
      .learningRate(learningRate)
      .regularization(true)
      .l2(l2)
      .weightInit(WeightInit.XAVIER)
      .updater(Updater.RMSPROP)
      .list()

    layers.zipWithIndex.foreach {
      case (layer, id) =>
        conf.layer(id, layer)
    }

    conf.backpropType(BackpropType.TruncatedBPTT)
      .tBPTTForwardLength(tBPTTForward).tBPTTBackwardLength(tBPTTForward)
      .pretrain(false).backprop(true).build()
  }

  def createBiLSTMLayer(nIn: Int, nOut: Int) = {
    new GravesBidirectionalLSTM.Builder()
      .nIn(nIn).nOut(nOut)
      .gateActivationFunction(Activation.HARDSIGMOID)
      .build()
  }

  def createRNNOutLayer(nIn: Int, nOut: Int) = {
    new RnnOutputLayer.Builder(LossFunction.MCXENT)
      .nIn(nIn).nOut(nOut)
      .activation(Activation.SOFTMAX)
      .build()
  }
}

class KoreanCharacterIterator(val stream: InputStream,
                              val miniBatchSize: Int = 10,
                              val taggers: Seq[CanTag]) extends DataSetIterator {
  private val lines = Source.fromInputStream(stream).getLines().toSeq
  private val lineSize = lines.size
  private var lineIt = Random.shuffle(lines).toIterator

  override def batch(): Int = ???

  override def cursor(): Int = ???

  override def totalExamples(): Int = ???

  override def resetSupported(): Boolean = ???

  override def inputColumns(): Int = ???

  override def getPreProcessor: DataSetPreProcessor = ???

  override def setPreProcessor(preProcessor: DataSetPreProcessor): Unit = ???

  override def getLabels: util.List[String] = ???

  override def totalOutcomes(): Int = ???

  override def reset(): Unit = {
    lineIt = lines.toIterator
    lineCount = lineSize
  }

  override def asyncSupported(): Boolean = ???

  override def numExamples(): Int = ???

  override def next(): DataSet = next(miniBatchSize)

  override def next(num: Int): DataSet = {
    val nExamples = Math.min(num, lineCount)

    val input = Nd4j.create(Array(nExamples, 20,))
  }

  override def hasNext: Boolean = ???

  private def getIterator =
    Random.shuffle(lines.toIterator.flatMap {
      line =>
        val seq = dissembleKorean(line)
        taggers.map {
          tag =>
            val tagged = tag.tagSentence(line)


            (seq, ???)
        }
    })
}