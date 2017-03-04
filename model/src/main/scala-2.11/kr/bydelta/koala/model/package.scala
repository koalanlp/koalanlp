package kr.bydelta.koala

import cc.factorie.variable.{CategoricalDomain, CategoricalVariable, LabeledCategoricalVariable}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 17. 1. 10.
  */
package object model {
  type Corpora = Iterable[String]
  protected[koala] type LabelSeq = ArrayBuffer[Label]
  protected[koala] final val TokenD, LabelD = new CategoricalDomain[String]

  protected[koala] class Token(str: String) extends CategoricalVariable[String](str) {
    def domain = TokenD
  }

  protected[koala] class Label(str: String, val token: Token) extends LabeledCategoricalVariable[String](str) {
    def domain = LabelD
  }

  protected[koala] object Token {
    def apply(str: String) = new Token(str)

    def unapply(arg: Token): Option[String] = Some(arg.categoryValue)
  }

  protected[koala] object Label {
    def apply(str: String, token: String): Label = apply(str, Token(token))

    def apply(str: String, token: Token): Label = new Label(str, token)

    def unapply(arg: Label): Option[(String, Token)] = Some(arg.categoryValue -> arg.token)
  }

}
