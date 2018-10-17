[kr.bydelta.koala](index.md) / [correctVerbApply](./correct-verb-apply.md)

# correctVerbApply

`fun correctVerbApply(verb: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, isVerb: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, rest: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

주어진 용언의 원형 [verb](correct-verb-apply.md#kr.bydelta.koala$correctVerbApply(kotlin.String, kotlin.Boolean, kotlin.String)/verb)이 뒷 부분 [rest](correct-verb-apply.md#kr.bydelta.koala$correctVerbApply(kotlin.String, kotlin.Boolean, kotlin.String)/rest)와 같이 어미가 붙어 활용될 때, 불규칙 활용 용언과 모음조화를 교정함.

동사인 경우 [isVerb](correct-verb-apply.md#kr.bydelta.koala$correctVerbApply(kotlin.String, kotlin.Boolean, kotlin.String)/isVerb) = true이어야 하며, 형용사인 경우 false이어야 함.

