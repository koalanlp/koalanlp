[kr.bydelta.koala.data](../index.md) / [Sentence](index.md) / [getNouns](./get-nouns.md)

# getNouns

`fun getNouns(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`

체언(명사, 수사, 대명사) 및 체언 성격의 어휘를 포함하는 어절들을 가져옵니다.
(명사형 전성어미 [POS.ETN](../../kr.bydelta.koala/-p-o-s/-e-t-n.md), 명사 파생 접미사 [POS.XSN](../../kr.bydelta.koala/-p-o-s/-x-s-n.md) 포함)

## 참고

**전성어미**는 용언 따위에 붙어 다른 품사의 기능을 수행하도록 변경하는 어미입니다.
예) '멋지게 살다'를 '멋지게 삶'으로 바꾸는 명사형 전성어미 '-ㅁ'이 있습니다. 원 기능은 동사이므로 부사의 수식을 받고 있습니다.

**파생접미사**는 용언의 어근이나 단어 따위에 붙어서 명사로 파생되도록 하는 접미사입니다.
예) 역시 '살다'를 '삶'으로 바꾸는 명사파생 접미사 '-ㅁ'이 있습니다. 이 경우 명사이므로 '멋진 삶'과 같이 형용사의 수식을 받습니다.

**Return**
체언 또는 체언 성격의 어휘를 포함하는 어절의 목록

