package kr.bydelta.koala.model

import org.tensorflow._

import scala.collection.mutable

/**
  * Created by bydelta on 17. 4. 8.
  */
object tf {
  private val counter = mutable.HashMap[String, Int]()

  def session()(implicit g: Graph) = new Session(g)

  // math_ops
  def add(x: Output, y: Output, name: String = "")
         (implicit g: Graph) = op("Add", name, x, y)

  // math_ops
  def argmin(input: Output,
             axis: Option[Output] = None,
             name: String = "",
             dimension: Option[Output] = None)(implicit g: Graph) = {
    val argAxis: Output =
      if (dimension.isDefined) {
        if (axis.isDefined) {
          throw new IllegalArgumentException("Cannot specify both 'axis' and 'dimension'")
        }
        dimension.get
      } else if (axis.isEmpty) {
        tf.constant(0)
      } else {
        axis.get
      }

    op("ArgMin", name, input, argAxis)
  }

  // math_ops
  def argmax(input: Output,
             axis: Option[Output] = None,
             name: String = "",
             dimension: Option[Output] = None)(implicit g: Graph) = {
    val argAxis: Output =
      if (dimension.isDefined) {
        if (axis.isDefined) {
          throw new IllegalArgumentException("Cannot specify both 'axis' and 'dimension'")
        }
        dimension.get
      } else if (axis.isEmpty) {
        tf.constant(0)
      } else {
        axis.get
      }

    op("ArgMax", name, input, argAxis)
  }

  // TODO complex value; sparse tensor
  // math_ops
  def abs(x: Output)(implicit g: Graph) = op("Abs", x)

  // math_ops
  def divide(x: Output, y: Output, name: String = "")
            (implicit g: Graph) = div(x, y, name)

  // math_ops
  def div(x: Output, y: Output, name: String = "")
         (implicit g: Graph) = {
    // TODO check base_dtype
    if (x.dataType() != y.dataType()) {
      throw new IllegalArgumentException(s"x and y must have the same dtype, got ${x.dataType()} != ${y.dataType()}")
    }
    if (x.dataType().isFloating || x.dataType().isComplex) {
      realdiv(x, y, name)
    } else {
      floordiv(x, y, name)
    }
  }

  // math_ops
  def subtract(x: Output, y: Output, name: String = "")
              (implicit g: Graph) = op("Sub", name, x, y)

  // TODO sparse tensor
  // math_ops
  def negative(x: Output, name: String = "")
              (implicit g: Graph) = op("Neg", name, x)

  // TODO sparse tensor
  // math_ops
  def sign(x: Output, name: String = "")
          (implicit g: Graph) = op("Sign", name, x)

  // TODO sparse tensor
  // math_ops
  def square(x: Output, name: String = "")
            (implicit g: Graph) = op("Square", name, x)

  // TODO sparse tensor
  // math_ops
  def sqrt(x: Output, name: String = "")
          (implicit g: Graph) = op("Sqrt", name, x)

  // TODO sparse tensor
  // math_ops
  def erf(x: Output, name: String = "")
         (implicit g: Graph) = op("Erf", name, x)

  // TODO IndexedSlices
  // math_ops
  def scalarMul(x: Output, y: Output, name: String = "")
               (implicit g: Graph) = {
    if (x.shape().numDimensions() == 0)
      multiply(x, y, name)
    else
      throw new IllegalArgumentException(s"Only scalar multiply works, got shape ${x.shape().toString}")
  }

  // math_ops
  def multiply(x: Output, y: Output, name: String = "")
              (implicit g: Graph) = op("Mul", name, x, y)

  // math_ops
  def pow(x: Output, y: Output, name: String = "")
         (implicit g: Graph) = op("Pow", name, x, y)

  // TODO Complex values
  // math_ops
  // op("Complex", name, x, y)
  def complex(x: Output, y: Output, name: String = "")
             (implicit g: Graph) =
  throw new UnsupportedOperationException("Java Tensorflow does not support complex values.")

  // TODO Complex values
  // math_ops
  // op("Real", name, x, y)
  def real(x: Output, name: String = "")(implicit g: Graph) =
  throw new UnsupportedOperationException("Java Tensorflow does not support complex values.")

  // TODO Complex values
  // math_ops
  // op("Imag", name, x, y)
  def imag(x: Output, name: String = "")(implicit g: Graph) =
  throw new UnsupportedOperationException("Java Tensorflow does not support complex values.")

  // math_ops
  def round(x: Output, name: String = "")(implicit g: Graph) = {
    if (x.dataType().isInteger)
      x
    else
      op("Round", name, x)
  }

  // math_ops
  def toFloat(x: Output, name: String = "ToFloat")
             (implicit g: Graph) = cast(x, DataType.FLOAT, name)

  // math_ops
  def toDouble(x: Output, name: String = "ToFloat")
              (implicit g: Graph) = cast(x, DataType.DOUBLE, name)

  // math_ops
  def toInt32(x: Output, name: String = "ToInt32")
             (implicit g: Graph) = cast(x, DataType.INT32, name)

  // math_ops
  def truediv(x: Output, y: Output)(implicit g: Graph) = {
    val castTo =
      if (x.dataType() == DataType.INT32 ||
        x.dataType() == DataType.UINT8) {
        Some(DataType.FLOAT)
      } else if (x.dataType() == DataType.INT64) {
        Some(DataType.DOUBLE)
      } else
        None
    val (a, b) = castTo match {
      case Some(d) => (cast(x, d), cast(y, d))
      case None => (x, y)
    }
    realdiv(a, b)
  }

  // math_ops
  def realdiv(x: Output, y: Output, name: String = "")
             (implicit g: Graph) = op("RealDiv", name, x, y)

  // math_ops
  def truncatediv(x: Output, y: Output, name: String = "")
                 (implicit g: Graph) = op("TruncateDiv", name, x, y)

  // math_ops
  def floorDiv(x: Output, y: Output, name: String = "")
              (implicit g: Graph) = floordiv(x, y, name)

  // math_ops
  def floordiv(x: Output, y: Output, name: String = "")
              (implicit g: Graph) = op("FloorDiv", name, x, y)

  // math_ops
  def truncatemod(x: Output, y: Output, name: String = "")
                 (implicit g: Graph) = op("TruncateMod", name, x, y)

  // math_ops
  def mod(x: Output, y: Output, name: String = "")
         (implicit g: Graph) = floormod(x, y, name)

  // math_ops
  def floormod(x: Output, y: Output, name: String = "")
              (implicit g: Graph) = op("FloorMod", name, x, y)

  //math_ops
  def logicalXOR(x: Output, y: Output, name: String = "LogicalXOR")
                (implicit g: Graph) =
  logicalAnd(logicalOr(x, y), logicalNot(logicalAnd(x, y)), name)

  //math_ops
  def logicalAnd(x: Output, y: Output, name: String = "LogicalAnd")
                (implicit g: Graph) =
  op("LogicalAnd", name, x, y)

  //math_ops
  def logicalOr(x: Output, y: Output, name: String = "LogicalOr")
               (implicit g: Graph) =
  op("LogicalOr", name, x, y)

  //math_ops
  def logicalNot(x: Output, name: String = "LogicalNot")
                (implicit g: Graph) =
  op("LogicalNot", name, x)

  //math_ops
  def less(x: Output, y: Output, name: String = "less")
          (implicit g: Graph) =
  op("Less", name, x, y)

  //math_ops
  def lessEqual(x: Output, y: Output, name: String = "lessEqual")
               (implicit g: Graph) =
  op("LessEqual", name, x, y)

  //math_ops
  def greater(x: Output, y: Output, name: String = "greater")
             (implicit g: Graph) =
  op("Greater", name, x, y)

  //math_ops
  def greaterEqual(x: Output, y: Output, name: String = "greaterEqual")
                  (implicit g: Graph) =
  op("GreaterEqual", name, x, y)

  // math_ops
  def range(start: Output,
            limit: Option[Output] = None,
            delta: Output = tf.constant(1),
            dType: Option[DataType] = None,
            name: String = "range")
           (implicit g: Graph) = {
    val (argStart, argLimit) =
      if (limit.isEmpty) (tf.constant(0), start)
      else (start, limit.get)

    val argDType =
      if (dType.isEmpty) {
        if (argStart.dataType().isReal &&
          argLimit.dataType().isReal &&
          delta.dataType().isReal) {
          Seq(argStart.dataType(),
            argLimit.dataType,
            delta.dataType()).maxBy(_.ordinal())
        } else {
          throw new IllegalArgumentException
        }
      } else
        dType.get

    op("Range", name,
      cast(argStart, argDType),
      cast(argLimit, argDType),
      cast(delta, argDType))
  }

  // TODO sparse tensor
  // math_ops
  def cast(x: Output, dataType: DataType, name: String = "")
          (implicit g: Graph) = {
    build("Cast", name)
      .addInput(x)
      .setAttr("DstT", dataType)
      .build()
      .output(0)
  }

  // math_ops
  def equal(x: Output, y: Output, name: String = "")
           (implicit g: Graph) = op("Equal", name, x, y)

  // math_ops
  def countNonZero(x: Output,
                   axis: Seq[Int] = Seq(),
                   keepDims: Boolean = false,
                   dType: DataType = DataType.INT64,
                   name: String = "",
                   reductionIndices: Seq[Int] = Seq())
                  (implicit g: Graph) = {
    cast(reduceSum(toInt64(notEqual(x, tf.constant(0))),
      axis, keepDims, "", reductionIndices), dType)
  }

  // math_ops
  def toInt64(x: Output, name: String = "ToInt64")
             (implicit g: Graph) = cast(x, DataType.INT64, name)

  // math_ops
  def notEqual(x: Output, y: Output, name: String = "")
              (implicit g: Graph) = op("NotEqual", name, x, y)

  // math_ops
  def reduceMean(x: Output,
                 axis: Seq[Int] = Seq(),
                 keepDims: Boolean = false,
                 name: String = "",
                 reductionIndices: Seq[Int] = Seq())
                (implicit g: Graph) = {
    build("Mean", name)
      .addInput(x)
      .addInputList(reductionDims(x, axis, reductionIndices))
      .setAttr("keep_dims", keepDims)
      .build().output(0)
  }

  // math_ops
  def reduceProd(x: Output,
                 axis: Seq[Int] = Seq(),
                 keepDims: Boolean = false,
                 name: String = "",
                 reductionIndices: Seq[Int] = Seq())
                (implicit g: Graph) = {
    build("Prod", name)
      .addInput(x)
      .addInputList(reductionDims(x, axis, reductionIndices))
      .setAttr("keep_dims", keepDims)
      .build().output(0)
  }

  // math_ops
  def reduceMin(x: Output,
                axis: Seq[Int] = Seq(),
                keepDims: Boolean = false,
                name: String = "",
                reductionIndices: Seq[Int] = Seq())
               (implicit g: Graph) = {
    build("Min", name)
      .addInput(x)
      .addInputList(reductionDims(x, axis, reductionIndices))
      .setAttr("keep_dims", keepDims)
      .build().output(0)
  }

  // math_ops
  def reduceMax(x: Output,
                axis: Seq[Int] = Seq(),
                keepDims: Boolean = false,
                name: String = "",
                reductionIndices: Seq[Int] = Seq())
               (implicit g: Graph) = {
    build("Max", name)
      .addInput(x)
      .addInputList(reductionDims(x, axis, reductionIndices))
      .setAttr("keep_dims", keepDims)
      .build().output(0)
  }

  // math_ops
  def reduceAll(x: Output,
                axis: Seq[Int] = Seq(),
                keepDims: Boolean = false,
                name: String = "",
                reductionIndices: Seq[Int] = Seq())
               (implicit g: Graph) = {
    build("All", name)
      .addInput(x)
      .addInputList(reductionDims(x, axis, reductionIndices))
      .setAttr("keep_dims", keepDims)
      .build().output(0)
  }

  // math_ops
  def reduceAny(x: Output,
                axis: Seq[Int] = Seq(),
                keepDims: Boolean = false,
                name: String = "",
                reductionIndices: Seq[Int] = Seq())
               (implicit g: Graph) = {
    build("Any", name)
      .addInput(x)
      .addInputList(reductionDims(x, axis, reductionIndices))
      .setAttr("keep_dims", keepDims)
      .build().output(0)
  }

  // math_ops
  def reduceLogsumexp(x: Output,
                      axis: Seq[Int] = Seq(),
                      keepDims: Boolean = false,
                      name: String = "",
                      reductionIndices: Seq[Int] = Seq())
                     (implicit g: Graph) = {
    val myMax = stopGradient(
      reduceMax(x, axis, true, "", reductionIndices)
    )
    val result = log(reduceSum(exp(x - myMax),
      axis, true, "", reductionIndices)) + myMax

    if (!keepDims)
      squeeze(result, axis.toArray)
    else
      result
  }

  //math_ops
  def trace(x: Output, name: String = "")
           (implicit g: Graph) = reduceSum(diag_part(x), Seq(-1), name = name)

  // math_ops
  def reduceSum(x: Output,
                axis: Seq[Int] = Seq(),
                keepDims: Boolean = false,
                name: String = "",
                reductionIndices: Seq[Int] = Seq())
               (implicit g: Graph) = {
    build("Sum", name)
      .addInput(x)
      .addInputList(reductionDims(x, axis, reductionIndices))
      .setAttr("keep_dims", keepDims)
      .build().output(0)
  }

  // TODO sparse tensor
  private def reductionDims(x: Output, axis: Seq[Int],
                            reductionIndices: Seq[Int])
                           (implicit g: Graph) = {
    (if (reductionIndices.nonEmpty) {
      if (axis.nonEmpty) {
        throw new IllegalArgumentException("Can't specify both 'axis' and 'reductionIndices'.")
      }
      reductionIndices.toArray
    } else {
      if (axis.nonEmpty) {
        axis.toArray
      } else {
        if (x.shape().numDimensions() != -1)
          (0 until x.shape().numDimensions()).toArray
        else
          (0 until rank(x)).toArray
      }
    }).map(tf.constant(_))
  }

  def constant[T](value: T, name: String = "", verifyShape: Boolean = false)
                 (implicit g: Graph) = {
    val builder = build("Constant", name)
    val t = Tensor.create(value)

    builder
      .setAttr("dType", t.dataType())
      .setAttr("value", t)
      .setAttr("shape", t.shape())
      .setAttr("verify_shape", verifyShape)
      .build()
      .output(0)
  }

  private[model] def build(opType: String, name: String = "")
                          (implicit g: Graph) = {
    val opName =
      if (name.isEmpty) {
        counter.update(opType, counter.getOrElse(opType, 0) + 1)
        val c = counter(opType)
        s"${opType}_$c"
      } else name
    g.opBuilder(opType, opName)
  }

  def diag_part(x: Output)(implicit g: Graph) = op("DiagPart", x)

  // TODO sparse matrix
  // math_ops
  def matmul(x: Output,
             y: Output,
             transposeX: Boolean = false,
             transposeY: Boolean = false,
             adjointX: Boolean = false,
             adjointY: Boolean = false,
             aIsSparse: Boolean = false,
             bIsSparse: Boolean = false,
             name: String = "")(implicit g: Graph) = {
    if (transposeX && adjointX)
      throw new IllegalArgumentException("Only one of transposeX and adjointX can be true.")
    if (transposeY && adjointY)
      throw new IllegalArgumentException("Only one of transposeY and adjointY can be true.")

    var X = x
    var Y = y

    if ((x.shape().numDimensions() == -1 || x.shape().numDimensions() > 2) &&
      (y.shape().numDimensions() == -1 || y.shape().numDimensions() > 2)) {
      var adjX = adjointX
      var adjY = adjointY

      if (transposeX) {
        X = conj(x)
        adjX = true
      }
      if (transposeY) {
        Y = conj(y)
        adjY = true
      }

      build("BatchMatMul", name)
        .addInput(X)
        .addInput(Y)
        .setAttr("adj_x", adjX)
        .setAttr("adj_y", adjY)
        .build().output(0)
    } else {
      var tX = transposeX
      var tY = transposeY

      if (adjointX) {
        X = conj(x)
        tX = true
      }
      if (adjointY) {
        Y = conj(y)
        tY = true
      }

      build("MatMul", name)
        .addInput(X)
        .addInput(Y)
        .setAttr("transpose_a", tX)
        .setAttr("transpose_b", tY)
        .build().output(0)
    }
  }

  def conj(x: Output)(implicit g: Graph) = op("Conj", x)

  // math_ops
  def addN(name: String = "")(nodes: Output*)(implicit g: Graph) = {
    if (nodes.length == 1) {
      if (name.nonEmpty)
        identity(nodes.head, name)
      else
        nodes.head
    } else {
      build("AddN")
        .addInputList(nodes.toArray)
        .build()
        .output(0)
    }
  }

  // math_ops
  def accumulateN(shape: Seq[Int] = Seq(),
                  tensorDType: Option[DataType] = None,
                  name: String = "")(nodes: Output*)
                 (implicit g: Graph) = {
    val argShape =
      if (shape.nonEmpty) {
        Shape.make(shape.head, shape.map(_.toLong).tail: _*)
      } else {
        Shape.unknown()
      }

    if (nodes.length == 1)
      nodes.head
    else {
      val dType = tensorDType.getOrElse(nodes.head.dataType())

    }
  }

  // TODO check
  def cross(x: Output, y: Output)(implicit g: Graph) = op("Cross", x, y)

  // TODO check
  def reciprocal(x: Output)(implicit g: Graph) = op("Reciprocal", x)

  def rsqrt(x: Output)(implicit g: Graph) = op("Rsqrt", x)

  def exp(x: Output)(implicit g: Graph) = op("Exp", x)

  def expm1(x: Output)(implicit g: Graph) = op("Expm1", x)

  def log(x: Output)(implicit g: Graph) = op("Log", x)

  def log1p(x: Output)(implicit g: Graph) = op("Log1p", x)

  def ceil(x: Output)(implicit g: Graph) = op("Ceil", x)

  def floor(x: Output)(implicit g: Graph) = op("Floor", x)

  def maximum(x: Output, y: Output)(implicit g: Graph) = op("Maximum", x, y)

  def minimum(x: Output, y: Output)(implicit g: Graph) = op("Minimum", x, y)

  def cos(x: Output)(implicit g: Graph) = op("Cos", x)

  def sin(x: Output)(implicit g: Graph) = op("Sin", x)

  def lbeta(x: Output)(implicit g: Graph) = {
    val logProdGammaX = reduce_sum(lgamma(x),
      reductionIndices = Seq(-1))
  }

  def lgamma(x: Output)(implicit g: Graph) = op("Lgamma", x)

  def tan(x: Output)(implicit g: Graph) = op("Tan", x)

  def acos(x: Output)(implicit g: Graph) = op("Acos", x)

  def asin(x: Output)(implicit g: Graph) = op("Asin", x)

  def atan(x: Output)(implicit g: Graph) = op("Atan", x)

  def digamma(x: Output)(implicit g: Graph) = op("Digamma", x)

  def erfc(x: Output)(implicit g: Graph) = op("Erfc", x)

  def squared_difference(x: Output, y: Output)(implicit g: Graph) = op("SquaredDifference", x, y)

  def igamma(x: Output, y: Output)(implicit g: Graph) = op("Igamma", x, y)

  def igammac(x: Output, y: Output)(implicit g: Graph) = op("Igammac", x, y)

  def zeta(x: Output, y: Output)(implicit g: Graph) = op("Zeta", x, y)

  def polygamma(x: Output, y: Output)(implicit g: Graph) = op("Polygamma", x, y)

  def betainc(x: Output, y: Output, z: Output)(implicit g: Graph) = op("Betainc", x, y, z)

  def rint(x: Output)(implicit g: Graph) = op("Rint", x)

  def diag(x: Output)(implicit g: Graph) = op("Diag", x)

  def transpose(x: Output, perm: Output)(implicit g: Graph) = op("Transpose", x, perm)

  def eye(x: Output)(implicit g: Graph) = ???

  def matrix_diag(x: Output)(implicit g: Graph) = op("MatrixDiag", x)

  def matrix_diag_part(x: Output)(implicit g: Graph) = op("MatrixDiagPart", x)

  def matrix_band_part(x: Output, lower: Output, upper: Output)(implicit g: Graph) = op("MatrixBandPart", x, lower, upper)

  def matrix_set_diag(x: Output, diag: Output)(implicit g: Graph) = op("MatrixSetDiag", x, diag)

  def matrix_transpose(x: Output)(implicit g: Graph) = ???

  def norm(x: Output)(implicit g: Graph) = ???

  def matrix_determinant(x: Output)(implicit g: Graph) = ???

  def matrix_inverse(x: Output)(implicit g: Graph) = ???

  def cholesky(x: Output)(implicit g: Graph) = ???

  def cholesky_solve(x: Output)(implicit g: Graph) = ???

  def matrix_solve(x: Output)(implicit g: Graph) = ???

  def matrix_triangular_solve(x: Output)(implicit g: Graph) = ???

  def matrix_solve_ls(x: Output)(implicit g: Graph) = ???

  def qr(x: Output)(implicit g: Graph) = ???

  def self_adjoint_eig(x: Output)(implicit g: Graph) = ???

  def self_adjoint_eigvals(x: Output)(implicit g: Graph) = ???

  def svd(x: Output)(implicit g: Graph) = ???

  def tensordot(x: Output)(implicit g: Graph) = ???

  def fft(x: Output)(implicit g: Graph) = ???

  def ifft(x: Output)(implicit g: Graph) = ???

  def fft2d(x: Output)(implicit g: Graph) = ???

  def ifft2d(x: Output)(implicit g: Graph) = ???

  def fft3d(x: Output)(implicit g: Graph) = ???

  def ifft3d(x: Output)(implicit g: Graph) = ???

  def reduce_prod(x: Output)(implicit g: Graph) = ???

  def reduce_min(x: Output)(implicit g: Graph) = ???

  def reduce_max(x: Output)(implicit g: Graph) = ???

  def reduce_all(x: Output)(implicit g: Graph) = ???

  def reduce_any(x: Output)(implicit g: Graph) = ???

  def reduce_logsumexp(x: Output)(implicit g: Graph) = ???

  def count_nonzero(x: Output)(implicit g: Graph) = ???

  def einsum(x: Output)(implicit g: Graph) = ???

  def cumsum(x: Output)(implicit g: Graph) = ???

  def cumprod(x: Output)(implicit g: Graph) = ???

  def segment_sum(x: Output)(implicit g: Graph) = ???

  def segment_prod(x: Output)(implicit g: Graph) = ???

  def segment_min(x: Output)(implicit g: Graph) = ???

  def segment_max(x: Output)(implicit g: Graph) = ???

  def segment_mean(x: Output)(implicit g: Graph) = ???

  def unsorted_segment_sum(x: Output)(implicit g: Graph) = ???

  def sparse_segment_sum(x: Output)(implicit g: Graph) = ???

  def sparse_segment_mean(x: Output)(implicit g: Graph) = ???

  def sparse_segment_sqrt_n(x: Output)(implicit g: Graph) = ???

  def setdiff1d(x: Output)(implicit g: Graph) = ???

  def where(x: Output)(implicit g: Graph) = ???

  def unique(x: Output)(implicit g: Graph) = ???

  def edit_distance(x: Output)(implicit g: Graph) = ???

  def invert_permutation(x: Output)(implicit g: Graph) = ???


}
