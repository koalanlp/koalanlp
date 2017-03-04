package kr.bydelta.koala.model

import cc.factorie.infer.InferByBPChain
import cc.factorie.optimize.{ConjugateGradient, LikelihoodExample, ParallelOnlineTrainer}
import kr.bydelta.koala.traits.CanTag


object POSTaggerTrainer {
  def train(corpora: Corpora, references: CanTag[_]*): Tagger = {
    val model = new CRFModel
    val labelSequences = new SentenceMatchIterator(references, corpora)

    val trainer = new ParallelOnlineTrainer(model.parameters, new ConjugateGradient)
    trainer.trainFromExamples(labelSequences.toIterable.map {
      labels => new LikelihoodExample(labels, model, InferByBPChain)
    })

    new Tagger(model, labelSequences.getIrregulars)
  }
}



