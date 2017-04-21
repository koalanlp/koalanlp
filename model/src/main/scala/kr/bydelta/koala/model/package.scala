package kr.bydelta.koala

import org.tensorflow._

/**
  * Created by bydelta on 17. 4. 15.
  */
package object model {

  private[model] def op(opName: String, nodes: Output*)(implicit g: Graph): Output =
    op(opName, "", nodes: _*)

  private[model] def op(opName: String, name: String, nodes: Output*)(implicit g: Graph): Output = {
    val builder =
      if (name == "") tf.build(opName)
      else tf.build(opName, name)
    nodes.foldLeft(builder) {
      case (operation, node) =>
        operation.addInput(node)
    }.build().output(0)
  }

  implicit class TFOperationScalarOp(x: AnyVal) {
    def :*(y: Output)(implicit g: Graph) = tf.constant(x, name = "scalar") :* y
  }

  implicit class TFOperationOp(x: Output) {
    def +(y: Output)(implicit g: Graph) = op("Add", x, y)

    def -(y: Output)(implicit g: Graph) = op("Sub", x, y)

    def *(y: Output)(implicit g: Graph) = op("MatMul", x, y)

    def *[T <: AnyVal](number: T)(implicit g: Graph) = :*(tf.constant(number, name = "scalar"))

    def :*(y: Output)(implicit g: Graph) = op("Mul", x, y)

    def :/(y: Output)(implicit g: Graph) = op("Div", x, y)

    def %(y: Output)(implicit g: Graph) = op("Mod", x, y)

    def :**(y: Output)(implicit g: Graph) = op("Pow", x, y)
  }

  implicit class DataTypeOp(dType: DataType) {
    // TODO Complex values
    def isComplex = false

    def isReal = isInteger || isFloating

    def isInteger = dType == DataType.INT32 ||
      dType == DataType.INT64 ||
      dType == DataType.UINT8

    def isFloating = dType == DataType.FLOAT ||
      dType == DataType.DOUBLE
  }

}
