[kr.bydelta.koala](../index.md) / [CanExtractResource](./index.md)

# CanExtractResource

`abstract class CanExtractResource`

Jar Resource에 포함된 모형을 임시디렉터리에 압축해제하기 위한 interface

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CanExtractResource()`<br>Jar Resource에 포함된 모형을 임시디렉터리에 압축해제하기 위한 interface |

### Properties

| Name | Summary |
|---|---|
| [modelName](model-name.md) | `abstract val modelName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>모델의 명칭. |

### Functions

| Name | Summary |
|---|---|
| [extractResource](extract-resource.md) | `fun extractResource(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>압축해제 작업 |
| [getExtractedPath](get-extracted-path.md) | `fun getExtractedPath(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>압축해제된 임시 디렉터리의 위치. |
