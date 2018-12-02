@file:JvmName("DataUtil")

package kr.bydelta.koala.data

import kr.bydelta.koala.*
import java.io.Serializable

/**
 * 저장할 수 있는 [Property]의 유형입니다.
 */
internal enum class Key {
    /** 다의어/동형이의어 의미번호 */
    WORD_SENSE,

    /** 형태소가 속하는 어절 **/
    WORD,

    /** 개체명 (목록) */
    NAMED_ENTITY,

    /** 상위 구문구조 */
    SYNTAX_PARENT,
    /** 하위 구문구조 (목록) */
    SYNTAX_CHILD,

    /** 의존관계 지배소 */
    DEP_GOVERNOR,
    /** 의존관계 피지배소 (목록) */
    DEP_DEPENDENT,

    /** 의미역 구조의 동사 (목록) */
    SRL_PREDICATE,
    /** 의미역 구조의 하위 논항 (목록) */
    SRL_ARGUMENT,

    /** 공지시어/상호참조/대용어 분석의 지칭 대상 */
    COREF_ENTITY,
    /** 공지시어/상호참조/대용어 분석의 지시어/대용어 (목록) */
    COREF_MENTION,
}

/**
 * ID가 이미 설정되었을 때 발생하는 Exception
 *
 * @since 2.0.0
 */
class AlreadySetIDException : Exception("The ID value was already initialized!")

/**
 * 텍스트 분석 과정에서 얻어지는 여러가지 값들을 표현하는 타입입니다.
 *
 * 다음 값들로 구현됩니다:
 * - [WordSense] 형태소 의미번호 속성
 * - [Entity] 개체명 정보 속성
 * - [SyntaxTree] 구문구조 속성
 * - [DepEdge] 의존구문구조 속성
 * - [RoleEdge] 의미역 구조 속성
 * - [CoreferenceGroup] 상호참조/공통 지시어 묶음 속성
 * - [Morpheme] 형태소
 * - [Word] 어절
 * - [Sentence] 문장
 *
 * @since 2.0.0
 */
interface Property : Serializable

/**
 * 텍스트 분석 과정에서 얻어지는 여러가지 값들을 표현하는 class.
 *
 * 다음 값들로 구현됩니다.
 * - [Entity] 개체명 정보 속성
 * - [SyntaxTree] 구문구조 속성
 * - [DepEdge] 의존구문구조 속성
 * - [RoleEdge] 의미역 구조 속성
 * - [Morpheme] 형태소
 * - [Word] 어절
 * - [Sentence] 문장
 *
 * @since 2.0.0
 */
abstract class CanHaveProperty : Property {
    /**
     * 가질 수 있는 단일 속성값을 저장할 [MutableMap]
     *
     * @since 2.0.0
     */
    private val properties: MutableMap<Key, Property> by lazy {
        mutableMapOf<Key, Property>()
    }

    /**
     * (internal function)
     *
     * [key] 속성에 해당하는 [T]-type 값 [value]를 저장합니다.
     *
     * 만약, 이전에 저장을 한 적이 있다면, 덮어쓰기 하지 않고 [IllegalStateException]를 발생합니다.
     * 만약, 목록형 변수에 값을 지정하려 한다면, 덮어쓰기 하지 않고 [UnsupportedOperationException]를 발생합니다.
     *
     * @since 2.0.0
     * @param[key] 속성값을 구분할 키 값으로, [Key]형입니다.
     * @param[value] 저장할 속성값. 속성값은 [Property]를 상속받아야 합니다.
     * @throws[IllegalStateException] 저장을 한 적이 있는 속성을 덮어씌우려는 경우
     * @throws[UnsupportedOperationException] 목록형 변수가 필요한 곳에서 호출한 경우
     */
    @Throws(IllegalStateException::class, UnsupportedOperationException::class)
    internal fun <T : Property> setProperty(key: Key, value: T) {
        when (key) {
            // 목록형 변수값이어야 하는 경우
            Key.NAMED_ENTITY, Key.SYNTAX_CHILD, Key.DEP_DEPENDENT, Key.SRL_ARGUMENT, Key.COREF_MENTION ->
                if (value is ListProperty<*>)
                    properties[key] = value
                else
                    throw UnsupportedOperationException("You should use 'addProperty' to insert a property to $key")
            else -> {
                if (key in properties)
                    throw IllegalStateException("There are more than two properties belong to the same key: $key")
                properties[key] = value
            }
        }
    }

    /**
     * (internal function)
     *
     * [key] 속성에 해당하는 [T]-type 값 [value]를 저장합니다.
     *
     * 만약, 목록형 변수가 아닌 곳에 값을 지정하려 한다면, 덮어쓰기 하지 않고 [UnsupportedOperationException]를 발생합니다.
     *
     * @since 2.0.0
     * @param[key] 속성값을 구분할 키 값으로, [Key]형입니다.
     * @param[value] 저장할 속성값. 속성값은 [Property]를 상속받아야 합니다.
     * @throws[UnsupportedOperationException] 목록형 변수가 필요한 곳에서 호출한 경우
     */
    @Throws(UnsupportedOperationException::class)
    @Suppress("UNCHECKED_CAST")
    internal fun <T : Property> addProperty(key: Key, value: T) {
        when (key) {
            // 목록형 변수값이어야 하는 경우
            Key.NAMED_ENTITY, Key.SYNTAX_CHILD, Key.DEP_DEPENDENT, Key.SRL_ARGUMENT, Key.SRL_PREDICATE, Key.COREF_MENTION -> {
                val prop = properties[key] as? ListProperty<T>

                if (prop == null) {
                    properties[key] = ListProperty(value)
                } else {
                    prop.values.add(value)
                }
            }
            else ->
                throw UnsupportedOperationException("You should use 'setProperty' to insert a property to $key")
        }
    }

    /**
     * (internal, 테스트 지원용)
     * property를 삭제합니다.
     *
     * @since 2.0.0
     * @param[key] 삭제할 키값 ([Key] 형)
     */
    internal fun removeProperty(key: Key) {
        properties.remove(key)
    }

    /**
     * (internal function)
     * [key]로 지정된 [T]-type의 property를 가져옵니다.
     *
     * @since 2.0.0
     * @param[key] 속성값을 가져올 키
     */
    @Suppress("UNCHECKED_CAST")
    internal fun <T : Property> getProperty(key: Key): T? = properties[key] as? T
}

/**
 * 속성의 목록을 저장할, 불변형(immutable) 목록 속성 값
 *
 * @since 2.0.0
 * @constructor 목록 속성을 생성합니다
 * @param values 목록 속성에 포함되는 값들입니다.
 */
class ListProperty<T : Property> internal constructor(internal val values: MutableList<T> = mutableListOf()) :
        Property, List<T> by values {

    /**
     * @constructor 목록 속성을 생성합니다
     * @param values 목록 속성에 포함되는 값들입니다.
     */
    internal constructor(vararg values: T) : this(mutableListOf(*values))

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is ListProperty<*> -> this.size == other.size && this.zip(other).all { it.first == it.second }
            else -> false
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     *
     * * Whenever it is invoked on the same object more than once, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified.
     * * If two objects are equal according to the equals() method, then calling the hashCode method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = this.sumBy { it.hashCode() }

    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L
    }
}

/**
 * 정수형 값을 저장할 [Property] class.
 *
 * 보통, 다의어/동형이의어 분별 작업에서 어깨/의미번호를 저장하는 데 활용합니다.
 *
 * ## 참고
 * **다의어 분별**은 동일한 단어의 여러 의미를 구분하는 작업입니다.
 * 예) '말1'은 다음 의미를 갖는 다의어이며, 다의어 분별 작업은 이를 구분합니다.
 * 1. 사람의 생각이나 느낌 따위를 표현하고 전달하는 데 쓰는 음성 기호.
 * 2. 음성 기호로 생각이나 느낌을 표현하고 전달하는 행위. 또는 그런 결과물.
 * 3. 일정한 주제나 줄거리를 가진 이야기.
 *
 * **동형이의어 분별**은 동일한 형태지만 다른 의미를 갖는 어절을 구분하는 작업입니다.
 * 예) '말'은 다음과 같은 여러 동형이의어의 표면형입니다.
 * 1. '말1': 사람의 생각이나 느낌 따위를 표현하고 전달하는 데 쓰는 음성 기호
 * 2. '말2': 톱질을 하거나 먹줄을 그을 때 밑에 받치는 나무
 * 3. '말3': 곡식, 액체, 가루 따위의 분량을 되는 데 쓰는 그릇
 * 4. '말4': 말과의 포유류
 * ...
 *
 * 아래를 참고해보세요.
 * * [kr.bydelta.koala.proc.CanDisambiguateSense] 동형이의어/다의어 분별 interface
 * * [Morpheme.getWordSense] 형태소의 어깨번호/의미번호를 가져오는 API
 *
 * @since 2.0.0
 * @constructor 정수값을 저장하는 [Property]를 생성합니다.
 * @param[id] 저장될 정수 값
 */
data class WordSense(val id: Int) : Property {
    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L // Version 2.0.0
    }
}

/**
 * 개체명 분석 결과를 저장할 [Property] class
 *
 * ## 참고
 * **개체명 인식**은 문장에서 인물, 장소, 기관, 대상 등을 인식하는 기술입니다.
 * 예) '철저한 진상 조사를 촉구하는 국제사회의 목소리가 커지고 있는 가운데, 트럼프 미국 대통령은 되레 사우디를 감싸고 나섰습니다.'에서, 다음을 인식하는 기술입니다.
 * * '트럼프': 인물
 * * '미국' : 국가
 * * '대통령' : 직위
 * * '사우디' : 국가
 *
 * 아래를 참고해보세요.
 * * [kr.bydelta.koala.proc.CanRecognizeEntity] 개체명 인식기 interface
 * * [Morpheme.getEntities] 형태소가 속하는 [Entity]를 가져오는 API
 * * [Word.getEntities] 어절에 연관된 모든 [Entity]를 가져오는 API
 * * [Sentence.getEntities] 문장에 포함된 모든 [Entity]를 가져오는 API
 * * [CoarseEntityType] [Entity]의 대분류 개체명 분류구조 Enum 값
 *
 * @since 2.0.0
 * @constructor 개체명 분석 결과를 저장합니다.
 * @param[surface] 개체명의 표면형 문자열.
 * @param[label] 개체명 대분류 값, [CoarseEntityType]에 기록된 개체명 중 하나.
 * @param[fineLabel] 개체명 세분류 값으로, [label]으로 시작하는 문자열.
 * @param[morphemes] 개체명을 이루는 형태소의 목록
 * @param[originalLabel] 원본 분석기가 제시한 개체명 분류의 값. 기본값은 null.
 */
class Entity(val surface: String,
             val label: CoarseEntityType,
             val fineLabel: String,
             morphemes: List<Morpheme>,
             val originalLabel: String? = null) : CanHaveProperty(), List<Morpheme> by morphemes {
    init {
        // 세부 개체명은 대분류 값에 소속되어야 합니다.
        assert(fineLabel.toUpperCase().startsWith(label.toString()))
        for (word in this) {
            word.addProperty(Key.NAMED_ENTITY, this)
        }
    }

    /**
     * 이 개체명과 공통된 대상을 지칭하는 공통 지시어 또는 대용어들의 묶음을 제공합니다.
     *
     * ## 참고
     * **공지시어 해소**는 문장 내 또는 문장 간에 같은 대상을 지칭하는 어구를 찾아 묶는 분석과정입니다.
     * 예) '삼성그룹의 계열사인 삼성물산은 같은 그룹의 계열사인 삼성생명과 함께'라는 문장에서
     * * '삼성그룹'과 '같은 그룹'을 찾아 묶는 것을 말합니다.
     *
     * **영형대용어 분석**은 문장에서 생략된 기능어를 찾아 문장 내 또는 문장 간에 언급되어 있는 어구와 묶는 분석과정입니다.
     * 예) '나는 밥을 먹었고, 영희도 먹었다'라는 문장에서,
     * * '먹었다'의 목적어인 '밥을'이 생략되어 있음을 찾는 것을 말합니다.
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanResolveCoref] 공지시어 해소, 대용어 분석기 interface
     * * [Sentence.getCorefGroups] 문장 내에 포함된 개체명 묶음 [CoreferenceGroup]들의 목록을 반환하는 API
     * * [CoreferenceGroup] 동일한 대상을 지칭하는 개체명을 묶는 API
     *
     * @return 공통된 대상을 묶은 [CoreferenceGroup]. 없다면 null.
     */
    fun getCorefGroup(): CoreferenceGroup? = getProperty(Key.COREF_ENTITY)

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$label($fineLabel; '$surface')"

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Entity -> this.label == other.label && this.size == other.size &&
                    this.fineLabel == other.fineLabel && this.zip(other).all { it.first == it.second }
            else -> false
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     *
     * * Whenever it is invoked on the same object more than once, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified.
     * * If two objects are equal according to the equals() method, then calling the hashCode method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = this.label.hashCode() + this.fineLabel.hashCode() + this.sumBy { it.hashCode() }

    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L
    }
}

/**
 * 공지시어 해소 또는 대용어 분석 결과를 저장할 class입니다.
 *
 * ## 참고
 * **공지시어 해소**는 문장 내 또는 문장 간에 같은 대상을 지칭하는 어구를 찾아 묶는 분석과정입니다.
 * 예) '삼성그룹의 계열사인 삼성물산은 같은 그룹의 계열사인 삼성생명과 함께'라는 문장에서
 * * '삼성그룹'과 '같은 그룹'을 찾아 묶는 것을 말합니다.
 *
 * **영형대용어 분석**은 문장에서 생략된 기능어를 찾아 문장 내 또는 문장 간에 언급되어 있는 어구와 묶는 분석과정입니다.
 * 예) '나는 밥을 먹었고, 영희도 먹었다'라는 문장에서,
 * * '먹었다'의 목적어인 '밥을'이 생략되어 있음을 찾는 것을 말합니다.
 *
 * 아래를 참고해보세요.
 * * [kr.bydelta.koala.proc.CanResolveCoref] 공지시어 해소, 대용어 분석기 interface
 * * [Sentence.getCorefGroups] 문장 내에 포함된 개체명 묶음 [CoreferenceGroup]들의 목록을 반환하는 API
 * * [Entity.getCorefGroup] 각 개체명을 묶어 같은 지시 대상을 갖는 묶음인 [CoreferenceGroup]를 가져오는 API
 *
 * @since 2.0.0
 * @constructor 공지시어 해소 또는 대용어 분석 결과를 저장합니다.
 * @param[children] 묶음에 포함되는 개체명들의 목록
 */
class CoreferenceGroup(children: List<Entity>) : Property, List<Entity> by children {
    init {
        for (child in this) {
            child.setProperty(Key.COREF_ENTITY, this)
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is CoreferenceGroup -> this.size == other.size && this.zip(other).all { it.first == it.second }
            else -> false
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     *
     * * Whenever it is invoked on the same object more than once, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified.
     * * If two objects are equal according to the equals() method, then calling the hashCode method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = this.sumBy { it.hashCode() }

    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L
    }
}

/**
 * [T]-type들의 트리 구조를 저장할 [Property]입니다. [Word]를 묶어서 표현하는 구조에 적용됩니다.
 *
 * @since 2.0.0
 * @constructor 트리 형태의 구조를 저장합니다.
 * @param label 트리에 붙어있는 표지자입니다. Null일 수 없습니다.
 * @param terminal 트리의 노드에서 연결되는 [Word]
 * @param children 트리/DAG의 자식 노드들
 */
abstract class Tree<L : Enum<*>, T : Tree<L, T>>(val label: L,
                                                 val terminal: Word?,
                                                 children: List<T>) : CanHaveProperty(), List<T> by children {
    /** 부모 노드를 나타내는 키 */
    internal abstract val PARENT_KEY: Key
    /** 자식 노드를 나타내는 키 */
    internal abstract val CHILD_KEY: Key

    /**
     * 부모 노드를 반환합니다.
     *
     * * 부모 노드가 초기화되지 않은 경우 null을 반환합니다.
     *
     * @since 2.0.0
     * @return 같은 타입의 부모 노드 또는 null
     */
    fun getParent() = getProperty<T>(PARENT_KEY)

    /**
     * 이 노드가 최상위 노드인지 확인합니다.
     *
     * @since 2.0.0
     * @return 최상위 노드인 경우 true
     */
    fun isRoot() = getParent() == null

    /**
     * 이 노드가 (terminal node를 제외하고) 자식 노드를 갖는지 확인합니다.
     *
     * * 구문분석 구조에서 terminal node는 [Word]가 됩니다.
     *
     * @since 2.0.0
     * @return 자식노드가 있다면 true.
     */
    fun hasNonTerminals() = this.isNotEmpty()

    /**
     * 이 노드를 포함해 모든 하위 Non-terminal 노드들에 속하는 terminal node들을 모읍니다.
     *
     * * 구문분석 구조에서는 이 구문구조에 속하는 어절의 모음입니다.
     *
     * @since 2.0.0
     * @return Terminal node의 목록
     */
    fun getTerminals(): List<Word> {
        val leaves = this.flatMap { it.getTerminals() }
        return (if (terminal != null) leaves.plusElement(terminal) else leaves).sortedBy { it.id }
    }

    /**
     * 이 노드의 Non-terminal 자식 노드를 모읍니다.
     *
     * * 이 함수는 읽기의 편의를 위한 syntactic sugar입니다. 즉 다음 구문은 동일합니다.
     * ```kotlin
     * for (item in x.getNonTerminals()){ ... }
     * for (item in x){ ... }
     * ```
     *
     * * 구문분석 구조에서는 이 구문구조에 속하는 하위 구문 구조입니다.
     *
     * @since 2.0.0
     * @return Non-terminal node의 목록
     */
    fun getNonTerminals(): List<T> = this

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Tree<*, *> -> {
                this.label == other.label && this.terminal == other.terminal &&
                        this.size == other.size && this.zip(other).all { it.first == it.second }
            }
            else -> false
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     *
     * * Whenever it is invoked on the same object more than once, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified.
     * * If two objects are equal according to the equals() method, then calling the hashCode method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = this.label.hashCode() + (this.terminal?.hashCode()
            ?: 0) + this.sumBy { it.hashCode() }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$label-Node(${terminal?.toString() ?: ""})"

    /**
     * 이 트리구조를 표현하는 텍스트 표현을 [buffer]에 담아 반환합니다.
     *
     * @since 2.0.0
     * @param[depth] 들여쓰기할 수준입니다. 숫자만큼 들여쓰기됩니다. (기본값 0)
     * @param[buffer] 결과를 저장할 버퍼입니다.
     * @return 트리구조의 표현을 담아 [buffer]에 저장한 후 돌려줍니다.
     */
    @JvmOverloads
    fun getTreeString(depth: Int = 0,
                      buffer: StringBuffer = StringBuffer()): StringBuffer {
        buffer.append("| ".repeat(depth))
        buffer.append(toString())

        for (child in this) {
            buffer.append('\n')
            child.getTreeString(depth + 1, buffer)
        }

        return buffer
    }
}

/**
 * 구문구조 분석의 결과를 저장할 [Property].
 *
 * ## 참고
 * **구문구조 분석**은 문장의 구성요소들(어절, 구, 절)이 이루는 문법적 구조를 분석하는 방법입니다.
 * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
 * 2개의 절이 있습니다
 * * 나는 밥을 먹었고
 * * 영희는 짐을 쌌다
 * 각 절은 3개의 구를 포함합니다
 * * 나는, 밥을, 영희는, 짐을: 체언구
 * * 먹었고, 쌌다: 용언구
 *
 * 아래를 참고해보세요.
 * * [kr.bydelta.koala.proc.CanParseSyntax] 구문구조 분석을 수행하는 interface.
 * * [Word.getPhrase] 어절이 직접 속하는 가장 작은 구구조 [SyntaxTree]를 가져오는 API
 * * [Sentence.getSyntaxTree] 전체 문장을 분석한 [SyntaxTree]를 가져오는 API
 * * [PhraseTag] 구구조의 형태 분류를 갖는 Enum 값
 *
 * @since 2.0.0
 * @constructor 구문구조 분석의 결과를 생성합니다.
 * @param label 구구조 표지자입니다. [PhraseTag] Enum 값입니다.
 * @param terminal 현재 구구조에 직접 속하는 [Word]들. 중간 구문구조인 경우 leaf를 직접 포함하지 않으므로 null.
 * @param children 현재 구구조에 속하는 하위 구구조 [SyntaxTree]. 하위 구구조가 없다면 빈 리스트.
 * @param originalLabel 원본 분석기의 표지자 String 값. 기본값은 null.
 */
class SyntaxTree constructor(label: PhraseTag,
                             terminal: Word? = null,
                             children: List<SyntaxTree> = emptyList(),
                             val originalLabel: String? = null) :
        Tree<PhraseTag, SyntaxTree>(label, terminal, children) {
    init {
        // terminal node에 부모로 지정
        this.terminal?.setProperty(PARENT_KEY, this)

        // non-terminal children node에 부모로 지정
        for (child in this) {
            child.setProperty(PARENT_KEY, this)
            addProperty(CHILD_KEY, child)
        }
    }

    /** 부모 노드를 나타내는 키 */
    override val PARENT_KEY: Key
        get() = Key.SYNTAX_PARENT

    /** 자식 노드를 나타내는 키 */
    override val CHILD_KEY: Key
        get() = Key.SYNTAX_CHILD

    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L
    }
}

/**
 * [T]-type의 DAG Edge를 저장합니다.
 *
 * @since 2.0.0
 * @constructor
 * @param src Edge의 시작점. 의존구문분석인 경우 지배소, 의미역인 경우 동사.
 * @param dest Edge의 종점. 의존구문분석인 경우 피지배소, 의미역인 경우 논항.
 * @param label Edge가 나타내는 관계.
 */
abstract class DAGEdge<L : Enum<*>, T : Property> internal constructor(val src: T?,
                                                                       val dest: T,
                                                                       val label: L?) : CanHaveProperty() {

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "${label ?: ""}('${src ?: "ROOT"}' → '$dest')"

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is DAGEdge<*, *> -> this.label == other.label && this.src == other.src && this.dest == other.dest
            else -> false
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     *
     * * Whenever it is invoked on the same object more than once, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified.
     * * If two objects are equal according to the equals() method, then calling the hashCode method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = (this.label?.hashCode() ?: 0) + this.dest.hashCode() + (this.src?.hashCode() ?: 0)
}

/**
 * 의존구문구조 분석의 결과.
 *
 * ## 참고
 * **의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.
 * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
 * 가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,
 * * '먹었고'가 '쌌다'와 대등하게 연결되고
 * * '나는'은 '먹었고'의 주어로 기능하며
 * * '밥을'은 '먹었고'의 목적어로 기능합니다.
 * * '영희는'은 '쌌다'의 주어로 기능하고,
 * * '짐을'은 '쌌다'의 목적어로 기능합니다.
 *
 * 아래를 참고해보세요.
 * * [kr.bydelta.koala.proc.CanParseDependency] 의존구조 분석을 수행하는 interface.
 * * [Word.getDependentEdges] 어절이 직접 지배하는 하위 의존구조 [DepEdge]의 목록을 가져오는 API
 * * [Word.getGovernorEdge] 어절이 지배당하는 상위 의존구조 [DepEdge]를 가져오는 API
 * * [Sentence.getDependencies] 전체 문장을 분석한 의존구조 [DepEdge]의 목록을 가져오는 API
 * * [PhraseTag] 의존구조의 형태 분류를 갖는 Enum 값 (구구조 분류와 같음)
 * * [DependencyTag] 의존구조의 기능 분류를 갖는 Enum 값
 *
 * @since 2.0.0
 * @constructor 의존구문구조 분석 결과를 생성합니다.
 * @param governor 의존구조의 지배소 [Word]. 문장의 Root에 해당하는 경우 null.
 * @param dependent 의존구조의 피지배소 [Word]
 * @param type 구구조 표지자, [PhraseTag] Enum 값 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)
 * @param depType 의존기능 표지자, [DependencyTag] Enum 값. 별도의 기능이 지정되지 않으면 null. (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)
 * @param originalLabel 원본 분석기의 표지자 String 값. 기본값은 null.
 */
class DepEdge constructor(val governor: Word?,
                          val dependent: Word,
                          val type: PhraseTag,
                          val depType: DependencyTag? = null,
                          val originalLabel: String? = null) :
        DAGEdge<DependencyTag, Word>(governor, dependent, depType) {
    init {
        // 지배소가 피지배소를 찾을 수 있게 지정
        governor?.addProperty(Key.DEP_DEPENDENT, this)
        // 피지배소가 지배소를 찾을 수 있게 지정
        dependent.setProperty(Key.DEP_GOVERNOR, this)
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$type${super.toString()}"

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is DepEdge -> this.type == other.type && super.equals(other)
            else -> false
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     *
     * * Whenever it is invoked on the same object more than once, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified.
     * * If two objects are equal according to the equals() method, then calling the hashCode method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = this.type.hashCode() + super.hashCode()

    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L
    }
}

/**
 * 의미역 구조 분석의 결과.
 *
 * ## 참고
 * **의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.
 * 예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
 * 동사 '먹었다'를 중심으로
 * * '나는'은 동작의 주체를,
 * * '밥을'은 동작의 대상을,
 * * '어제'는 동작의 시점을
 * * '집에서'는 동작의 장소를 나타냅니다.
 *
 * 아래를 참고해보세요.
 * * [kr.bydelta.koala.proc.CanLabelSemanticRole] 의미역 분석을 수행하는 interface.
 * * [Word.getArgumentRoles] 어절이 술어인 논항들의 [RoleEdge] 목록을 가져오는 API
 * * [Word.getPredicateRoles] 어절이 논항인 [RoleEdge]의 술어를 가져오는 API
 * * [Sentence.getRoles] 전체 문장을 분석한 의미역 구조 [RoleEdge]를 가져오는 API
 * * [RoleType] 의미역 분류를 갖는 Enum 값
 *
 * @since 2.0.0
 * @constructor 의미역 분석 결과를 생성합니다.
 * @param predicate 의미역 구조에서 표현하는 동사 [Word]
 * @param argument 의미역 구조에서 서술된 논항 [Word]
 * @param label 의미역 표지자, [RoleType] Enum 값
 * @param modifiers 논항의 수식어구들
 * @param originalLabel 원본 분석기의 표지자 String 값. 기본값은 null.
 */
class RoleEdge constructor(val predicate: Word,
                           val argument: Word,
                           label: RoleType,
                           val modifiers: List<Word> = emptyList(),
                           val originalLabel: String? = null) : DAGEdge<RoleType, Word>(predicate, argument, label) {
    init {
        // 동사가 논항을 찾을 수 있게 지정
        predicate.addProperty(Key.SRL_ARGUMENT, this)
        // 논항이 동사를 찾을 수 있게 지정
        argument.addProperty(Key.SRL_PREDICATE, this)
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "${label ?: ""}('${src?.surface
            ?: "ROOT"}' → '${dest.surface}/${modifiers.joinToString(" ") { it.surface }}')"

    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L
    }
}

/**
 * 형태소를 저장하는 [Property] class입니다.
 *
 * ## 참고
 * **형태소**는 의미를 가지는 요소로서는 더 이상 분석할 수 없는 가장 작은 말의 단위로 정의됩니다.
 *
 * **형태소 분석**은 문장을 형태소의 단위로 나누는 작업을 의미합니다.
 * 예) '문장을 형태소로 나눠봅시다'의 경우,
 * * 문장/일반명사, -을/조사,
 * * 형태소/일반명사, -로/조사,
 * * 나누-(다)/동사, -어-/어미, 보-(다)/동사, -ㅂ시다/어미
 * 로 대략 나눌 수 있습니다.
 *
 * 아래를 참고해보세요.
 * * [kr.bydelta.koala.proc.CanTag] 형태소 분석기의 최상위 Interface
 * * [POS] 형태소의 분류를 담은 Enum class
 *
 * @since 1.0.0
 * @constructor
 * @param surface 형태소 표면형 String
 * @param originalTag  원본 형태소 분석기의 품사 String
 * @param tag     세종 품사표기
 */
class Morpheme constructor(val surface: String, val tag: POS,
                           val originalTag: String? = null) : CanHaveProperty() {
    /****** Properties ******/

    /**
     * 형태소의 어절 내 위치입니다.
     *
     * ## 참고
     * [equals] 연산에 [id] 값은 포함되지 않으며, 한번 값이 설정된 경우 다시 설정 불가능합니다.
     * (재설정시 [AlreadySetIDException] 발생합니다)
     *
     * @since 1.x
     * @throws[AlreadySetIDException] 한번 ID 값이 설정된 이후 다시 ID를 설정하려는 경우
     * */
    var id: Int = -1
        @Throws(AlreadySetIDException::class) internal set(value) {
            if (field == -1) field = value
            else throw AlreadySetIDException()
        }

    /**
     * 이 형태소의 의미번호를 저장합니다.
     */
    fun setWordSense(id: Int) = setProperty(Key.WORD_SENSE, WordSense(id))

    /**
     * 다의어 분석 결과인, 이 형태소의 사전 속 의미/어깨번호 값을 돌려줍니다.
     *
     * 다의어 분석을 한 적이 없다면 null을 돌려줍니다.
     *
     * @since 2.0.0
     * @return 의미/어깨번호 값
     * */
    fun getWordSense(): Int? = getProperty<WordSense>(Key.WORD_SENSE)?.id

    /**
     * 개체명 분석을 했다면, 현재 형태소가 속한 개체명 값을 돌려줍니다.
     *
     * ## 참고
     * **개체명 인식**은 문장에서 인물, 장소, 기관, 대상 등을 인식하는 기술입니다.
     * 예) '철저한 진상 조사를 촉구하는 국제사회의 목소리가 커지고 있는 가운데, 트럼프 미국 대통령은 되레 사우디를 감싸고 나섰습니다.'에서, 다음을 인식하는 기술입니다.
     * * '트럼프': 인물
     * * '미국' : 국가
     * * '대통령' : 직위
     * * '사우디' : 국가
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanRecognizeEntity] 개체명 인식기 interface
     * * [Sentence.getEntities] 문장에 포함된 모든 [Entity]를 가져오는 API
     * * [Word.getEntities] 어절에 연관된 모든 [Entity]를 가져오는 API
     * * [Entity] 개체명을 저장하는 형태
     * * [CoarseEntityType] [Entity]의 대분류 개체명 분류구조 Enum 값
     *
     * @since 2.0.0
     * @return [Entity]의 목록입니다. 분석 결과가 없으면 null.
     * */
    fun getEntities() = getProperty<ListProperty<Entity>>(Key.NAMED_ENTITY)?.values

    /**
     * 이 형태소를 포함하는 단어를 돌려줍니다.
     */
    fun getWord() = getProperty<Word>(Key.WORD)

    /****** Tag checkers ******/

    /**
     * 체언(명사, 수사, 대명사) 형태소인지 확인합니다.
     *
     * @since 1.x
     * @return 체언이라면 true
     */
    fun isNoun() = tag.isNoun()

    /**
     * 용언(동사, 형용사) 형태소인지 확인합니다.
     *
     * @since 1.x
     * @return 용언이라면 true
     */
    fun isPredicate() = tag.isPredicate()

    /**
     * 수식언(관형사, 부사) 형태소인지 확인합니다.
     *
     * @since 1.x
     * @return 수식언이라면 true
     */
    fun isModifier() = tag.isModifier()

    /**
     * 관계언(조사) 형태소인지 확인합니다.
     *
     * @since 1.x
     * @return 관계언이라면 true
     */
    fun isJosa() = tag.isPostPosition()

    /**
     * 세종 품사 [tag]가 주어진 품사 표기 [partialTag] 묶음에 포함되는지 확인합니다.
     *
     * 예) "N"은 체언인지 확인하고, "NP"는 대명사인지 확인
     *
     * ## 단축명령
     * * 체언(명사, 수사, 대명사) [isNoun]
     * * 용언(동사, 형용사)는 [isPredicate]
     * * 수식언(관형사, 부사)는 [isModifier]
     * * 관계언(조사)는 [isJosa]
     *
     * ## 참고
     * * 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않습니다.
     * * 세종 품사표기는 [POS]를 참고하세요.
     * * 품사 표기는 [비교표](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
     * 에서 확인가능합니다.
     *
     * @since 1.x
     * @param[partialTag] 포함 여부를 확인할 상위 형태소 분류 품사표기
     * @return 포함되는 경우 true.
     */
    fun hasTag(partialTag: String): Boolean = this.tag.startsWith(partialTag)

    /**
     * 세종 품사 [tag]가 주어진 품사 표기들 [tags] 묶음들 중 하나에 포함되는지 확인합니다.
     *
     * 예) List("N", "MM")의 경우, 체언 또는 관형사인지 확인합니다.
     *
     * ## 단축명령
     * * 체언(명사, 수사, 대명사) [isNoun]
     * * 용언(동사, 형용사)는 [isPredicate]
     * * 수식언(관형사, 부사)는 [isModifier]
     * * 관계언(조사)는 [isJosa]
     *
     * ## 참고
     * * 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않습니다.
     * * 세종 품사표기는 [POS]를 참고하세요.
     * * 품사 표기는 [비교표](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
     * 에서 확인가능합니다.
     *
     * @since 1.x
     * @param[tags] 포함 여부를 확인할 상위 형태소 분류 품사표기들
     * @return 하나라도 포함되는 경우 true.
     */
    fun hasTagOneOf(vararg tags: String): Boolean = tags.any { it.contains(tag) }

    /**
     * 원본 품사 [originalTag]가 주어진 품사 표기 [partialTag] 묶음에 포함되는지 확인합니다.
     *
     * 지정된 원본 품사가 없으면 (즉, null이면) false를 반환합니다.
     *
     * ## 단축명령
     * * 체언(명사, 수사, 대명사) [isNoun]
     * * 용언(동사, 형용사)는 [isPredicate]
     * * 수식언(관형사, 부사)는 [isModifier]
     * * 관계언(조사)는 [isJosa]
     *
     * ## 참고
     * * 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않습니다.
     * * 각 분석기의 품사 표기는 [비교표](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
     * 에서 확인가능합니다.
     *
     * @since 1.x
     * @param[partialTag] 포함 여부를 확인할 상위 형태소 분류 품사표기들
     * @return 하나라도 포함되는 경우 true.
     */
    fun hasOriginalTag(partialTag: String): Boolean = originalTag?.toUpperCase()?.startsWith(partialTag.toUpperCase())
            ?: false

    /****** Equalities ******/
    /**
     * 표면형과 더불어, 형태소의 품사 표기도 같은지 확인합니다.
     *
     * ## 참고
     * [id]가 다르더라도 품사와 표면형이 같으면 같다고 판단합니다.
     *
     * @return 모두 같으면 true.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Morpheme -> other.surface == this.surface && other.tag == this.tag
            else -> false
        }
    }

    /**
     * 타 형태소 객체 [another]와 형태소의 표면형이 같은지 비교합니다.
     *
     * @since 1.x
     * @param[another] 표면형을 비교할 형태소
     * @return 표면형이 같으면 true
     */
    fun equalsWithoutTag(another: Morpheme): Boolean = another.surface == this.surface

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     */
    override fun hashCode(): Int = surface.hashCode() * POS_SIZE + tag.hashCode()

    /********* String representation *********/

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String =
            if (originalTag != null) "$surface/$tag($originalTag)"
            else "$surface/$tag"

    /********* Components *********/

    /**
     * [surface] 값을 첫 component로 반환합니다.
     *
     * ## Kotlin
     * 다음과 같이 사용할 수 있습니다.
     * ```
     * val (surface, tag) = Morpheme("String", POS.NNP)
     * // surface == "String"
     * // tag == POs.NNP
     * ```
     */
    operator fun component1() = surface

    /**
     * [tag] 값을 두번째 component로 반환합니다.
     *
     * ## Kotlin
     * 다음과 같이 사용할 수 있습니다.
     * ```
     * val (surface, tag) = Morpheme("String", POS.NNP)
     * // surface == "String"
     * // tag == POs.NNP
     * ```
     */
    operator fun component2() = tag

    /**
     * Static fields
     */
    companion object {
        /** Serialization Version ID: 2.0.0 by hexadecimal */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L
    }
}

/**
 * 어절을 표현하는 [Property] class입니다.
 *
 * @since 1.x
 * @constructor 어절을 구성합니다.
 * @throws[AlreadySetIDException] 초기화 과정에서 ID값을 각 [Morpheme]에 할당할 수 없으면,
 * 즉, 어떤 [Morpheme]이 이미 다른 Word 객체에 등록되었을 때 발생합니다.
 * @param surface   어절의 표면형 String.
 * @param morphemes 어절에 포함된 형태소의 목록: List<[Morpheme]>.
 */
class Word @Throws(AlreadySetIDException::class) constructor(val surface: String = "",
                                                             private val morphemes: List<Morpheme> = listOf()) :
        CanHaveProperty(), List<Morpheme> by morphemes {

    init {
        // ID 값 배정
        for ((index, value) in morphemes.withIndex()) {
            value.id = index
            value.setProperty(Key.WORD, this)
        }
    }

    /********** Access property values **********/

    /**
     * 어절의 문장 내 위치입니다.
     *
     * ## 참고
     * [equals] 연산에 [id] 값은 포함되지 않습니다.
     *
     * @since 1.x
     * */
    var id: Int = -1
        internal set(value) {
            field = value
        }

    /**
     * 개체명 분석을 했다면, 현재 어절이 속한 개체명 값을 돌려줍니다.
     *
     * ## 참고
     * **개체명 인식**은 문장에서 인물, 장소, 기관, 대상 등을 인식하는 기술입니다.
     * 예) '철저한 진상 조사를 촉구하는 국제사회의 목소리가 커지고 있는 가운데, 트럼프 미국 대통령은 되레 사우디를 감싸고 나섰습니다.'에서, 다음을 인식하는 기술입니다.
     * * '트럼프': 인물
     * * '미국' : 국가
     * * '대통령' : 직위
     * * '사우디' : 국가
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanRecognizeEntity] 개체명 인식기 interface
     * * [Morpheme.getEntities] 형태소를 포함하는 모든 [Entity]를 가져오는 API
     * * [Sentence.getEntities] 문장에 포함된 모든 [Entity]를 가져오는 API
     * * [Entity] 개체명을 저장하는 형태
     * * [CoarseEntityType] [Entity]의 대분류 개체명 분류구조 Enum 값
     *
     * @since 2.0.0
     * @return [Entity]의 목록입니다. 분석 결과가 없으면 null.
     * */
    fun getEntities() = this.mapNotNull { it.getEntities() }.flatten().toSet()

    /**
     * 구문분석을 했다면, 현재 어절이 속한 직속 상위 구구조(Phrase)를 돌려줍니다.
     *
     * ## 참고
     * **구문구조 분석**은 문장의 구성요소들(어절, 구, 절)이 이루는 문법적 구조를 분석하는 방법입니다.
     * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
     * 2개의 절이 있습니다
     * * 나는 밥을 먹었고
     * * 영희는 짐을 쌌다
     * 각 절은 3개의 구를 포함합니다
     * * 나는, 밥을, 영희는, 짐을: 체언구
     * * 먹었고, 쌌다: 용언구
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanParseSyntax] 구문구조 분석을 수행하는 interface.
     * * [Sentence.getSyntaxTree] 전체 문장을 분석한 [SyntaxTree]를 가져오는 API
     * * [SyntaxTree] 구문구조를 저장하는 형태
     * * [PhraseTag] 구구조의 형태 분류를 갖는 Enum 값
     *
     * @since 2.0.0
     * @return 어절의 상위 구구조 [SyntaxTree]. 분석 결과가 없으면 null
     * */
    fun getPhrase() = getProperty<SyntaxTree>(Key.SYNTAX_PARENT)

    /**
     * 의존구문분석을 했다면, 현재 어절이 지배소인 하위 의존구문 구조의 값을 돌려줍니다.
     *
     * ## 참고
     * **의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.
     * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
     * 가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,
     * * '먹었고'가 '쌌다'와 대등하게 연결되고
     * * '나는'은 '먹었고'의 주어로 기능하며
     * * '밥을'은 '먹었고'의 목적어로 기능합니다.
     * * '영희는'은 '쌌다'의 주어로 기능하고,
     * * '짐을'은 '쌌다'의 목적어로 기능합니다.
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanParseDependency] 의존구조 분석을 수행하는 interface.
     * * [Word.getGovernorEdge] 어절이 지배당하는 상위 의존구조 [DepEdge]를 가져오는 API
     * * [Sentence.getDependencies] 전체 문장을 분석한 의존구조 [DepEdge]의 목록을 가져오는 API
     * * [DepEdge] 의존구문구조의 저장형태
     * * [PhraseTag] 의존구조의 형태 분류를 갖는 Enum 값 (구구조 분류와 같음)
     * * [DependencyTag] 의존구조의 기능 분류를 갖는 Enum 값
     *
     * @since 2.0.0
     * @return 어절이 지배하는 의존구문구조 [DepEdge]의 목록. 분석 결과가 없으면 null
     * */
    fun getDependentEdges(): List<DepEdge>? = getProperty<ListProperty<DepEdge>>(Key.DEP_DEPENDENT)?.values

    /**
     * 의존구문분석을 했다면, 현재 어절이 의존소인 상위 의존구문 구조의 값을 돌려줍니다.
     *
     * ## 참고
     * **의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.
     * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
     * 가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,
     * * '먹었고'가 '쌌다'와 대등하게 연결되고
     * * '나는'은 '먹었고'의 주어로 기능하며
     * * '밥을'은 '먹었고'의 목적어로 기능합니다.
     * * '영희는'은 '쌌다'의 주어로 기능하고,
     * * '짐을'은 '쌌다'의 목적어로 기능합니다.
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanParseDependency] 의존구조 분석을 수행하는 interface.
     * * [Word.getDependentEdges] 어절이 직접 지배하는 하위 의존구조 [DepEdge]의 목록를 가져오는 API
     * * [Sentence.getDependencies] 전체 문장을 분석한 의존구조 [DepEdge]의 목록을 가져오는 API
     * * [DepEdge] 의존구문구조의 저장형태
     * * [PhraseTag] 의존구조의 형태 분류를 갖는 Enum 값 (구구조 분류와 같음)
     * * [DependencyTag] 의존구조의 기능 분류를 갖는 Enum 값
     *
     * @since 2.0.0
     * @return 어절이 지배당하는 의존구문구조 [DepEdge]. 분석 결과가 없으면 null.
     * */
    fun getGovernorEdge() = getProperty<DepEdge>(Key.DEP_GOVERNOR)

    /**
     * 의미역 분석을 했다면, 현재 어절이 논항인 상위 의미역 구조를 돌려줌.
     *
     * ## 참고
     * **의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.
     * 예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
     * 동사 '먹었다'를 중심으로
     * * '나는'은 동작의 주체를,
     * * '밥을'은 동작의 대상을,
     * * '어제'는 동작의 시점을
     * * '집에서'는 동작의 장소를 나타냅니다.
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanLabelSemanticRole] 의미역 분석을 수행하는 interface.
     * * [Word.getArgumentRoles] 어절이 술어인 논항들의 [RoleEdge] 목록을 가져오는 API
     * * [Sentence.getRoles] 전체 문장을 분석한 의미역 구조 [RoleEdge]를 가져오는 API
     * * [RoleEdge] 의미역 구조를 저장하는 형태
     * * [RoleType] 의미역 분류를 갖는 Enum 값
     *
     * @since 2.0.0
     * @return 어절이 논항인 상위 의미역 구조 [RoleEdge]. 분석 결과가 없으면 null.
     * */
    fun getPredicateRoles(): List<RoleEdge>? = getProperty<ListProperty<RoleEdge>>(Key.SRL_PREDICATE)?.values

    /**
     * 의미역 분석을 했다면, 현재 어절이 술어로 기능하는 하위 의미역 구조의 목록을 돌려줌.
     *
     * ## 참고
     * **의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.
     * 예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
     * 동사 '먹었다'를 중심으로
     * * '나는'은 동작의 주체를,
     * * '밥을'은 동작의 대상을,
     * * '어제'는 동작의 시점을
     * * '집에서'는 동작의 장소를 나타냅니다.
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanLabelSemanticRole] 의미역 분석을 수행하는 interface.
     * * [Word.getPredicateRoles] 어절이 논항인 [RoleEdge]의 술어를 가져오는 API
     * * [Sentence.getRoles] 전체 문장을 분석한 의미역 구조 [RoleEdge]를 가져오는 API
     * * [RoleEdge] 의미역 구조를 저장하는 형태
     * * [RoleType] 의미역 분류를 갖는 Enum 값
     *
     * @since 2.0.0
     * @return 어절이 술어로 기능하는 하위 의미역 구조 [RoleEdge]의 목록. 분석 결과가 없으면 null.
     * */
    fun getArgumentRoles(): List<RoleEdge>? = getProperty<ListProperty<RoleEdge>>(Key.SRL_ARGUMENT)?.values

    /********** Equalities **********/

    /**
     * 표면형과 더불어, 포함된 형태소의 배열 순서가 같은지 확인합니다. (reference 기준의 동일함 판단이 아닙니다)
     *
     * ## 참고
     * * [id]가 다르더라도 형태소와 표면형이 같으면 같다고 판단합니다.
     * * 형태소 역시 품사와 표면형으로 판단하며, reference 기준으로 판단하지 않습니다. ([Morpheme.equals] 참고)
     *
     * @return 모두 같으면 true.
     */
    override fun equals(other: Any?): Boolean =
            when (other) {
                is Word -> this.surface == other.surface &&
                        this.size == other.size && this.zip(other).all { it.first == it.second }
                else -> false
            }

    /**
     * [another] 어절과 표면형이 같은지 비교합니다.
     *
     * @since 1.x
     * @param[another] 표면형을 비교할 다른 어절
     * @return 표면형이 같으면 true
     */
    fun equalsWithoutTag(another: Word): Boolean = another.surface == this.surface

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     */
    override fun hashCode(): Int =
            (this.surface.hashCode() + morphemes.sumBy { it.hashCode() }) % Int.MAX_VALUE

    /********** String representation **********/

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$surface = ${singleLineString()}"

    /**
     * 품사분석 결과를, 1행짜리 String으로 변환합니다.
     *
     * 예) '나/NP+는/JX'
     *
     * ## 참고
     * * 세종 품사표기는 [POS]를 참고하세요.
     *
     * @return 각 형태소별로 "표면형/품사" 형태로 기록하고 이를 +로 이어붙인 문자열.
     */
    fun singleLineString(): String =
            this.joinToString(separator = "+") { "${it.surface}/${it.tag}" }

    /** Static fields */
    companion object {
        /** Serialization version ID: Ver 2.0.0 (in hexadecimal)*/
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid = 0x200L
    }
}

/**
 * 문장을 표현하는 [Property] class입니다.
 *
 * @since 1.x
 * @constructor 문장을 구성합니다.
 * @param words 문장에 포함되는 어절 [Word]의 목록
 */
class Sentence(private val words: List<Word>) : CanHaveProperty(), List<Word> by words {
    init {
        // ID 값 배정
        for ((index, value) in words.withIndex()) {
            value.id = index
        }
    }

    /********** Access property values **********/

    /** 최상위 구구조를 지정합니다 */
    fun setSyntaxTree(tree: SyntaxTree) = setProperty(Key.SYNTAX_PARENT, tree)

    /** 의존구문분석 결과인 edge들을 저장합니다. */
    fun setDepEdges(edges: List<DepEdge>) = setProperty(Key.DEP_DEPENDENT, ListProperty(edges.toMutableList()))

    /** 의미역 분석 결과인 edge들을 저장합니다. */
    fun setRoleEdges(edges: List<RoleEdge>) = setProperty(Key.SRL_ARGUMENT, ListProperty(edges.toMutableList()))

    /** 개체명 분석 결과로 얻은 개체명들을 저장합니다. */
    fun setEntities(entities: List<Entity>) = setProperty(Key.NAMED_ENTITY, ListProperty(entities.toMutableList()))

    /** 상호참조 분석/대용어 분석 결과로 얻은 개체명의 묶음들을 저장합니다. */
    fun setCorefGroups(groups: List<CoreferenceGroup>) = setProperty(Key.COREF_MENTION, ListProperty(groups.toMutableList()))

    /**
     * 구문분석을 했다면, 최상위 구구조(Phrase)를 돌려줍니다.
     *
     * ## 참고
     * **구문구조 분석**은 문장의 구성요소들(어절, 구, 절)이 이루는 문법적 구조를 분석하는 방법입니다.
     * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
     * 2개의 절이 있습니다
     * * 나는 밥을 먹었고
     * * 영희는 짐을 쌌다
     * 각 절은 3개의 구를 포함합니다
     * * 나는, 밥을, 영희는, 짐을: 체언구
     * * 먹었고, 쌌다: 용언구
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanParseSyntax] 구문구조 분석을 수행하는 interface.
     * * [Word.getPhrase] 어절의 직속 상위 [SyntaxTree]를 가져오는 API
     * * [SyntaxTree] 구문구조를 저장하는 형태
     * * [PhraseTag] 구구조의 형태 분류를 갖는 Enum 값
     *
     * @since 2.0.0
     * @return 최상위 구구조 [SyntaxTree]. 분석 결과가 없으면 null.
     * */
    fun getSyntaxTree() = getProperty<SyntaxTree>(Key.SYNTAX_PARENT)

    /**
     * 의존구문분석을 했다면, 문장에 포함된 모든 의존구조의 목록을 돌려줍니다.
     *
     * ## 참고
     * **의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.
     * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
     * 가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,
     * * '먹었고'가 '쌌다'와 대등하게 연결되고
     * * '나는'은 '먹었고'의 주어로 기능하며
     * * '밥을'은 '먹었고'의 목적어로 기능합니다.
     * * '영희는'은 '쌌다'의 주어로 기능하고,
     * * '짐을'은 '쌌다'의 목적어로 기능합니다.
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanParseDependency] 의존구조 분석을 수행하는 interface.
     * * [Word.getDependentEdges] 어절이 직접 지배하는 하위 의존구조 [DepEdge]의 목록을 가져오는 API
     * * [Word.getGovernorEdge] 어절이 지배당하는 상위 의존구조 [DepEdge]를 가져오는 API
     * * [DepEdge] 의존구문구조의 저장형태
     * * [PhraseTag] 의존구조의 형태 분류를 갖는 Enum 값 (구구조 분류와 같음)
     * * [DependencyTag] 의존구조의 기능 분류를 갖는 Enum 값
     *
     * @since 2.0.0
     * @return 문장 내 모든 의존구문구조 [DepEdge]의 목록. 분석 결과가 없으면 null.
     * */
    fun getDependencies(): List<DepEdge>? = getProperty<ListProperty<DepEdge>>(Key.DEP_DEPENDENT)?.values

    /**
     * 의미역 분석을 했다면, 문장에 포함된 의미역 구조의 목록을 돌려줌.
     *
     * ## 참고
     * **의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.
     * 예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
     * 동사 '먹었다'를 중심으로
     * * '나는'은 동작의 주체를,
     * * '밥을'은 동작의 대상을,
     * * '어제'는 동작의 시점을
     * * '집에서'는 동작의 장소를 나타냅니다.
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanLabelSemanticRole] 의미역 분석을 수행하는 interface.
     * * [Word.getArgumentRoles] 어절이 술어인 논항들의 [RoleEdge] 목록을 가져오는 API
     * * [Word.getPredicateRoles] 어절이 논항인 [RoleEdge]의 술어를 가져오는 API
     * * [RoleEdge] 의미역 구조를 저장하는 형태
     * * [RoleType] 의미역 분류를 갖는 Enum 값
     *
     * @since 2.0.0
     * @return 문장 내 모든 의미역 구조 [RoleEdge]
     * */
    fun getRoles(): List<RoleEdge>? = getProperty<ListProperty<RoleEdge>>(Key.SRL_ARGUMENT)?.values

    /**
     * 개체명 분석을 했다면, 문장의 모든 개체명 목록을 돌려줍니다.
     *
     * ## 참고
     * **개체명 인식**은 문장에서 인물, 장소, 기관, 대상 등을 인식하는 기술입니다.
     * 예) '철저한 진상 조사를 촉구하는 국제사회의 목소리가 커지고 있는 가운데, 트럼프 미국 대통령은 되레 사우디를 감싸고 나섰습니다.'에서, 다음을 인식하는 기술입니다.
     * * '트럼프': 인물
     * * '미국' : 국가
     * * '대통령' : 직위
     * * '사우디' : 국가
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanRecognizeEntity] 개체명 인식기 interface
     * * [Word.getEntities] 해당 어절을 포함하는 [Entity]를 가져오는 API
     * * [Entity] 개체명을 저장하는 형태
     * * [CoarseEntityType] [Entity]의 대분류 개체명 분류구조 Enum 값
     *
     * @since 2.0.0
     * @return 문장에 포함된 모든 [Entity]의 목록입니다.
     * */
    fun getEntities(): List<Entity>? = getProperty<ListProperty<Entity>>(Key.NAMED_ENTITY)?.values


    /**
     * 문장 내에 포함된 공통 지시어 또는 대용어들의 묶음을 제공합니다.
     *
     * ## 참고
     * **공지시어 해소**는 문장 내 또는 문장 간에 같은 대상을 지칭하는 어구를 찾아 묶는 분석과정입니다.
     * 예) '삼성그룹의 계열사인 삼성물산은 같은 그룹의 계열사인 삼성생명과 함께'라는 문장에서
     * * '삼성그룹'과 '같은 그룹'을 찾아 묶는 것을 말합니다.
     *
     * **영형대용어 분석**은 문장에서 생략된 기능어를 찾아 문장 내 또는 문장 간에 언급되어 있는 어구와 묶는 분석과정입니다.
     * 예) '나는 밥을 먹었고, 영희도 먹었다'라는 문장에서,
     * * '먹었다'의 목적어인 '밥을'이 생략되어 있음을 찾는 것을 말합니다.
     *
     * 아래를 참고해보세요.
     * * [kr.bydelta.koala.proc.CanResolveCoref] 공지시어 해소, 대용어 분석기 interface
     * * [Entity.getCorefGroup] 개체명과 동일한 대상을 지칭하는 대상들의 묶음인 [CoreferenceGroup]을 반환하는 API
     * * [CoreferenceGroup] 동일한 대상을 지칭하는 개체명을 묶는 API
     *
     * @return 공통된 대상을 묶은 [CoreferenceGroup]의 목록. 분석 결과가 없으면 null.
     */
    fun getCorefGroups(): List<CoreferenceGroup>? = getProperty<ListProperty<CoreferenceGroup>>(Key.COREF_MENTION)?.values

    /********** Filter words by specific types **********/

    /**
     * 체언(명사, 수사, 대명사) 및 체언 성격의 어휘를 포함하는 어절들을 가져옵니다.
     * - 포함: 체언, 명사형 전성어미 [POS.ETN], 명사 파생 접미사 [POS.XSN]
     * - 제외: 관형형 전성어미 [POS.ETM], 동사/형용사/부사 파생 접미사 [POS.XSV], [POS.XSA], [POS.XSM]
     * - 가장 마지막에 적용되는 어미/접미사를 기준으로 판정함
     *
     * ## 참고
     * **전성어미**는 용언 따위에 붙어 다른 품사의 기능을 수행하도록 변경하는 어미입니다.
     * 예) '멋지게 살다'를 '멋지게 삶'으로 바꾸는 명사형 전성어미 '-ㅁ'이 있습니다. 원 기능은 동사이므로 부사의 수식을 받고 있습니다.
     *
     * **파생접미사**는 용언의 어근이나 단어 따위에 붙어서 명사로 파생되도록 하는 접미사입니다.
     * 예) 역시 '살다'를 '삶'으로 바꾸는 명사파생 접미사 '-ㅁ'이 있습니다. 이 경우 명사이므로 '멋진 삶'과 같이 형용사의 수식을 받습니다.
     *
     * @return 체언 또는 체언 성격의 어휘를 포함하는 어절의 목록
     */
    fun getNouns(): List<Word> = words.filter { w ->
        val inclusion = w.indexOfLast { it.isNoun() || it.hasTagOneOf("ETN", "XSN") }
        val exclusion = w.indexOfLast { it.hasTagOneOf("XSV", "XSA", "XSM") }
        inclusion != -1 && inclusion > exclusion
    }

    /**
     * 용언(동사, 형용사) 및 용언 성격의 어휘를 포함하는 어절들을 가져옵니다.
     * - 포함: 용언, 동사 파생 접미사 [POS.XSV]
     * - 제외: 명사형/관형형 전성어미 [POS.ETN], [POS.ETM], 명사/형용사/부사 파생 접미사 [POS.XSN], [POS.XSA], [POS.XSM]
     * - 가장 마지막에 적용되는 어미/접미사를 기준으로 판정함
     *
     * ## 참고
     * **전성어미**는 용언 따위에 붙어 다른 품사의 기능을 수행하도록 변경하는 어미입니다.
     * 예) '멋지게 살다'를 '멋지게 삶'으로 바꾸는 명사형 전성어미 '-ㅁ'이 있습니다. 원 기능은 동사이므로 부사의 수식을 받고 있습니다.
     *
     * **파생접미사**는 용언의 어근이나 단어 따위에 붙어서 명사로 파생되도록 하는 접미사입니다.
     * 예) 역시 '살다'를 '삶'으로 바꾸는 명사파생 접미사 '-ㅁ'이 있습니다. 이 경우 명사이므로 '멋진 삶'과 같이 형용사의 수식을 받습니다.
     *
     * @return 용언 또는 용언 성격의 어휘를 포함하는 어절의 목록
     */
    fun getVerbs(): List<Word> = words.filter { w ->
        val inclusion = w.indexOfLast { it.isPredicate() || it.tag == POS.XSV }
        val exclusion = w.indexOfLast { it.hasTagOneOf("ETN", "ETM", "XSN", "XSA", "XSM") }
        inclusion != -1 && inclusion > exclusion
    }

    /**
     * 수식언(관형사, 부사) 및 수식언 성격의 어휘를 포함하는 어절들을 가져옵니다.
     * - 포함: 수식언, 관형형 전성어미 [POS.ETM], 형용사/부사 파생 접미사 [POS.XSA], [POS.XSM]
     * - 제외: 명사형 전성어미 [POS.ETN], 명사/동사 파생 접미사 [POS.XSN], [POS.XSV]
     * - 가장 마지막에 적용되는 어미/접미사를 기준으로 판정함
     *
     * ## 참고
     * **전성어미**는 용언 따위에 붙어 다른 품사의 기능을 수행하도록 변경하는 어미입니다.
     * 예) '멋지게 살다'를 '멋지게 삶'으로 바꾸는 명사형 전성어미 '-ㅁ'이 있습니다. 원 기능은 동사이므로 부사의 수식을 받고 있습니다.
     *
     * **파생접미사**는 용언의 어근이나 단어 따위에 붙어서 명사로 파생되도록 하는 접미사입니다.
     * 예) 역시 '살다'를 '삶'으로 바꾸는 명사파생 접미사 '-ㅁ'이 있습니다. 이 경우 명사이므로 '멋진 삶'과 같이 형용사의 수식을 받습니다.
     *
     * @return 수식언 또는 수식언 성격의 어휘를 포함하는 어절의 목록
     */
    fun getModifiers(): List<Word> = words.filter { w ->
        val inclusion = w.indexOfLast { it.isModifier() || it.hasTagOneOf("ETM", "XSA", "XSM") }
        val exclusion = w.indexOfLast { it.hasTagOneOf("ETN", "XSN", "XSV") }
        inclusion != -1 && inclusion > exclusion
    }
    /********** Equalities **********/

    /**
     * 표면형과 더불어, 포함된 어절과 형태소의 배열 순서가 같은지 확인합니다. (reference 기준의 동일함 판단이 아닙니다)
     *
     * ## 참고
     * * [Word.id]나 [Morpheme.id]가 다르더라도 형태소와 표면형이 같으면 같다고 판단합니다.
     * * 세부 동작은 [Word.equals]와 [Morpheme.equals] 참고
     *
     * @return 모두 같으면 true.
     */
    override fun equals(other: Any?): Boolean =
            when (other) {
                is Sentence -> this.size == other.size && this.zip(other).all { it.first == it.second }
                else -> false
            }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     */
    override fun hashCode(): Int = words.sumBy { it.hashCode() } % Int.MAX_VALUE

    /********** String representation **********/

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = surfaceString()

    /**
     * 어절의 표면형을 이어붙이되, 지정된 [delimiter]로 띄어쓰기 된 문장을 반환합니다.
     *
     * @param delimiter 어절 사이의 띄어쓰기 방식. 기본값 = 공백(" ")
     * @return 띄어쓰기 된 문장입니다.
     */
    @JvmOverloads
    fun surfaceString(delimiter: String = " "): String =
            words.joinToString(separator = delimiter) { it.surface }

    /**
     * 품사분석 결과를, 1행짜리 String으로 변환합니다.
     *
     * @see [Word.singleLineString]
     * @return 품사분석 결과를 담은 1행짜리 String.
     */
    fun singleLineString(): String =
            words.joinToString(separator = " ") { it.singleLineString() }

    /** Static fields */
    companion object {
        /** Serialization version ID: Ver 2.0.0 (in hexadecimal)*/
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid = 0x200L

        /** 빈 문장입니다. */
        @JvmStatic
        val empty: Sentence by lazy { Sentence(listOf()) }
    }
}