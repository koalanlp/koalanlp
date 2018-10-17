[kr.bydelta.koala](../index.md) / [DependencyTag](./index.md)

# DependencyTag

`enum class DependencyTag`

의존구문구조 기능표지자를 담은 Enum class입니다.
(ETRI 표준안)

[참고](http://aiopen.etri.re.kr/data/1.%20%EC%9D%98%EC%A1%B4%20%EA%B5%AC%EB%AC%B8%EB%B6%84%EC%84%9D%EC%9D%84%20%EC%9C%84%ED%95%9C%20%ED%95%9C%EA%B5%AD%EC%96%B4%20%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84%20%EA%B0%80%EC%9D%B4%EB%93%9C%EB%9D%BC%EC%9D%B8%20%EB%B0%8F%20%EC%97%91%EC%86%8C%EB%B8%8C%EB%A0%88%EC%9D%B8%20%EC%96%B8%EC%96%B4%EB%B6%84%EC%84%9D%20%EB%A7%90%EB%AD%89%EC%B9%98.pdf)

**Since**
1.x

### Enum Values

| Name | Summary |
|---|---|
| [SBJ](-s-b-j.md) | ''주어'': 술어가 나타내는 동작이나 상태의 주체가 되는 말 |
| [OBJ](-o-b-j.md) | ''목적어'': 타동사가 쓰인 문장에서 동작의 대상이 되는 말 |
| [CMP](-c-m-p.md) | ''보어'': 주어와 서술어만으로는 뜻이 완전하지 못한 문장에서, 그 불완전한 곳을 보충하여 뜻을 완전하게 하는 수식어. |
| [MOD](-m-o-d.md) | 체언 수식어(관형격). 관형격 체언구(NP_MOD), 관형형 용언구(VP_MOD), 관형절(S_MOD) |
| [AJT](-a-j-t.md) | 용언 수식어(부사격). 부사격 체언구(NP_AJT), 부사격 용언구(VP_AJT) 문말어미+부사격 조사(S_AJT) |
| [CNJ](-c-n-j.md) | ''접속어'': 단어와 단어, 구절과 구절, 문장과 문장을 이어 주는 구실을 하는 문장 성분. |
| [UNDEF](-u-n-d-e-f.md) | 정의되지 않음: 기존 PRN(삽입어구) 포함 |
| [ROOT](-r-o-o-t.md) | ROOT 지시자 |
