package kr.bydelta.koala.model

import cc.factorie.Var
import cc.factorie.la.DenseTensor2
import cc.factorie.model.{DotFamilyWithStatistics2, Model, Parameters}

/**
  * Created by bydelta on 17. 1. 10.
  */

class CRFModel extends Model with Parameters {
  val markov = new DotFamilyWithStatistics2[Label, Label] {
    val weights = Weights(new DenseTensor2(LabelD.size, LabelD.size))
  }
  val observ = new DotFamilyWithStatistics2[Label, Token] {
    val weights = Weights(new DenseTensor2(LabelD.size, TokenD.size))
  }

  def factors(labels: Iterable[Var]) = labels match {
    case labels: LabelSeq =>
      labels.map(label => observ.Factor(label, label.token)) ++
        labels.sliding(2).map(window => markov.Factor(window.head, window.last))
  }
}
