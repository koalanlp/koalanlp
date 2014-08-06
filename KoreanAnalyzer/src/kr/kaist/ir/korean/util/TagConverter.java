package kr.kaist.ir.korean.util;

import kr.kaist.ir.korean.data.TaggedWord.FunctionalTag;

/**
 * 한나눔과 꼬꼬마의 서로 다른 형태소 유형을 통합 형식으로 변환하는 도구 클래스
 * 
 * @author 김부근
 * @version 0.2.2
 * @since 2014-07-31
 */
public final class TagConverter {
	/**
	 * 형태소를 분석한 분석기의 유형
	 * 
	 * @author 김부근
	 *
	 */
	public static enum TaggerType {
		/** 한나눔 */
		HNN,
		/** 꼬꼬마 */
		KKMA
	};

	/**
	 * 생성자. TagConverter는 객체를 생성하지 않으므로, 혼란 방지를 위해 private 선언.
	 */
	private TagConverter() {
	}

	/**
	 * 주어진 표기가 통합 형식으로 변환하였을 때 문제가 발생하는 지 확인한다.
	 * 
	 * @param tag
	 *            문제 발생 여부를 확인할 표기
	 * @param type
	 *            형태소 분석기 유형
	 * @return 문제가 발생하는 경우 참(true).
	 */
	public static boolean isProblematicPOSTag(String tag, TaggerType type) {
		if (type == TaggerType.KKMA) {
			return (tag.equalsIgnoreCase("NNG") || tag.startsWith("U") || tag
					.startsWith("JX"));
		} else {
			return (tag.equalsIgnoreCase("paa") || tag.equalsIgnoreCase("px")
					|| tag.startsWith("nn") || tag.equalsIgnoreCase("jp") || tag
						.equalsIgnoreCase("f"));
		}
	}

	/**
	 * 주어진 품사 구분 표기를 가장 가까운 통합형식으로 변환한다.
	 * 
	 * @param tag
	 *            통합형식으로 변환할 표기
	 * @param type
	 *            형태소 분석기 유형
	 * @return 가장 가깝게 변환된 통합형식 표기
	 */
	public static String toIntegratedPOSTag(String tag, TaggerType type) {
		if (type == TaggerType.KKMA) {
			return toIntegratedFromKkokkoma(tag);
		} else {
			return toIntegratedFromHannanum(tag);
		}
	}

	/**
	 * 꼬꼬마 형태소 분석기의 결과를 변환한다.
	 * 
	 * @param tag
	 *            변환할 표기
	 * @return 통합형식으로 변환된 표기
	 */
	private static String toIntegratedFromKkokkoma(String tag) {
		return tag.toUpperCase();
	}

	/**
	 * 한나눔 형태소 분석기의 결과를 변환한다.
	 * 
	 * @param tag
	 *            변환할 표기
	 * @return 통합형식으로 변환된 표기
	 */
	private static String toIntegratedFromHannanum(String tag) {
		if (tag.startsWith("p")) {
			return "V" + tag.substring(1, 2).toUpperCase();
		} else if (tag.startsWith("xs")) {
			if (tag.startsWith("xsm")) {
				return "XSA";
			} else if (tag.startsWith("xsa")) {
				return "XSM";
			}

			return "XS" + tag.substring(2, 3).toUpperCase();
		}

		switch (tag) {
		case "ncpa":
			return "NNGA";
		case "ncps":
			return "NNGS";
		case "ncn":
		case "ncr":
			return "NNGN";
		case "nqpa":
		case "nqpb":
		case "nqpc":
		case "nqq":
			return "NNP";
		case "nbn":
		case "nbs":
			return "NNB";
		case "nbu":
			return "NNM";
		case "nnc":
		case "nno":
			return "NR";
		case "npp":
		case "npd":
			return "NPP";
		case "jp":
			return "VCP";
		case "mmd":
		case "mma":
			return "MDT";
		case "mad":
		case "mag":
			return "MAG";
		case "maj":
			return "MAC";
		case "ii":
			return "IC";
		case "jcs":
			return "JKS";
		case "jcc":
			return "JKC";
		case "jcm":
			return "JKG";
		case "jco":
			return "JKO";
		case "jca":
			return "JKM";
		case "jcv":
			return "JKI";
		case "jcr":
			return "JCQ";
		case "jct":
		case "jcj":
			return "JC";
		case "ecc":
			return "ECE";
		case "ecs":
			return "ECD";
		case "ecx":
			return "ECS";
		case "etm":
			return "ETD";
		case "sl":
		case "sr":
			return "SS";
		case "sd":
			return "SO";
		case "su":
		case "sy":
			return "SW";
		case "f":
			return "O";
		default:
			return tag.toUpperCase();
		}
	}

	public static FunctionalTag getDependencyTag(String type, TaggerType tagger) {
		if (tagger == TaggerType.HNN) {
			switch (type) {
			case "SBJ":
				return FunctionalTag.Subject;
			case "OBJ":
				return FunctionalTag.Object;
			case "CMP":
				return FunctionalTag.Complement;
			case "VMOD":
			case "NMOD":
			case "MOD":
				return FunctionalTag.Modifier;
			case "ADV":
			case "AJT":
				return FunctionalTag.Adjunct;
			case "CNJ":
				return FunctionalTag.Conjunctive;
			case "INT":
				return FunctionalTag.Interjective;
			case "PRN":
				return FunctionalTag.Parenthetical;
			default:
				System.out.println("알려지지 않은 Dependency Tag:" + type);
			}
		} else {
			switch (type) {
			case "목적어":
				return FunctionalTag.Object;
			case "주어":
				return FunctionalTag.Subject;
			case "부사어":
				return FunctionalTag.Adjunct;
			case "보어":
			case "인용":
				return FunctionalTag.Complement;
			case "수식":
			case "명사구":
			case "이유":
				return FunctionalTag.Modifier;
			default:
				if (type.contains("연결")) {
					return FunctionalTag.Conjunctive;
				} else if (type.contains("대상")) {
					return FunctionalTag.Object;
				}
				System.out.println("알려지지 않은 Dependency Tag:" + type);
				return FunctionalTag.Conjunctive;
			}
		}
		return null;
	}
}
