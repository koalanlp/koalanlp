package kr.bydelta.koala

import kr.bydelta.koala.Entity.Companion.equals
import kr.bydelta.koala.ListProperty.Companion.equals
import kr.bydelta.koala.Morpheme.Companion.equals
import kr.bydelta.koala.SyntaxTree.Companion.equals
import kr.bydelta.koala.Word.Companion.equals
import java.io.Serializable
import java.lang.Exception

internal const val KEY_WORDSENSE: Byte = 0
internal const val KEY_NER: Byte = 10
internal const val KEY_SYNTAXTREE: Byte = 20
internal const val KEY_DEPTREE: Byte = 21
internal const val KEY_SRL: Byte = 22
internal const val KEY_CHILD: Byte = 30
internal const val KEY_PARENT: Byte = 31

/**
 * ID가 이미 설정되었을 때 발생하는 Exception
 */
class AlreadySetIDException : Exception("The ID value was already initialized!")

/**
 * 텍스트 분석 과정에서 얻어지는 여러가지 값들을 표현하는 class
 */
abstract class Property : Serializable {
    /**
     * 가질 수 있는 속성값을 저장할 [MutableMap]
     */
    protected val properties: MutableMap<Byte, Property> by lazy {
        mutableMapOf<Byte, Property>()
    }

    /**
     * [key] 속성에 해당하는 [T]-type 값 [value]를 저장합니다.
     *
     * 만약, 이전에 저장을 한 적이 있다면, 덮어쓰기 하지 않고 [IllegalStateException]를 발생합니다.
     */
    @Throws(IllegalStateException::class)
    internal fun <T : Property> setProperty(key: Byte, value: T) {
        if (key in properties)
            throw IllegalStateException("There are more than two properties belong to the same key: $key")
        properties[key] = value
    }

    /**
     * (테스트 지원용)
     * property를 삭제합니다.
     */
    internal fun removeProperty(key: Byte) {
        properties.remove(key)
    }

    /**
     * [property]의 타입에 맞는 키 값을 선택합니다.
     */
    private tailrec fun <T : Property> getPropertyKey(property: T): Byte {
        return when (property) {
            is SyntaxTree -> KEY_SYNTAXTREE
            is DepTree -> KEY_DEPTREE
            is RoleTree -> KEY_SRL
            is Entity -> KEY_NER
            is IntProperty -> KEY_WORDSENSE
            is ListProperty<*> ->
                if (property.isNotEmpty()) getPropertyKey(property[0])
                else KEY_CHILD
            else -> throw IllegalArgumentException("Argument ${property::class} is not a proper property!")
        }
    }

    /**
     * [T]-type 값 [property]를 저장합니다.
     *
     * 만약, 이전에 저장을 한 적이 있다면, 덮어쓰기 하지 않고 [IllegalStateException]를 발생시킵니다.
     * 적당한 키를 찾을 수 없다면, [IllegalArgumentException]를 발생시킵니다.
     */
    @Throws(IllegalStateException::class, IllegalArgumentException::class)
    fun <T : Property> setProperty(property: T) {
        val key = getPropertyKey(property)
        setProperty(key, property)
    }

    /**
     * [T]-type 값들을 가변변수로 하여 [properties]로 받은 뒤, 저장합니다.
     *
     * 만약, 이전에 저장을 한 적이 있다면, 덮어쓰기 하지 않고 [IllegalStateException]를 발생합니다.
     */
    @Throws(IllegalStateException::class)
    fun <T : Property> setListProperty(vararg properties: T) {
        val listProp = ListProperty(properties.asList())
        val key = getPropertyKey(listProp)
        setProperty(key, listProp)
    }

    /**
     * [key]로 지정된 [T]-type의 property를 가져옵니다.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Property> getProperty(key: Byte): T? = properties[key] as? T
}

/**
 * 정수형 값을 저장할 Property class
 *
 * @param value 저장될 정수 값
 */
data class IntProperty(val value: Int) : Property() {
    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L // Version 2.0.0
    }
}

/**
 * 개체명 분석 결과를 저장할 Property class
 *
 * @param type 개체명 대분류 값 (CoarseEntityType)
 * @param fineType 개체명 세분류 값 (String)
 * @param words 개체명을 이루는 어절의 목록
 */
class Entity(val type: CoarseEntityType,
             val fineType: String,
             val words: List<Word>) : Property(), List<Word> by words {
    init {
        // 세부 개체명은 대분류 값에 소속되어야 합니다.
        assert(fineType.toUpperCase().startsWith(type.toString()))

        // 각 어절에 Entity로 현재 대상을 지정합니다.
        for (word in words) {
            word.linkEntity(this)
        }
    }

    /**
     * 개체명의 표면형
     */
    val surface: String by lazy { words.joinToString(separator = " ") { it.surface } }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$type($fineType; '$surface')"

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Entity -> this.type == other.type && this.size == other.size &&
                    this.fineType == other.fineType && this.zip(other).all { it.first == it.second }
            else -> false
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     *
     * * Whenever it is invoked on the same object more than once, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified.
     * * If two objects are equal according to the equals() method, then calling the hashCode method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = this.type.hashCode() + this.fineType.hashCode() + this.sumBy { it.hashCode() }

    /** Static Fields */
    companion object {
        /** Serialization Version ID: ver 2.0.0 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x200L
    }
}

/**
 * 목록형 값을 저장할 Property.
 *
 * @param children 저장할 [T]-type 값의 목록
 */
class ListProperty<T : Property>(val children: List<T>) : Property(), List<T> by children {
    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is ListProperty<*> -> this.children.size == other.children.size &&
                    this.zip(other).all { it.first == it.second }
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
        private val serialVersionUid: Long = 0x200L // Version 2.0.0
    }
}

/**
 * [T]-type들의 트리 또는 DAG 구조를 저장할 Property
 *
 * @param leaf 트리/DAG의 노드에서 연결되는 word
 * @param children 트리/DAG의 자식 노드들
 */
open class Tree<T : Tree<T>>(val leaf: Word?,
                             val children: List<T>) : Property(), List<T> by children {
    init {
        // 부모 노드로 자신을 지정하게 함
        for (child in children) {
            child.setProperty(KEY_PARENT, this)
        }
    }

    /**
     * 부모 노드를 반환함. 부모 노드가 초기화되지 않은 경우 null.
     */
    fun getParent() = getProperty<T>(KEY_PARENT)

    /**
     * 이 노드가 최상위 노드인지 확인함.
     */
    fun isRoot() = getParent() == null

    /**
     * 이 노드가 (leaf node를 제외하고) 자식 노드를 갖는지 확인.
     */
    fun hasNonTerminals() = children.isNotEmpty()

    /**
     * 이 노드의 자식노드들에 속하는 leaf node들을 모음.
     *
     * 보통 이 구문구조에 속하는 어절의 모음임.
     */
    fun getTerminals(): List<Word> {
        val leaves = children.flatMap { it.getTerminals() }
        return (if (leaf != null) leaves.plusElement(leaf) else leaves).sortedBy { it.id }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Tree<*> -> {
                val children1 = this.children
                val children2 = other.children
                this.leaf == other.leaf && children1.size == children2.size &&
                        children1.zip(children2).all { it.first == it.second }
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
    override fun hashCode(): Int = (this.leaf?.hashCode() ?: 0) + this.children.sumBy { it.hashCode() }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "Node(${leaf?.toString() ?: ""})"

    /**
     * 이 트리구조를 표현하는 텍스트 표현을 [buffer]에 담아 반환함. [depth]는 들여쓰기의 수준임.
     */
    @JvmOverloads
    fun getTreeString(depth: Int = 0,
                      buffer: StringBuffer = StringBuffer()): StringBuffer {
        buffer.append("| ".repeat(depth))
        buffer.append(toString())

        for (child in children) {
            buffer.append('\n')
            child.getTreeString(depth + 1, buffer)
        }

        return buffer
    }
}

/**
 * 구문구조 분석의 결과.
 *
 * @param type 구구조 표지자
 * @param node 현재 구구조에 직접 속하는 어절. 중간 구문구조인 경우 null.
 * @param children 현재 구구조에 속하는 하위 구구조. 하위 구구조가 없다면 빈 리스트.
 */
class SyntaxTree constructor(val type: PhraseTag,
                             node: Word? = null,
                             children: List<SyntaxTree> = emptyList()) : Tree<SyntaxTree>(node, children) {
    init {
        // 어절에 구구조 지정
        node?.setProperty(KEY_SYNTAXTREE, this)
    }

    /**
     * 구문구조 분석의 결과.
     *
     * @param type 구구조 표지자
     * @param node 현재 구구조에 직접 속하는 어절. 중간 구문구조인 경우 null.
     * @param children 현재 구구조에 속하는 하위 구구조
     */
    constructor(type: PhraseTag, node: Word?, vararg children: SyntaxTree) : this(type, node, children.asList())

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$type-${super.toString()}"

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is SyntaxTree -> this.type == other.type && super.equals(other)
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
 * 의존구문구조 분석의 결과.
 *
 * @param head 현재 의존구조의 지배소
 * @param type 구구조 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)
 * @param depType 의존기능 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)
 * @param children 지배를 받는 다른 의존구조
 */
class DepTree constructor(val head: Word,
                          val type: PhraseTag,
                          val depType: DependencyTag? = null,
                          children: List<DepTree> = emptyList()) : Tree<DepTree>(head, children) {
    init {
        // 어절에 의존구조 지정
        head.setProperty(KEY_DEPTREE, this)
    }

    /**
     * 의존구문구조 분석의 결과.
     *
     * @param head 현재 의존구조의 지배소
     * @param type 구구조 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)
     * @param depType 의존기능 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)
     * @param children 지배를 받는 다른 의존구조
     */
    constructor(head: Word, type: PhraseTag, depType: DependencyTag?, vararg children: DepTree) :
            this(head, type, depType, children.asList())

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$type${depType?.toString()?.let { "-$it" } ?: ""}-${super.toString()}"

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is DepTree -> this.type == other.type && this.depType == other.depType && super.equals(other)
            else -> false
        }
    }

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     *
     * * Whenever it is invoked on the same object more than once, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified.
     * * If two objects are equal according to the equals() method, then calling the hashCode method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int = this.type.hashCode() + (this.depType?.hashCode() ?: 0) + super.hashCode()

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
 * @param node 의미역 구조를 표현하는 지배소
 * @param type 의미역 표지자
 * @param children 지배를 받는 다른 의미역 구조
 */
class RoleTree constructor(node: Word, val type: RoleType,
                           children: List<RoleTree> = emptyList()) : Tree<RoleTree>(node, children) {
    init {
        // 어절에 의미역 구조 지정
        node.setProperty(KEY_SRL, this)
    }

    /**
     * 의미역 구조 분석의 결과.
     *
     * @param node 의미역 구조를 표현하는 지배소
     * @param type 의미역 표지자
     * @param children 지배를 받는 다른 의미역 구조
     */
    constructor(node: Word, type: RoleType, vararg children: RoleTree) :
            this(node, type, children.asList())

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "$type-${super.toString()}"

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is RoleTree -> this.type == other.type && super.equals(other)
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
 * 형태소 class
 *
 * @param surface 형태소 표면형 String
 * @param originalTag  원본 형태소 분석기의 품사 String
 * @param tag     세종 품사표기
 */
class Morpheme internal constructor(val surface: String, val tag: POS,
                                    val originalTag: String? = null) : Property() {
    /****** Properties ******/

    /** 형태소의 어절 내 위치. [equals] 연산에 [id] 값은 포함되지 않으며,
     * 한번 값이 설정된 경우 다시 설정 불가능 (재설정시 [AlreadySetIDException] 발생). */
    var id: Int = -1
        @Throws(AlreadySetIDException::class) internal set(value) {
            if (field == -1) field = value
            else throw AlreadySetIDException()
        }

    /** 다의어 분석 결과인, 이 형태소의 사전 속 어깨번호 값을 돌려줌.
     * 다의어 분석을 한 적이 없다면 [UninitializedPropertyAccessException]이 발생함. */
    @Throws(UninitializedPropertyAccessException::class)
    fun getWordSense(): Int = getProperty<IntProperty>(KEY_WORDSENSE)?.value
            ?: throw UninitializedPropertyAccessException()

    /****** Tag checkers ******/

    /**
     * 체언(명사, 수사, 대명사) 형태소인지 확인.
     */
    fun isNoun() = tag.isNoun()

    /**
     * 용언(동사, 형용사) 형태소인지 확인.
     */
    fun isPredicate() = tag.isPredicate()

    /**
     * 수식언(관형사, 부사) 형태소인지 확인.
     */
    fun isModifier() = tag.isModifier()

    /**
     * 관계언(조사) 형태소인지 확인.
     */
    fun isJosa() = tag.isPostPosition()

    /**
     * 세종 품사 [tag]가 주어진 품사 표기 [partialTag] 묶음에 포함되는지 확인.
     *
     * 예를 들어, N은 체언인지 확인하고, NP는 대명사인지 확인함.
     *
     * 참고: 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않음
     *
     * 품사 표기는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
     * 에서 확인
     */
    fun hasTag(partialTag: String): Boolean = this.tag.startsWith(partialTag)

    /**
     * 세종 품사 [tag]가 주어진 품사 표기들 [tags] 묶음들 중 하나에 포함되는지 확인.
     *
     * 예를 들어, (N, MM)의 경우, 체언 또는 관형사인지 확인.
     *
     * 참고: 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않음
     *
     * 품사 표기는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
     * 에서 확인
     */
    fun hasTagOneOf(vararg tags: String): Boolean = tags.any { it.contains(tag) }

    /**
     * 원본 품사 [originalTag]가 주어진 품사 표기 [partialTag] 묶음에 포함되는지 확인. 원본 품사가 없으면 false.
     *
     * 각 분석기별 품사 표기는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
     * 에서 확인
     */
    fun hasOriginalTag(partialTag: String): Boolean = originalTag?.toUpperCase()?.startsWith(partialTag.toUpperCase())
            ?: false

    /****** Equalities ******/
    /**
     * Indicates whether some other object is "equal to" this one (without [id]).
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Morpheme -> other.surface == this.surface && other.tag == this.tag
            else -> false
        }
    }

    /**
     * 타 형태소 객체 [another]와 형태소의 표면형이 같은지 비교함.
     */
    fun equalsWithoutTag(another: Morpheme): Boolean = another.surface == this.surface

    /**
     * Returns a hash code value for the object.  The general contract of hashCode is:
     */
    override fun hashCode(): Int = (POS_SIZE + surface.hashCode()) * POS_SIZE + tag.hashCode()

    /********* String representation *********/

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String =
            if (originalTag != null) "$surface/$tag($originalTag)"
            else "$surface/$tag"

    /********* Components *********/

    /**
     * [surface] 값을 첫 component로 반환함.
     */
    operator fun component1() = surface

    /**
     * [tag] 값을 두번째 component로 반환함.
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
 * 어절 Class.
 *
 * 초기화 과정에서 ID값을 각 [Morpheme]에 할당할 수 없으면, [AlreadySetIDException]이 발생함.
 *
 * @param surface   어절의 표면형 String.
 * @param morphemes 어절에 포함된 형태소의 목록: List<[Morpheme]>.
 */
class Word @Throws(AlreadySetIDException::class) internal constructor(val surface: String = "",
                                                                      private val morphemes: List<Morpheme> = listOf()) :
        Property(), List<Morpheme> by morphemes {

    init {
        // ID 값 배정
        for ((index, value) in morphemes.withIndex()) {
            value.id = index
        }
    }

    /********** Access property values **********/

    /** 어절의 문장 내 위치. [equals] 연산에 [id] 값은 포함되지 않음. */
    var id: Int = -1
        internal set(value) {
            field = value
        }

    /**
     * 개체명 분석 결과 [entity]를 등록합니다.
     */
    internal fun linkEntity(entity: Entity) {
        if (KEY_NER in properties) {
            val entities = getEntities()
            if (entities is MutableList<Entity>) {
                entities.add(entity)
            } else {
                val mutableEntities = entities.toMutableList()
                mutableEntities.add(entity)

                val mutableListProperty = ListProperty(mutableEntities)
                properties[KEY_NER] = mutableListProperty
            }
        } else {
            properties[KEY_NER] = ListProperty(mutableListOf(entity))
        }
    }

    /** 개체명 분석을 했다면, 현재 어절이 속한 개체명 값을 돌려줌.
     * 분석을 하지 않은 경우 [UninitializedPropertyAccessException] 발생 */
    @Throws(UninitializedPropertyAccessException::class)
    fun getEntities() =
            getProperty<ListProperty<Entity>>(KEY_NER)?.children ?: throw UninitializedPropertyAccessException()

    /** 구문분석을 했다면, 현재 어절이 속한 직속 상위 구구조(Phrase)를 돌려줌.
     * 분석을 하지 않은 경우 [UninitializedPropertyAccessException] 발생 */
    @Throws(UninitializedPropertyAccessException::class)
    fun getPhrase() = getProperty<SyntaxTree>(KEY_SYNTAXTREE) ?: throw UninitializedPropertyAccessException()

    /** 의존구문분석을 했다면, 현재 어절이 지배소인 의존구문 구조의 값을 돌려줌.
     * 분석을 하지 않은 경우 [UninitializedPropertyAccessException] 발생 */
    @Throws(UninitializedPropertyAccessException::class)
    fun getDependency() = getProperty<DepTree>(KEY_DEPTREE) ?: throw UninitializedPropertyAccessException()

    /** 의미역 분석을 했다면, 현재 어절이 지배하는 의미역 구조를 돌려줌.
     * 분석을 하지 않았거나, 의미역 구조에 포함되지 않는 경우 [UninitializedPropertyAccessException] 발생 */
    @Throws(UninitializedPropertyAccessException::class)
    fun getRole() = getProperty<RoleTree>(KEY_SRL) ?: throw UninitializedPropertyAccessException()

    /********** Equalities **********/

    /**
     * Indicates whether some other object is "equal to" this one (without [id]).
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
     */
    override fun equals(other: Any?): Boolean =
            when (other) {
                is Word -> this.surface == other.surface &&
                        this.size == other.size && this.zip(other).all { it.first == it.second }
                else -> false
            }

    /**
     * [another] 어절과 표면형이 같은지 비교합니다.
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
 * 문장 Class
 *
 * @param words 문장에 포함되는 어절의 목록
 */
class Sentence(private val words: List<Word>) : Property(), List<Word> by words {
    init {
        // ID 값 배정
        for ((index, value) in words.withIndex()) {
            value.id = index
        }
    }

    /********** Access property values **********/

    /** 구문분석을 했다면, 문장의 최상위 구구조를 돌려줌.
     * 분석을 하지 않은 경우 [UninitializedPropertyAccessException] 발생 */
    @Throws(UninitializedPropertyAccessException::class)
    fun getSyntaxTree() = getProperty<SyntaxTree>(KEY_SYNTAXTREE) ?: throw UninitializedPropertyAccessException()

    /** 의존구문분석을 했다면, 문장의 최상위 지배소의 의존구문결과를 돌려줌.
     * 분석을 하지 않은 경우 [UninitializedPropertyAccessException] 발생 */
    @Throws(UninitializedPropertyAccessException::class)
    fun getDependencyTree() = getProperty<DepTree>(KEY_DEPTREE) ?: throw UninitializedPropertyAccessException()

    /** 의미역 분석을 했다면, 문장의 최상위 의미역 구조를 돌려줌.
     * 분석을 하지 않은 경우 [UninitializedPropertyAccessException] 발생 */
    @Throws(UninitializedPropertyAccessException::class)
    fun getSemRoleTree() = getProperty<RoleTree>(KEY_SRL) ?: throw UninitializedPropertyAccessException()

    /** 개체명 분석을 했다면, 문장의 개체명 목록을 돌려줌.
     * 분석을 하지 않은 경우 [UninitializedPropertyAccessException] 발생 */
    @Throws(UninitializedPropertyAccessException::class)
    fun getNamedEntities() = getProperty<ListProperty<Entity>>(KEY_NER)?.children
            ?: throw UninitializedPropertyAccessException()

    /********** Filter words by specific types **********/

    /**
     * 체언(명사, 수사, 대명사) 및 체언 성격의 어휘를 포함하는 어절들 (명사형 전성어미 [POS.ETN], 명사 파생 접미사 [POS.XSN] 포함)
     */
    fun getNouns(): List<Word> = words.filter { w ->
        w.any { it.isNoun() || it.hasTagOneOf("ETN", "XSN") }
    }

    /**
     * 용언(동사, 형용사) 및 용언 성격의 어휘를 포함하는 어절들 (용언의 활용형: V+E, 동사/형용사 파생 접미사 [POS.XSV], [POS.XSM] 포함)
     */
    fun getVerbs(): List<Word> = words.filter { w ->
        w.any { it.isPredicate() || it.hasTagOneOf("XSV", "XSM") }
    }

    /**
     * 수식언(관형사, 부사) 및 수식언 성격의 어휘를 포함하는 어절들 (관형형 전성어미 [POS.ETM], 부사 파생 접미사 [POS.XSA] 포함)
     */
    fun getModifiers(): List<Word> = words.filter { w ->
        w.any { it.isModifier() || it.hasTagOneOf("ETM", "XSA") }
    }
    /********** Equalities **********/

    /**
     * Indicates whether some other object is "equal to" this one (deep equal).
     *
     * Note that the `==` operator in Kotlin code is translated into a call to [equals] when objects on both sides of the
     * operator are not null.
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
     * 띄어쓰기 된 문장을 반환.
     *
     * @param delimiter 어절 사이의 띄어쓰기 방식. 기본값 = 공백(" ")
     * @return 띄어쓰기 된 문장.
     */
    @JvmOverloads
    fun surfaceString(delimiter: String = " "): String =
            words.joinToString(separator = delimiter) { it.surface }

    /**
     * 품사분석 결과를, 1행짜리 String으로 변환.
     *
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

        /** 빈 문장 */
        @JvmStatic
        val empty: Sentence by lazy { Sentence(listOf()) }
    }
}