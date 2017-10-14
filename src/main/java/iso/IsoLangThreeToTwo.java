package iso;

/**
 * Maps ISO 3 character language codes to 2 character if exist.
 * See https://raw.githubusercontent.com/maikudou/iso639-js/master/alpha3to2mapping.json
 * @author mac002
 *
 */
public class IsoLangThreeToTwo {
	
	public static String threeToTwo(String three) {
		switch (three) {
		case ("aar"): {
			return "aa";
		}
		case ("abk"): {
			return "ab";
		}
		case ("afr"): {
			return "af";
		}
		case ("aka"): {
			return "ak";
		}
		case ("amh"): {
			return "am";
		}
		case ("ara"): {
			return "ar";
		}
		case ("arg"): {
			return "an";
		}
		case ("asm"): {
			return "as";
		}
		case ("ava"): {
			return "av";
		}
		case ("ave"): {
			return "ae";
		}
		case ("aym"): {
			return "ay";
		}
		case ("aze"): {
			return "az";
		}
		case ("bak"): {
			return "ba";
		}
		case ("bam"): {
			return "bm";
		}
		case ("bel"): {
			return "be";
		}
		case ("ben"): {
			return "bn";
		}
		case ("bis"): {
			return "bi";
		}
		case ("bod"): {
			return "bo";
		}
		case ("bos"): {
			return "bs";
		}
		case ("bre"): {
			return "br";
		}
		case ("bul"): {
			return "bg";
		}
		case ("cat"): {
			return "ca";
		}
		case ("ces"): {
			return "cs";
		}
		case ("cha"): {
			return "ch";
		}
		case ("che"): {
			return "ce";
		}
		case ("chu"): {
			return "cu";
		}
		case ("chv"): {
			return "cv";
		}
		case ("cor"): {
			return "kw";
		}
		case ("cos"): {
			return "co";
		}
		case ("cre"): {
			return "cr";
		}
		case ("cym"): {
			return "cy";
		}
		case ("dan"): {
			return "da";
		}
		case ("deu"): {
			return "de";
		}
		case ("div"): {
			return "dv";
		}
		case ("dzo"): {
			return "dz";
		}
		case ("ell"): {
			return "el";
		}
		case ("eng"): {
			return "en";
		}
		case ("epo"): {
			return "eo";
		}
		case ("est"): {
			return "et";
		}
		case ("eus"): {
			return "eu";
		}
		case ("ewe"): {
			return "ee";
		}
		case ("fao"): {
			return "fo";
		}
		case ("fas"): {
			return "fa";
		}
		case ("fij"): {
			return "fj";
		}
		case ("fin"): {
			return "fi";
		}
		case ("fra"): {
			return "fr";
		}
		case ("fry"): {
			return "fy";
		}
		case ("ful"): {
			return "ff";
		}
		case ("gla"): {
			return "gd";
		}
		case ("gle"): {
			return "ga";
		}
		case ("glg"): {
			return "gl";
		}
		case ("glv"): {
			return "gv";
		}
		case ("gr"): { // special case.  Classic Greek to modern
			return "el";
		}
		case ("grn"): {
			return "gn";
		}
		case ("guj"): {
			return "gu";
		}
		case ("hat"): {
			return "ht";
		}
		case ("hau"): {
			return "ha";
		}
		case ("hbs"): {
			return "sh";
		}
		case ("heb"): {
			return "he";
		}
		case ("her"): {
			return "hz";
		}
		case ("hin"): {
			return "hi";
		}
		case ("hmo"): {
			return "ho";
		}
		case ("hrv"): {
			return "hr";
		}
		case ("hun"): {
			return "hu";
		}
		case ("hye"): {
			return "hy";
		}
		case ("ibo"): {
			return "ig";
		}
		case ("ido"): {
			return "io";
		}
		case ("iii"): {
			return "ii";
		}
		case ("iku"): {
			return "iu";
		}
		case ("ile"): {
			return "ie";
		}
		case ("ina"): {
			return "ia";
		}
		case ("ind"): {
			return "id";
		}
		case ("ipk"): {
			return "ik";
		}
		case ("isl"): {
			return "is";
		}
		case ("ita"): {
			return "it";
		}
		case ("jav"): {
			return "jv";
		}
		case ("jpn"): {
			return "ja";
		}
		case ("kal"): {
			return "kl";
		}
		case ("kan"): {
			return "kn";
		}
		case ("kas"): {
			return "ks";
		}
		case ("kat"): {
			return "ka";
		}
		case ("kau"): {
			return "kr";
		}
		case ("kaz"): {
			return "kk";
		}
		case ("khm"): {
			return "km";
		}
		case ("kik"): {
			return "ki";
		}
		case ("kin"): {
			return "rw";
		}
		case ("kir"): {
			return "ky";
		}
		case ("kom"): {
			return "kv";
		}
		case ("kon"): {
			return "kg";
		}
		case ("kor"): {
			return "ko";
		}
		case ("kua"): {
			return "kj";
		}
		case ("kur"): {
			return "ku";
		}
		case ("lao"): {
			return "lo";
		}
		case ("lat"): {
			return "la";
		}
		case ("lav"): {
			return "lv";
		}
		case ("lim"): {
			return "li";
		}
		case ("lin"): {
			return "ln";
		}
		case ("lit"): {
			return "lt";
		}
		case ("ltz"): {
			return "lb";
		}
		case ("lub"): {
			return "lu";
		}
		case ("lug"): {
			return "lg";
		}
		case ("mah"): {
			return "mh";
		}
		case ("mal"): {
			return "ml";
		}
		case ("mar"): {
			return "mr";
		}
		case ("mkd"): {
			return "mk";
		}
		case ("mlg"): {
			return "mg";
		}
		case ("mlt"): {
			return "mt";
		}
		case ("mon"): {
			return "mn";
		}
		case ("mri"): {
			return "mi";
		}
		case ("msa"): {
			return "ms";
		}
		case ("mya"): {
			return "my";
		}
		case ("nau"): {
			return "na";
		}
		case ("nav"): {
			return "nv";
		}
		case ("nbl"): {
			return "nr";
		}
		case ("nde"): {
			return "nd";
		}
		case ("ndo"): {
			return "ng";
		}
		case ("nep"): {
			return "ne";
		}
		case ("nld"): {
			return "nl";
		}
		case ("nno"): {
			return "nn";
		}
		case ("nob"): {
			return "nb";
		}
		case ("nor"): {
			return "no";
		}
		case ("nya"): {
			return "ny";
		}
		case ("oci"): {
			return "oc";
		}
		case ("oji"): {
			return "oj";
		}
		case ("ori"): {
			return "or";
		}
		case ("orm"): {
			return "om";
		}
		case ("oss"): {
			return "os";
		}
		case ("pan"): {
			return "pa";
		}
		case ("pli"): {
			return "pi";
		}
		case ("pol"): {
			return "pl";
		}
		case ("por"): {
			return "pt";
		}
		case ("pus"): {
			return "ps";
		}
		case ("que"): {
			return "qu";
		}
		case ("roh"): {
			return "rm";
		}
		case ("ron"): {
			return "ro";
		}
		case ("run"): {
			return "rn";
		}
		case ("rus"): {
			return "ru";
		}
		case ("sag"): {
			return "sg";
		}
		case ("san"): {
			return "sa";
		}
		case ("sin"): {
			return "si";
		}
		case ("slk"): {
			return "sk";
		}
		case ("slv"): {
			return "sl";
		}
		case ("sme"): {
			return "se";
		}
		case ("smo"): {
			return "sm";
		}
		case ("sna"): {
			return "sn";
		}
		case ("snd"): {
			return "sd";
		}
		case ("som"): {
			return "so";
		}
		case ("sot"): {
			return "st";
		}
		case ("spa"): {
			return "es";
		}
		case ("sqi"): {
			return "sq";
		}
		case ("srd"): {
			return "sc";
		}
		case ("srp"): {
			return "sr";
		}
		case ("ssw"): {
			return "ss";
		}
		case ("sun"): {
			return "su";
		}
		case ("swa"): {
			return "sw";
		}
		case ("swe"): {
			return "sv";
		}
		case ("tah"): {
			return "ty";
		}
		case ("tam"): {
			return "ta";
		}
		case ("tat"): {
			return "tt";
		}
		case ("tel"): {
			return "te";
		}
		case ("tgk"): {
			return "tg";
		}
		case ("tgl"): {
			return "tl";
		}
		case ("tha"): {
			return "th";
		}
		case ("tir"): {
			return "ti";
		}
		case ("ton"): {
			return "to";
		}
		case ("tsn"): {
			return "tn";
		}
		case ("tso"): {
			return "ts";
		}
		case ("tuk"): {
			return "tk";
		}
		case ("tur"): {
			return "tr";
		}
		case ("twi"): {
			return "tw";
		}
		case ("uig"): {
			return "ug";
		}
		case ("ukr"): {
			return "uk";
		}
		case ("urd"): {
			return "ur";
		}
		case ("uzb"): {
			return "uz";
		}
		case ("ven"): {
			return "ve";
		}
		case ("vie"): {
			return "vi";
		}
		case ("vol"): {
			return "vo";
		}
		case ("wln"): {
			return "wa";
		}
		case ("wol"): {
			return "wo";
		}
		case ("xho"): {
			return "xh";
		}
		case ("yid"): {
			return "yi";
		}
		case ("yor"): {
			return "yo";
		}
		case ("zha"): {
			return "za";
		}
		case ("zho"): {
			return "zh";
		}
		case ("zul"): {
			return "zu";
		}
			default: {
				return three;
			}
		}
	}
}
