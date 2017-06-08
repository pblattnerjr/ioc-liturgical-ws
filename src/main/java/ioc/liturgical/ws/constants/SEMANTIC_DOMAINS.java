package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;

import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;


/**
 * Semantic Domains from Louw and Nida's Greek Lexicon.
 * Persmission to use is being sought.
 * 
 * @author mac002
 *
 */
public enum SEMANTIC_DOMAINS {
	ROOT("DomainRoot", "The root node of the Semantic Domains.", null)
	, SD_1(
			""
			, "Geographical Objects and Features"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_1_A(
			"A"
			, " Universe, Creation "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_B(
			"B"
			, " Regions Above the Earth "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_C(
			"C"
			, " Regions Below the Surface of the Earth "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_D(
			"D"
			, " Heavenly Bodies "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_E(
			"E"
			, " Atmospheric Objects "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_F(
			"F"
			, " The Earth's Surface "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_G(
			"G"
			, " Elevated Land Formations "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_H(
			"H"
			, " Depressions and Holes "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_I(
			"I"
			, " Land in Contrast With the Sea "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_J(
			"J"
			, " Bodies of Water "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_K(
			"K"
			, " Sociopolitical Areas "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_L(
			"L"
			, " Governmental Administrative Areas "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_M(
			"M"
			, " Areas Which Are Uninhabited or Only Sparsely Populated "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_N(
			"N"
			, " Population Centers "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_O(
			"O"
			, " Pastures and Cultivated Lands "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_1_P(
			"P"
			, " Thoroughfares: Roads, Streets, Paths, etc. "
			, SEMANTIC_DOMAINS.SD_1
		)
		, SD_2(
			""
			, "Natural Substances"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_2_A(
			"A"
			, " Elements "
			, SEMANTIC_DOMAINS.SD_2
		)
		, SD_2_B(
			"B"
			, " Air "
			, SEMANTIC_DOMAINS.SD_2
		)
		, SD_2_C(
			"C"
			, " Fire "
			, SEMANTIC_DOMAINS.SD_2
		)
		, SD_2_D(
			"D"
			, " Water "
			, SEMANTIC_DOMAINS.SD_2
		)
		, SD_2_E(
			"E"
			, " Earth, Mud, Sand, Rock "
			, SEMANTIC_DOMAINS.SD_2
		)
		, SD_2_F(
			"F"
			, " Precious and Semiprecious Stones and Substances "
			, SEMANTIC_DOMAINS.SD_2
		)
		, SD_2_G(
			"G"
			, " Metals "
			, SEMANTIC_DOMAINS.SD_2
		)
		, SD_3(
			""
			, "Plants"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_3_A(
			"A"
			, " Plants [General Meaning] "
			, SEMANTIC_DOMAINS.SD_3
		)
		, SD_3_B(
			"B"
			, " Trees "
			, SEMANTIC_DOMAINS.SD_3
		)
		, SD_3_C(
			"C"
			, " Plants That Are Not Trees "
			, SEMANTIC_DOMAINS.SD_3
		)
		, SD_3_D(
			"D"
			, " Fruit Parts of Plants "
			, SEMANTIC_DOMAINS.SD_3
		)
		, SD_3_E(
			"E"
			, " Non-Fruit Parts of Plants "
			, SEMANTIC_DOMAINS.SD_3
		)
		, SD_3_F(
			"F"
			, " Wood and Wood Products "
			, SEMANTIC_DOMAINS.SD_3
		)
		, SD_4(
			""
			, "Animals"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_4_A(
			"A"
			, " Animals "
			, SEMANTIC_DOMAINS.SD_4
		)
		, SD_4_B(
			"B"
			, " Birds "
			, SEMANTIC_DOMAINS.SD_4
		)
		, SD_4_C(
			"C"
			, " Insects "
			, SEMANTIC_DOMAINS.SD_4
		)
		, SD_4_D(
			"D"
			, " Reptiles and Other 'Creeping Things' "
			, SEMANTIC_DOMAINS.SD_4
		)
		, SD_4_E(
			"E"
			, " Fishes and Other Sea Creatures "
			, SEMANTIC_DOMAINS.SD_4
		)
		, SD_5(
			""
			, "Foods and Condiments"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_5_A(
			"A"
			, " Food "
			, SEMANTIC_DOMAINS.SD_5
		)
		, SD_5_B(
			"B"
			, " Condiments "
			, SEMANTIC_DOMAINS.SD_5
		)
		, SD_6(
			""
			, "Artifacts"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_6_A(
			"A"
			, " Artifacts [General Meaning] "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_B(
			"B"
			, " Instruments Used in Agriculture and Husbandry "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_C(
			"C"
			, " Instruments Used in Fishing "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_D(
			"D"
			, " Instruments Used in Binding and Fastening "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_E(
			"E"
			, " Traps, Snares "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_F(
			"F"
			, " Instruments Used in Punishment and Execution "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_G(
			"G"
			, " Weapons and Armor "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_H(
			"H"
			, " Boats and Parts of Boats "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_I(
			"I"
			, " Vehicles "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_J(
			"J"
			, " Instruments Used in Marking and Writing "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_K(
			"K"
			, " Money and Monetary Units "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_L(
			"L"
			, " Musical Instruments "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_M(
			"M"
			, " Images and Idols "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_N(
			"N"
			, " Lights and Light Holders "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_O(
			"O"
			, " Furniture "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_P(
			"P"
			, " Containers "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_Q(
			"Q"
			, " Cloth, Leather, and Objects Made of Such Materials "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_R(
			"R"
			, " Adornments "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_S(
			"S"
			, " Plant Products "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_T(
			"T"
			, " Medicines "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_U(
			"U"
			, " Perfumes and Incense "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_V(
			"V"
			, " Instruments for Measuring "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_6_W(
			"W"
			, " Miscellaneous "
			, SEMANTIC_DOMAINS.SD_6
		)
		, SD_7(
			""
			, "Constructions"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_7_A(
			"A"
			, " Constructions [General Meaning] "
			, SEMANTIC_DOMAINS.SD_7
		)
		, SD_7_B(
			"B"
			, " Buildings "
			, SEMANTIC_DOMAINS.SD_7
		)
		, SD_7_C(
			"C"
			, " Parts and Areas of Buildings "
			, SEMANTIC_DOMAINS.SD_7
		)
		, SD_7_D(
			"D"
			, " Open Constructions "
			, SEMANTIC_DOMAINS.SD_7
		)
		, SD_7_E(
			"E"
			, " Constructions for Holding Water "
			, SEMANTIC_DOMAINS.SD_7
		)
		, SD_7_F(
			"F"
			, " Walls and Fences "
			, SEMANTIC_DOMAINS.SD_7
		)
		, SD_7_G(
			"G"
			, " Miscellaneous Constructions "
			, SEMANTIC_DOMAINS.SD_7
		)
		, SD_7_H(
			"H"
			, " Building Materials "
			, SEMANTIC_DOMAINS.SD_7
		)
		, SD_8(
			""
			, "Body, Body Parts, and Body Products"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_8_A(
			"A"
			, " Body "
			, SEMANTIC_DOMAINS.SD_8
		)
		, SD_8_B(
			"B"
			, " Parts of the Body "
			, SEMANTIC_DOMAINS.SD_8
		)
		, SD_8_C(
			"C"
			, " Physiological Products of the Body "
			, SEMANTIC_DOMAINS.SD_8
		)
		, SD_9(
			""
			, "People"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_9_A(
			"A"
			, " Human Beings "
			, SEMANTIC_DOMAINS.SD_9
		)
		, SD_9_B(
			"B"
			, " Males "
			, SEMANTIC_DOMAINS.SD_9
		)
		, SD_9_C(
			"C"
			, " Females "
			, SEMANTIC_DOMAINS.SD_9
		)
		, SD_9_D(
			"D"
			, " Children "
			, SEMANTIC_DOMAINS.SD_9
		)
		, SD_9_E(
			"E"
			, " Persons For Whom There Is Affectionate Concern "
			, SEMANTIC_DOMAINS.SD_9
		)
		, SD_10(
			""
			, "Kinship Terms"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_10_A(
			"A"
			, " Groups and Members of Groups of Persons Regarded as Related by Blood but without Special Reference to Successive Generations "
			, SEMANTIC_DOMAINS.SD_10
		)
		, SD_10_B(
			"B"
			, " Kinship Relations Involving Successive Generations "
			, SEMANTIC_DOMAINS.SD_10
		)
		, SD_10_C(
			"C"
			, " Kinship Relations of the Same Generation "
			, SEMANTIC_DOMAINS.SD_10
		)
		, SD_10_D(
			"D"
			, " Kinship Relations Based upon Marriage "
			, SEMANTIC_DOMAINS.SD_10
		)
		, SD_11(
			""
			, "Groups and Classes of Persons and Members of Such Groups and Classes"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_11_A(
			"A"
			, " General "
			, SEMANTIC_DOMAINS.SD_11
		)
		, SD_11_B(
			"B"
			, " Socio-Religious "
			, SEMANTIC_DOMAINS.SD_11
		)
		, SD_11_C(
			"C"
			, " Socio-Political "
			, SEMANTIC_DOMAINS.SD_11
		)
		, SD_11_D(
			"D"
			, " Ethnic-Cultural "
			, SEMANTIC_DOMAINS.SD_11
		)
		, SD_11_E(
			"E"
			, " Philosophical "
			, SEMANTIC_DOMAINS.SD_11
		)
		, SD_12(
			""
			, "Supernatural Beings and Powers"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_12_A(
			"A"
			, " Supernatural Beings "
			, SEMANTIC_DOMAINS.SD_12
		)
		, SD_12_B(
			"B"
			, " Supernatural Powers "
			, SEMANTIC_DOMAINS.SD_12
		)
		, SD_13(
			""
			, "Be, Become, Exist, Happen"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_13_A(
			"A"
			, " State "
			, SEMANTIC_DOMAINS.SD_13
		)
		, SD_13_B(
			"B"
			, " Change of State "
			, SEMANTIC_DOMAINS.SD_13
		)
		, SD_13_C(
			"C"
			, " Exist "
			, SEMANTIC_DOMAINS.SD_13
		)
		, SD_13_D(
			"D"
			, " Happen "
			, SEMANTIC_DOMAINS.SD_13
		)
		, SD_14(
			""
			, "Physical Events and States"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_14_A(
			"A"
			, " Weather "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_B(
			"B"
			, " Wind "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_C(
			"C"
			, " Rain "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_D(
			"D"
			, " Thunder and Lightning "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_E(
			"E"
			, " Events Involving Liquids and Dry Masses "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_F(
			"F"
			, " Light "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_G(
			"G"
			, " Darkness "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_H(
			"H"
			, " Burning "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_I(
			"I"
			, " Sound "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_14_J(
			"J"
			, " Movement of the Earth "
			, SEMANTIC_DOMAINS.SD_14
		)
		, SD_15(
			""
			, "Linear Movement"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_15_A(
			"A"
			, " Move, Come/Go "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_B(
			"B"
			, " Travel, Journey "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_C(
			"C"
			, " Pass, Cross Over, Go Through, Go Around "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_D(
			"D"
			, " Leave, Depart, Flee, Escape, Send "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_E(
			"E"
			, " Come Near, Approach "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_F(
			"F"
			, " Come, Come To, Arrive "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_G(
			"G"
			, " Return "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_H(
			"H"
			, " Come/Go Into "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_I(
			"I"
			, " Come/Go Onto "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_J(
			"J"
			, " Come/Go Up, Ascend "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_K(
			"K"
			, " Come/Go Down, Descend "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_L(
			"L"
			, " Fall "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_M(
			"M"
			, " Gather, Cause To Come Together "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_N(
			"N"
			, " Disperse, Scatter "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_O(
			"O"
			, " Come/Go Prior To "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_P(
			"P"
			, " Come/Go In Front Of "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_Q(
			"Q"
			, " Come/Go Behind "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_R(
			"R"
			, " Go Around, Surround "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_S(
			"S"
			, " Come/Go With, Travel With "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_T(
			"T"
			, " Follow, Accompany "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_U(
			"U"
			, " Pursue, Follow "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_V(
			"V"
			, " Drive Along, Carry Along "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_W(
			"W"
			, " Lead, Bring, Take "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_X(
			"X"
			, " Carry, Bear "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_Y(
			"Y"
			, " Pull, Draw, Drag "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_Z(
			"Z"
			, " Throw, Hurl "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZA(
			"ZA"
			, " Movement with Speed "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZB(
			"ZB"
			, " Goal-Oriented Movement "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZC(
			"ZC"
			, " Walk, Step "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZD(
			"ZD"
			, " Run "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZE(
			"ZE"
			, " Jump, Leap "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZF(
			"ZF"
			, " Dance "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZG(
			"ZG"
			, " Fly "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZH(
			"ZH"
			, " Swim "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_15_ZI(
			"ZI"
			, " Roll "
			, SEMANTIC_DOMAINS.SD_15
		)
		, SD_16(
			""
			, "Non-Linear Movement"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_17(
			""
			, "Stances and Events Related to Stances"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_17_A(
			"A"
			, " Stand "
			, SEMANTIC_DOMAINS.SD_17
		)
		, SD_17_B(
			"B"
			, " Sit "
			, SEMANTIC_DOMAINS.SD_17
		)
		, SD_17_C(
			"C"
			, " Kneel "
			, SEMANTIC_DOMAINS.SD_17
		)
		, SD_17_D(
			"D"
			, " Prostrate "
			, SEMANTIC_DOMAINS.SD_17
		)
		, SD_17_E(
			"E"
			, " Prostrate as an Act of Reverence or Supplication "
			, SEMANTIC_DOMAINS.SD_17
		)
		, SD_17_F(
			"F"
			, " Recline [to eat] "
			, SEMANTIC_DOMAINS.SD_17
		)
		, SD_17_G(
			"G"
			, " Lie "
			, SEMANTIC_DOMAINS.SD_17
		)
		, SD_17_H(
			"H"
			, " Bend Over, Straighten Up "
			, SEMANTIC_DOMAINS.SD_17
		)
		, SD_18(
			""
			, "Attachment"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_18_A(
			"A"
			, " Grasp, Hold "
			, SEMANTIC_DOMAINS.SD_18
		)
		, SD_18_B(
			"B"
			, " Fasten, Stick To "
			, SEMANTIC_DOMAINS.SD_18
		)
		, SD_19(
			""
			, "Physical Impact"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_19_A(
			"A"
			, " Hit, Strike "
			, SEMANTIC_DOMAINS.SD_19
		)
		, SD_19_B(
			"B"
			, " Pierce, Cut "
			, SEMANTIC_DOMAINS.SD_19
		)
		, SD_19_C(
			"C"
			, " Split, Tear "
			, SEMANTIC_DOMAINS.SD_19
		)
		, SD_19_D(
			"D"
			, " Break, Break Through "
			, SEMANTIC_DOMAINS.SD_19
		)
		, SD_19_E(
			"E"
			, " Press "
			, SEMANTIC_DOMAINS.SD_19
		)
		, SD_19_F(
			"F"
			, " Dig "
			, SEMANTIC_DOMAINS.SD_19
		)
		, SD_20(
			""
			, "Violence, Harm, Destroy, Kill"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_20_A(
			"A"
			, " Violence "
			, SEMANTIC_DOMAINS.SD_20
		)
		, SD_20_B(
			"B"
			, " Harm, Wound "
			, SEMANTIC_DOMAINS.SD_20
		)
		, SD_20_C(
			"C"
			, " Destroy "
			, SEMANTIC_DOMAINS.SD_20
		)
		, SD_20_D(
			"D"
			, " Kill "
			, SEMANTIC_DOMAINS.SD_20
		)
		, SD_21(
			""
			, "Danger, Risk, Safe, Save"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_21_A(
			"A"
			, " Danger "
			, SEMANTIC_DOMAINS.SD_21
		)
		, SD_21_B(
			"B"
			, " Expose Oneself to Danger "
			, SEMANTIC_DOMAINS.SD_21
		)
		, SD_21_C(
			"C"
			, " Safe, Free from Danger "
			, SEMANTIC_DOMAINS.SD_21
		)
		, SD_21_D(
			"D"
			, " Become Safe, Free from Danger "
			, SEMANTIC_DOMAINS.SD_21
		)
		, SD_21_E(
			"E"
			, " Cause To Be Safe, Free from Danger "
			, SEMANTIC_DOMAINS.SD_21
		)
		, SD_21_F(
			"F"
			, " Save in a Religious Sense "
			, SEMANTIC_DOMAINS.SD_21
		)
		, SD_22(
			""
			, "Trouble, Hardship, Relief, Favorable Circumstances"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_22_A(
			"A"
			, " Trouble, Hardship, Distress "
			, SEMANTIC_DOMAINS.SD_22
		)
		, SD_22_B(
			"B"
			, " Experience Trouble, Hardship "
			, SEMANTIC_DOMAINS.SD_22
		)
		, SD_22_C(
			"C"
			, " Cause Trouble, Hardship "
			, SEMANTIC_DOMAINS.SD_22
		)
		, SD_22_D(
			"D"
			, " Difficult, Hard "
			, SEMANTIC_DOMAINS.SD_22
		)
		, SD_22_E(
			"E"
			, " Relief from Trouble "
			, SEMANTIC_DOMAINS.SD_22
		)
		, SD_22_F(
			"F"
			, " Easy, Light "
			, SEMANTIC_DOMAINS.SD_22
		)
		, SD_22_G(
			"G"
			, " Favorable Circumstances or State "
			, SEMANTIC_DOMAINS.SD_22
		)
		, SD_23(
			""
			, "Physiological Processes and States"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_23_A(
			"A"
			, " Eat, Drink "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_B(
			"B"
			, " Processes Involving the Mouth, Other Than Eating and Drinking "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_C(
			"C"
			, " Birth, Procreation "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_D(
			"D"
			, " Sexual Relations "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_E(
			"E"
			, " Sleep, Waking "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_F(
			"F"
			, " Tire, Rest "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_G(
			"G"
			, " Live, Die "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_H(
			"H"
			, " Health, Vigor, Strength "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_I(
			"I"
			, " Sickness, Disease, Weakness "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_J(
			"J"
			, " Breathe, Breath "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_K(
			"K"
			, " Grow, Growth "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_L(
			"L"
			, " Ripen, Produce Fruit, Bear Seed "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_23_M(
			"M"
			, " Rot, Decay "
			, SEMANTIC_DOMAINS.SD_23
		)
		, SD_24(
			""
			, "Sensory Events and States"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_24_A(
			"A"
			, " See "
			, SEMANTIC_DOMAINS.SD_24
		)
		, SD_24_B(
			"B"
			, " Hear "
			, SEMANTIC_DOMAINS.SD_24
		)
		, SD_24_C(
			"C"
			, " Smell "
			, SEMANTIC_DOMAINS.SD_24
		)
		, SD_24_D(
			"D"
			, " Taste "
			, SEMANTIC_DOMAINS.SD_24
		)
		, SD_24_E(
			"E"
			, " Touch, Feel "
			, SEMANTIC_DOMAINS.SD_24
		)
		, SD_24_F(
			"F"
			, " Pain, Suffering "
			, SEMANTIC_DOMAINS.SD_24
		)
		, SD_24_G(
			"G"
			, " General Sensory Perception "
			, SEMANTIC_DOMAINS.SD_24
		)
		, SD_25(
			""
			, "Attitudes and Emotions"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_25_A(
			"A"
			, " Desire, Want, Wish "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_B(
			"B"
			, " Desire Strongly "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_C(
			"C"
			, " Love, Affection, Compassion "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_D(
			"D"
			, " Hope, Look Forward To "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_E(
			"E"
			, " Be Willing "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_F(
			"F"
			, " Be Eager, Be Earnest, In a Devoted Manner "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_G(
			"G"
			, " Content, Satisfied "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_H(
			"H"
			, " Acceptable To, To Be Pleased With "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_I(
			"I"
			, " Thankful, Grateful "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_J(
			"J"
			, " Enjoy, Take Pleasure In, Be Fond of Doing "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_K(
			"K"
			, " Happy, Glad, Joyful "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_L(
			"L"
			, " Laugh, Cry, Groan "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_M(
			"M"
			, " Encouragement, Consolation "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_N(
			"N"
			, " Courage, Boldness "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_O(
			"O"
			, " Patience, Endurance, Perseverance "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_P(
			"P"
			, " Offend, Be Offended "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_Q(
			"Q"
			, " Abhor "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_R(
			"R"
			, " Shame, Disgrace, Humiliation "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_S(
			"S"
			, " Pride [legitimate] "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_T(
			"T"
			, " Surprise, Astonish "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_U(
			"U"
			, " Worry, Anxiety, Distress, Peace "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_V(
			"V"
			, " Fear, Terror, Alarm "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_W(
			"W"
			, " Sorrow, Regret "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_25_X(
			"X"
			, " Discouragement "
			, SEMANTIC_DOMAINS.SD_25
		)
		, SD_26(
			""
			, "Psychological Faculties"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_27(
			""
			, "Learn"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_27_A(
			"A"
			, " Learn "
			, SEMANTIC_DOMAINS.SD_27
		)
		, SD_27_B(
			"B"
			, " Learn the Location of Something "
			, SEMANTIC_DOMAINS.SD_27
		)
		, SD_27_C(
			"C"
			, " Learn Something Against Someone "
			, SEMANTIC_DOMAINS.SD_27
		)
		, SD_27_D(
			"D"
			, " Try To Learn "
			, SEMANTIC_DOMAINS.SD_27
		)
		, SD_27_E(
			"E"
			, " Be Willing To Learn "
			, SEMANTIC_DOMAINS.SD_27
		)
		, SD_27_F(
			"F"
			, " Be Ready To Learn, Pay Attention "
			, SEMANTIC_DOMAINS.SD_27
		)
		, SD_27_G(
			"G"
			, " Recognize "
			, SEMANTIC_DOMAINS.SD_27
		)
		, SD_28(
			""
			, "Know"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_28_A(
			"A"
			, " Know "
			, SEMANTIC_DOMAINS.SD_28
		)
		, SD_28_B(
			"B"
			, " Known [the content of knowledge] "
			, SEMANTIC_DOMAINS.SD_28
		)
		, SD_28_C(
			"C"
			, " Well Known, Clearly Shown, Revealed "
			, SEMANTIC_DOMAINS.SD_28
		)
		, SD_28_D(
			"D"
			, " Able To Be Known "
			, SEMANTIC_DOMAINS.SD_28
		)
		, SD_28_E(
			"E"
			, " Not Able To Be Known, Secret "
			, SEMANTIC_DOMAINS.SD_28
		)
		, SD_29(
			""
			, "Memory and Recall"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_29_A(
			"A"
			, " Storing of Information "
			, SEMANTIC_DOMAINS.SD_29
		)
		, SD_29_B(
			"B"
			, " Recalling from Memory "
			, SEMANTIC_DOMAINS.SD_29
		)
		, SD_29_C(
			"C"
			, " Not Remembering, Forgetting "
			, SEMANTIC_DOMAINS.SD_29
		)
		, SD_29_D(
			"D"
			, " Recalling and Responding with Appropriate Action "
			, SEMANTIC_DOMAINS.SD_29
		)
		, SD_30(
			""
			, "Think"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_30_A(
			"A"
			, " To Think, Thought "
			, SEMANTIC_DOMAINS.SD_30
		)
		, SD_30_B(
			"B"
			, " To Think About, with the Implied Purpose of Responding Appropriately "
			, SEMANTIC_DOMAINS.SD_30
		)
		, SD_30_C(
			"C"
			, " To Think Concerning Future Contingencies "
			, SEMANTIC_DOMAINS.SD_30
		)
		, SD_30_D(
			"D"
			, " To Intend, To Purpose, To Plan "
			, SEMANTIC_DOMAINS.SD_30
		)
		, SD_30_E(
			"E"
			, " To Decide, To Conclude "
			, SEMANTIC_DOMAINS.SD_30
		)
		, SD_30_F(
			"F"
			, " To Choose, To Select, To Prefer "
			, SEMANTIC_DOMAINS.SD_30
		)
		, SD_30_G(
			"G"
			, " To Distinguish, To Evaluate, To Judge "
			, SEMANTIC_DOMAINS.SD_30
		)
		, SD_31(
			""
			, "Hold a View, Believe, Trust"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_31_A(
			"A"
			, " Have an Opinion, Hold a View "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_B(
			"B"
			, " Hold a Wrong View, Be Mistaken "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_C(
			"C"
			, " Agree, Consent "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_D(
			"D"
			, " Acknowledge "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_E(
			"E"
			, " Suppose, Think Possible "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_F(
			"F"
			, " Believe To Be True "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_G(
			"G"
			, " Accept As True "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_H(
			"H"
			, " Change an Opinion Concerning Truth "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_I(
			"I"
			, " Trust, Rely "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_31_J(
			"J"
			, " Be a Believer, Christian Faith "
			, SEMANTIC_DOMAINS.SD_31
		)
		, SD_32(
			""
			, "Understand"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_32_A(
			"A"
			, " Understand "
			, SEMANTIC_DOMAINS.SD_32
		)
		, SD_32_B(
			"B"
			, " Come To Understand "
			, SEMANTIC_DOMAINS.SD_32
		)
		, SD_32_C(
			"C"
			, " Ease or Difficulty in Understanding "
			, SEMANTIC_DOMAINS.SD_32
		)
		, SD_32_D(
			"D"
			, " Capacity for Understanding "
			, SEMANTIC_DOMAINS.SD_32
		)
		, SD_32_E(
			"E"
			, " Lack of Capacity for Understanding "
			, SEMANTIC_DOMAINS.SD_32
		)
		, SD_33(
			""
			, "Communication"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_33_A(
			"A"
			, " Language "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_B(
			"B"
			, " Word, Passage "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_C(
			"C"
			, " Discourse Types "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_D(
			"D"
			, " Language Levels "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_E(
			"E"
			, " Written Language "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_F(
			"F"
			, " Speak, Talk "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_G(
			"G"
			, " Sing, Lament "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_H(
			"H"
			, " Keep Silent "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_I(
			"I"
			, " Name "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_J(
			"J"
			, " Interpret, Mean, Explain "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_K(
			"K"
			, " Converse, Discuss "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_L(
			"L"
			, " Ask For, Request "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_M(
			"M"
			, " Pray "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_N(
			"N"
			, " Question, Answer "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_O(
			"O"
			, " Inform, Announce "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_P(
			"P"
			, " Assert, Declare "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_Q(
			"Q"
			, " Teach "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_R(
			"R"
			, " Speak Truth, Speak Falsehood "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_S(
			"S"
			, " Preach, Proclaim "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_T(
			"T"
			, " Witness, Testify "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_U(
			"U"
			, " Profess Allegiance "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_V(
			"V"
			, " Admit, Confess, Deny "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_W(
			"W"
			, " Agree "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_X(
			"X"
			, " Foretell, Tell Fortunes "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_Y(
			"Y"
			, " Promise "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_Z(
			"Z"
			, " Threaten "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZA(
			"ZA"
			, " Advise "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZB(
			"ZB"
			, " Urge, Persuade "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZC(
			"ZC"
			, " Call "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZD(
			"ZD"
			, " Invite "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZE(
			"ZE"
			, " Insist "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZF(
			"ZF"
			, " Command, Order "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZG(
			"ZG"
			, " Law, Regulation, Ordinance "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZH(
			"ZH"
			, " Recommend, Propose "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZI(
			"ZI"
			, " Intercede "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZJ(
			"ZJ"
			, " Thanks "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZK(
			"ZK"
			, " Praise "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZL(
			"ZL"
			, " Flatter "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZM(
			"ZM"
			, " Boast "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZN(
			"ZN"
			, " Foolish Talk "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZO(
			"ZO"
			, " Complain "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZP(
			"ZP"
			, " Insult, Slander "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZQ(
			"ZQ"
			, " Gossip "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZR(
			"ZR"
			, " Mock, Ridicule "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZS(
			"ZS"
			, " Criticize "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZT(
			"ZT"
			, " Rebuke "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZU(
			"ZU"
			, " Warn "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZV(
			"ZV"
			, " Accuse, Blame "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZW(
			"ZW"
			, " Defend, Excuse "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZX(
			"ZX"
			, " Dispute, Debate "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZY(
			"ZY"
			, " Argue, Quarrel "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_33_ZZ(
			"ZZ"
			, " Oppose, Contradict "
			, SEMANTIC_DOMAINS.SD_33
		)
		, SD_34(
			""
			, "Association"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_34_A(
			"A"
			, " Associate "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_B(
			"B"
			, " Join, Begin To Associate "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_C(
			"C"
			, " Belong To, Be Included in the Membership of, Be Excluded From "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_D(
			"D"
			, " Limit or Avoid Association "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_E(
			"E"
			, " Establish or Confirm a Relation "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_F(
			"F"
			, " Visit "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_G(
			"G"
			, " Welcome, Receive "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_H(
			"H"
			, " Show Hospitality "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_I(
			"I"
			, " Kiss, Embrace "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_34_J(
			"J"
			, " Marriage, Divorce "
			, SEMANTIC_DOMAINS.SD_34
		)
		, SD_35(
			""
			, "Help, Care For"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_35_A(
			"A"
			, " Help "
			, SEMANTIC_DOMAINS.SD_35
		)
		, SD_35_B(
			"B"
			, " Serve "
			, SEMANTIC_DOMAINS.SD_35
		)
		, SD_35_C(
			"C"
			, " Provide For, Support "
			, SEMANTIC_DOMAINS.SD_35
		)
		, SD_35_D(
			"D"
			, " Care For, Take Care Of "
			, SEMANTIC_DOMAINS.SD_35
		)
		, SD_35_E(
			"E"
			, " Entrust To the Care Of "
			, SEMANTIC_DOMAINS.SD_35
		)
		, SD_35_F(
			"F"
			, " Rear, Bring Up "
			, SEMANTIC_DOMAINS.SD_35
		)
		, SD_35_G(
			"G"
			, " Adopt "
			, SEMANTIC_DOMAINS.SD_35
		)
		, SD_35_H(
			"H"
			, " Desert, Forsake "
			, SEMANTIC_DOMAINS.SD_35
		)
		, SD_36(
			""
			, "Guide, Discipline, Follow"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_36_A(
			"A"
			, " Guide, Lead "
			, SEMANTIC_DOMAINS.SD_36
		)
		, SD_36_B(
			"B"
			, " Discipline, Train "
			, SEMANTIC_DOMAINS.SD_36
		)
		, SD_36_C(
			"C"
			, " Obey, Disobey "
			, SEMANTIC_DOMAINS.SD_36
		)
		, SD_36_D(
			"D"
			, " Follow, Be a Disciple "
			, SEMANTIC_DOMAINS.SD_36
		)
		, SD_37(
			""
			, "Control, Rule"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_37_A(
			"A"
			, " Control, Restrain "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_B(
			"B"
			, " Compel, Force "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_C(
			"C"
			, " Exercise Authority "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_D(
			"D"
			, " Rule, Govern "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_E(
			"E"
			, " Assign to a Role or Function "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_F(
			"F"
			, " Seize, Take into Custody "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_G(
			"G"
			, " Hand Over, Betray "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_H(
			"H"
			, " Imprison "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_I(
			"I"
			, " Guard, Watch Over "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_37_J(
			"J"
			, " Release, Set Free "
			, SEMANTIC_DOMAINS.SD_37
		)
		, SD_38(
			""
			, "Punish, Reward"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_38_A(
			"A"
			, " Punish "
			, SEMANTIC_DOMAINS.SD_38
		)
		, SD_38_B(
			"B"
			, " Reward, Recompense "
			, SEMANTIC_DOMAINS.SD_38
		)
		, SD_39(
			""
			, "Hostility, Strife"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_39_A(
			"A"
			, " Opposition, Hostility "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_B(
			"B"
			, " Division "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_C(
			"C"
			, " Resistance "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_D(
			"D"
			, " Yielding "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_E(
			"E"
			, " Strife, Struggle "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_F(
			"F"
			, " Revenge "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_G(
			"G"
			, " Rebellion "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_H(
			"H"
			, " Riot "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_I(
			"I"
			, " Persecution "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_J(
			"J"
			, " Attack "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_K(
			"K"
			, " Ambush "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_39_L(
			"L"
			, " Conquer "
			, SEMANTIC_DOMAINS.SD_39
		)
		, SD_40(
			""
			, "Reconciliation, Forgiveness"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_40_A(
			"A"
			, " Reconciliation "
			, SEMANTIC_DOMAINS.SD_40
		)
		, SD_40_B(
			"B"
			, " Forgiveness "
			, SEMANTIC_DOMAINS.SD_40
		)
		, SD_41(
			""
			, "Behavior and Related States"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_41_A(
			"A"
			, " Behavior, Conduct "
			, SEMANTIC_DOMAINS.SD_41
		)
		, SD_41_B(
			"B"
			, " Custom, Tradition "
			, SEMANTIC_DOMAINS.SD_41
		)
		, SD_41_C(
			"C"
			, " Particular Patterns of Behavior "
			, SEMANTIC_DOMAINS.SD_41
		)
		, SD_41_D(
			"D"
			, " Imitate Behavior "
			, SEMANTIC_DOMAINS.SD_41
		)
		, SD_41_E(
			"E"
			, " Change Behavior "
			, SEMANTIC_DOMAINS.SD_41
		)
		, SD_42(
			""
			, "Perform, Do"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_42_A(
			"A"
			, " Function "
			, SEMANTIC_DOMAINS.SD_42
		)
		, SD_42_B(
			"B"
			, " Do, Perform "
			, SEMANTIC_DOMAINS.SD_42
		)
		, SD_42_C(
			"C"
			, " Make, Create "
			, SEMANTIC_DOMAINS.SD_42
		)
		, SD_42_D(
			"D"
			, " Work, Toil "
			, SEMANTIC_DOMAINS.SD_42
		)
		, SD_42_E(
			"E"
			, " Craft, Trade "
			, SEMANTIC_DOMAINS.SD_42
		)
		, SD_43(
			""
			, "Agriculture"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_44(
			""
			, "Animal Husbandry, Fishing"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_45(
			""
			, "Building, Constructing"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_46(
			""
			, "Household Activities"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_47(
			""
			, "Activities Involving Liquids or Masses"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_47_A(
			"A"
			, " Movement of Liquids or Masses "
			, SEMANTIC_DOMAINS.SD_47
		)
		, SD_47_B(
			"B"
			, " Use of Liquids "
			, SEMANTIC_DOMAINS.SD_47
		)
		, SD_47_C(
			"C"
			, " Application and Removal of Liquids or Masses "
			, SEMANTIC_DOMAINS.SD_47
		)
		, SD_48(
			""
			, "Activities Involving Cloth"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_49(
			""
			, "Activities Involving Clothing and Adorning"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_50(
			""
			, "Contests and Play"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_51(
			""
			, "Festivals"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_52(
			""
			, "Funerals and Burial"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_53(
			""
			, "Religious Activities"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_53_A(
			"A"
			, " Religious Practice "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_B(
			"B"
			, " Offering, Sacrifice "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_C(
			"C"
			, " Purify, Cleanse "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_D(
			"D"
			, " Defile, Unclean, Common "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_E(
			"E"
			, " Baptize "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_F(
			"F"
			, " Dedicate, Consecrate "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_G(
			"G"
			, " Worship, Reverence "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_H(
			"H"
			, " Fasting "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_I(
			"I"
			, " Roles and Functions "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_J(
			"J"
			, " Magic "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_K(
			"K"
			, " Exorcism "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_53_L(
			"L"
			, " Sacrilege "
			, SEMANTIC_DOMAINS.SD_53
		)
		, SD_54(
			""
			, "Maritime Activities"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_55(
			""
			, "Military Activities"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_55_A(
			"A"
			, " To Arm "
			, SEMANTIC_DOMAINS.SD_55
		)
		, SD_55_B(
			"B"
			, " To Fight "
			, SEMANTIC_DOMAINS.SD_55
		)
		, SD_55_C(
			"C"
			, " Army "
			, SEMANTIC_DOMAINS.SD_55
		)
		, SD_55_D(
			"D"
			, " Soldiers, Officers "
			, SEMANTIC_DOMAINS.SD_55
		)
		, SD_55_E(
			"E"
			, " Prisoners of War "
			, SEMANTIC_DOMAINS.SD_55
		)
		, SD_56(
			""
			, "Courts and Legal Procedures"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_56_A(
			"A"
			, " Court of Justice "
			, SEMANTIC_DOMAINS.SD_56
		)
		, SD_56_B(
			"B"
			, " Lawsuit, Case "
			, SEMANTIC_DOMAINS.SD_56
		)
		, SD_56_C(
			"C"
			, " Accusation "
			, SEMANTIC_DOMAINS.SD_56
		)
		, SD_56_D(
			"D"
			, " Judicial Hearing, Inquiry "
			, SEMANTIC_DOMAINS.SD_56
		)
		, SD_56_E(
			"E"
			, " Judge, Condemn, Acquit "
			, SEMANTIC_DOMAINS.SD_56
		)
		, SD_56_F(
			"F"
			, " Obtain Justice "
			, SEMANTIC_DOMAINS.SD_56
		)
		, SD_56_G(
			"G"
			, " Attorney, Lawyer "
			, SEMANTIC_DOMAINS.SD_56
		)
		, SD_56_H(
			"H"
			, " Lead Off to Punishment "
			, SEMANTIC_DOMAINS.SD_56
		)
		, SD_57(
			""
			, "Possess, Transfer, Exchange"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_57_A(
			"A"
			, " Have, Possess, Property, Owner "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_B(
			"B"
			, " Have Sufficient "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_C(
			"C"
			, " Be Rich, Be Wealthy "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_D(
			"D"
			, " Treasure "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_E(
			"E"
			, " Need, Lack "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_F(
			"F"
			, " Be Poor, Be Needy, Poverty "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_G(
			"G"
			, " Take, Obtain, Gain, Lose "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_H(
			"H"
			, " Give "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_I(
			"I"
			, " Receive "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_J(
			"J"
			, " Exchange "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_K(
			"K"
			, " Spend, Waste "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_L(
			"L"
			, " Pay, Price, Cost "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_M(
			"M"
			, " Hire, Rent Out "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_N(
			"N"
			, " Tax, Tribute "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_O(
			"O"
			, " Sell, Buy, Price "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_P(
			"P"
			, " Earn, Gain, Do Business "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_Q(
			"Q"
			, " Lend, Loan, Interest, Borrow, Bank "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_R(
			"R"
			, " Owe, Debt, Cancel "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_S(
			"S"
			, " Be a Financial Burden "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_T(
			"T"
			, " Keep Records "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_57_U(
			"U"
			, " Steal, Rob "
			, SEMANTIC_DOMAINS.SD_57
		)
		, SD_58(
			""
			, "Nature, Class, Example"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_58_A(
			"A"
			, " Nature, Character "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_B(
			"B"
			, " Appearance as an Outward Manifestation of Form "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_C(
			"C"
			, " Basic Principles or Features Defining the Nature of Something "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_D(
			"D"
			, " Class, Kind "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_E(
			"E"
			, " Same or Equivalent Kind or Class "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_F(
			"F"
			, " Different Kind or Class "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_G(
			"G"
			, " Distinctive, Unique "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_H(
			"H"
			, " Unusual, Different From the Ordinary "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_I(
			"I"
			, " Pattern, Model, Example, and Corresponding Representation "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_J(
			"J"
			, " Archetype, Corresponding Type [Antitype] "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_58_K(
			"K"
			, " New, Old [primarily non-temporal] "
			, SEMANTIC_DOMAINS.SD_58
		)
		, SD_59(
			""
			, "Quantity"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_59_A(
			"A"
			, " Many, Few [Countables] "
			, SEMANTIC_DOMAINS.SD_59
		)
		, SD_59_B(
			"B"
			, " Much, Little [Masses, Collectives, Extensions] "
			, SEMANTIC_DOMAINS.SD_59
		)
		, SD_59_C(
			"C"
			, " All, Any, Each, Every [Totality] "
			, SEMANTIC_DOMAINS.SD_59
		)
		, SD_59_D(
			"D"
			, " Full, Empty "
			, SEMANTIC_DOMAINS.SD_59
		)
		, SD_59_E(
			"E"
			, " Enough, Sufficient "
			, SEMANTIC_DOMAINS.SD_59
		)
		, SD_59_F(
			"F"
			, " Abundance, Excess, Sparing "
			, SEMANTIC_DOMAINS.SD_59
		)
		, SD_59_G(
			"G"
			, " Increase, Decrease "
			, SEMANTIC_DOMAINS.SD_59
		)
		, SD_59_H(
			"H"
			, " Add, Subtract "
			, SEMANTIC_DOMAINS.SD_59
		)
		, SD_60(
			""
			, "Number"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_60_A(
			"A"
			, " Number, Countless "
			, SEMANTIC_DOMAINS.SD_60
		)
		, SD_60_B(
			"B"
			, " One, Two, Three, Etc. [Cardinals] "
			, SEMANTIC_DOMAINS.SD_60
		)
		, SD_60_C(
			"C"
			, " First, Second, Third, Etc. [Ordinals] "
			, SEMANTIC_DOMAINS.SD_60
		)
		, SD_60_D(
			"D"
			, " Half, Third, Fourth [Fractional Part] "
			, SEMANTIC_DOMAINS.SD_60
		)
		, SD_60_E(
			"E"
			, " Once, Twice, Three Times, Etc. [Cardinals of Time] "
			, SEMANTIC_DOMAINS.SD_60
		)
		, SD_60_F(
			"F"
			, " Double, Four Times As Much, Etc. [Multiples] "
			, SEMANTIC_DOMAINS.SD_60
		)
		, SD_60_G(
			"G"
			, " Pair, Group [Numbered Collectives] "
			, SEMANTIC_DOMAINS.SD_60
		)
		, SD_61(
			""
			, "Sequence"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_62(
			""
			, "Arrange, Organize"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_62_A(
			"A"
			, " Put Together, Arrange [of physical objects] "
			, SEMANTIC_DOMAINS.SD_62
		)
		, SD_62_B(
			"B"
			, " Organize [of events and states] "
			, SEMANTIC_DOMAINS.SD_62
		)
		, SD_63(
			""
			, "Whole, Unite, Part, Divide"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_63_A(
			"A"
			, " Whole "
			, SEMANTIC_DOMAINS.SD_63
		)
		, SD_63_B(
			"B"
			, " Unite "
			, SEMANTIC_DOMAINS.SD_63
		)
		, SD_63_C(
			"C"
			, " Mix "
			, SEMANTIC_DOMAINS.SD_63
		)
		, SD_63_D(
			"D"
			, " Part "
			, SEMANTIC_DOMAINS.SD_63
		)
		, SD_63_E(
			"E"
			, " Remnant "
			, SEMANTIC_DOMAINS.SD_63
		)
		, SD_63_F(
			"F"
			, " Divide "
			, SEMANTIC_DOMAINS.SD_63
		)
		, SD_63_G(
			"G"
			, " Separate "
			, SEMANTIC_DOMAINS.SD_63
		)
		, SD_64(
			""
			, "Comparison"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_65(
			""
			, "Value"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_65_A(
			"A"
			, " Valuable, Lacking in Value "
			, SEMANTIC_DOMAINS.SD_65
		)
		, SD_65_B(
			"B"
			, " Worthy, Not Worthy "
			, SEMANTIC_DOMAINS.SD_65
		)
		, SD_65_C(
			"C"
			, " Good, Bad "
			, SEMANTIC_DOMAINS.SD_65
		)
		, SD_65_D(
			"D"
			, " Useful, Useless "
			, SEMANTIC_DOMAINS.SD_65
		)
		, SD_65_E(
			"E"
			, " Advantageous, Not Advantageous "
			, SEMANTIC_DOMAINS.SD_65
		)
		, SD_65_F(
			"F"
			, " Important, Unimportant "
			, SEMANTIC_DOMAINS.SD_65
		)
		, SD_66(
			""
			, "Proper, Improper"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_67(
			""
			, "Time"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_67_A(
			"A"
			, " A Point of Time without Reference to Other Points of Time: Time, Occasion, Ever, Often "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_B(
			"B"
			, " A Point of Time with Reference to Other Points of Time: Before, Long Ago, Now, At the Same Time, When, About, After "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_C(
			"C"
			, " A Point of Time with Reference to Duration of Time: Beginning, End "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_D(
			"D"
			, " A Point of Time with Reference to Units of Time: Daybreak, Midday, Midnight, Late "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_E(
			"E"
			, " Duration of Time without Reference to Points or Units of Time: Time, Spend Time, Always, Eternal, Old, Immediately, Young "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_F(
			"F"
			, " Duration of Time with Reference to Some Point of Time: Until, Delay, Still, From "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_G(
			"G"
			, " Duration of Time with Reference to Some Unit of Time: During, In, While, Throughout "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_H(
			"H"
			, " Indefinite Units of Time: Age, Lifetime, Interval, Period "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_I(
			"I"
			, " Definite Units of Time: Year, Month, Week, Day, Hour "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_67_J(
			"J"
			, " Units of Time with Reference to Other Units or Points of Time: Yesterday, Today, Next Day "
			, SEMANTIC_DOMAINS.SD_67
		)
		, SD_68(
			""
			, "Aspect"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_68_A(
			"A"
			, " Begin, Start "
			, SEMANTIC_DOMAINS.SD_68
		)
		, SD_68_B(
			"B"
			, " Continue "
			, SEMANTIC_DOMAINS.SD_68
		)
		, SD_68_C(
			"C"
			, " Complete, Finish, Succeed "
			, SEMANTIC_DOMAINS.SD_68
		)
		, SD_68_E(
			"E"
			, " Try, Attempt "
			, SEMANTIC_DOMAINS.SD_68
		)
		, SD_68_F(
			"F"
			, " Do Intensely or Extensively "
			, SEMANTIC_DOMAINS.SD_68
		)
		, SD_68_G(
			"G"
			, " Rapidity, Suddenness "
			, SEMANTIC_DOMAINS.SD_68
		)
		, SD_69(
			""
			, "Affirmation, Negation"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_69_A(
			"A"
			, " Affirmation "
			, SEMANTIC_DOMAINS.SD_69
		)
		, SD_69_B(
			"B"
			, " Negation "
			, SEMANTIC_DOMAINS.SD_69
		)
		, SD_69_C(
			"C"
			, " Negation Combined with Clitics "
			, SEMANTIC_DOMAINS.SD_69
		)
		, SD_69_D(
			"D"
			, " Markers for an Affirmative Response to Questions "
			, SEMANTIC_DOMAINS.SD_69
		)
		, SD_69_E(
			"E"
			, " Markers for a Negative Response to Questions "
			, SEMANTIC_DOMAINS.SD_69
		)
		, SD_70(
			""
			, "Real, Unreal"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_71(
			""
			, "Mode"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_71_A(
			"A"
			, " Possible, Impossible "
			, SEMANTIC_DOMAINS.SD_71
		)
		, SD_71_B(
			"B"
			, " Probable, Improbable "
			, SEMANTIC_DOMAINS.SD_71
		)
		, SD_71_C(
			"C"
			, " Certain, Uncertain "
			, SEMANTIC_DOMAINS.SD_71
		)
		, SD_71_D(
			"D"
			, " Should, Ought "
			, SEMANTIC_DOMAINS.SD_71
		)
		, SD_71_E(
			"E"
			, " Necessary, Unnecessary "
			, SEMANTIC_DOMAINS.SD_71
		)
		, SD_72(
			""
			, "True, False"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_72_A(
			"A"
			, " True, False "
			, SEMANTIC_DOMAINS.SD_72
		)
		, SD_72_B(
			"B"
			, " Accurate, Inaccurate "
			, SEMANTIC_DOMAINS.SD_72
		)
		, SD_73(
			""
			, "Genuine, Phony"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_74(
			""
			, "Able, Capable"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_75(
			""
			, "Adequate, Qualified"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_76(
			""
			, "Power, Force"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_77(
			""
			, "Ready, Prepared"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_78(
			""
			, "Degree"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_78_A(
			"A"
			, " Much, Little [Positive-Negative Degree] "
			, SEMANTIC_DOMAINS.SD_78
		)
		, SD_78_B(
			"B"
			, " More Than, Less Than [Comparative Degree] "
			, SEMANTIC_DOMAINS.SD_78
		)
		, SD_78_C(
			"C"
			, " About, Approximately, Almost, Hardly [Approximate Degree] "
			, SEMANTIC_DOMAINS.SD_78
		)
		, SD_78_D(
			"D"
			, " Completely, Enough [Completive Degree] "
			, SEMANTIC_DOMAINS.SD_78
		)
		, SD_78_E(
			"E"
			, " Up To, As Much As, To the Degree That [Marked Extent of Degree] "
			, SEMANTIC_DOMAINS.SD_78
		)
		, SD_79(
			""
			, "Features of Objects"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_79_A(
			"A"
			, " Physical [Material], Spiritual "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_B(
			"B"
			, " Natural [Human], Spiritual "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_C(
			"C"
			, " Solid, Liquid "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_D(
			"D"
			, " Beautiful, Ugly "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_E(
			"E"
			, " Glorious "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_F(
			"F"
			, " Transparent "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_G(
			"G"
			, " Color "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_H(
			"H"
			, " Sweet, Bitter, Tasteless "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_I(
			"I"
			, " Fragrance, Odor "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_J(
			"J"
			, " Clean, Dirty "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_K(
			"K"
			, " Spotted, Spotless "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_L(
			"L"
			, " Blemished, Unblemished "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_M(
			"M"
			, " Strong, Weak "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_N(
			"N"
			, " Hot, Lukewarm, Cold "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_O(
			"O"
			, " Wet, Dry "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_P(
			"P"
			, " Uneven [Rough], Level [Smooth] "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_Q(
			"Q"
			, " Straight, Crooked "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_R(
			"R"
			, " Two-Dimensional and Three-Dimensional Shapes "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_S(
			"S"
			, " Sharp "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_T(
			"T"
			, " Pure, Unadulterated, Undiluted "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_U(
			"U"
			, " Soft, Tender "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_V(
			"V"
			, " Male, Female "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_W(
			"W"
			, " Shapes "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_X(
			"X"
			, " Open, Closed "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_Y(
			"Y"
			, " Covered Over "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_Z(
			"Z"
			, " Wrapped "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_ZA(
			"ZA"
			, " Rolled Up "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_ZB(
			"ZB"
			, " Large, Small "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_79_ZC(
			"ZC"
			, " Perfect "
			, SEMANTIC_DOMAINS.SD_79
		)
		, SD_80(
			""
			, "Space"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_80_A(
			"A"
			, " Space, Place "
			, SEMANTIC_DOMAINS.SD_80
		)
		, SD_80_B(
			"B"
			, " Limits, Boundaries of Space "
			, SEMANTIC_DOMAINS.SD_80
		)
		, SD_81(
			""
			, "Spacial Dimensions"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_81_A(
			"A"
			, " Measure, To Measure "
			, SEMANTIC_DOMAINS.SD_81
		)
		, SD_81_B(
			"B"
			, " High, Low, Deep "
			, SEMANTIC_DOMAINS.SD_81
		)
		, SD_81_C(
			"C"
			, " Long, Short, Far "
			, SEMANTIC_DOMAINS.SD_81
		)
		, SD_81_D(
			"D"
			, " Narrow, Wide "
			, SEMANTIC_DOMAINS.SD_81
		)
		, SD_81_E(
			"E"
			, " Specific Measures of Volume "
			, SEMANTIC_DOMAINS.SD_81
		)
		, SD_81_F(
			"F"
			, " Specific Measures of Length "
			, SEMANTIC_DOMAINS.SD_81
		)
		, SD_82(
			""
			, "Spacial Orientations"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_82_A(
			"A"
			, " North, South, East, West "
			, SEMANTIC_DOMAINS.SD_82
		)
		, SD_82_B(
			"B"
			, " Left, Right, Straight Ahead, Opposite "
			, SEMANTIC_DOMAINS.SD_82
		)
		, SD_83(
			""
			, "Spacial Positions"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_83_A(
			"A"
			, " Here, There "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_B(
			"B"
			, " Where, Somewhere, Everywhere "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_C(
			"C"
			, " Among, Between, In, Inside "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_D(
			"D"
			, " Around, About, Outside "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_E(
			"E"
			, " At, Beside, Near, Far "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_F(
			"F"
			, " In Front Of, Face To Face, In Back Of, Behind "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_G(
			"G"
			, " Opposite, Over Against, Across From, Offshore From "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_H(
			"H"
			, " On, Upon, On the Surface Of "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_I(
			"I"
			, " Above, Below "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_83_J(
			"J"
			, " Beyond, On the Other Side Of "
			, SEMANTIC_DOMAINS.SD_83
		)
		, SD_84(
			""
			, "Spacial Extensions"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_84_A(
			"A"
			, " Extension From a Source "
			, SEMANTIC_DOMAINS.SD_84
		)
		, SD_84_B(
			"B"
			, " Extension To a Goal "
			, SEMANTIC_DOMAINS.SD_84
		)
		, SD_84_C(
			"C"
			, " Extension Along a Path "
			, SEMANTIC_DOMAINS.SD_84
		)
		, SD_85(
			""
			, "Existence in Space"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_85_A(
			"A"
			, " Be in a Place "
			, SEMANTIC_DOMAINS.SD_85
		)
		, SD_85_B(
			"B"
			, " Put, Place "
			, SEMANTIC_DOMAINS.SD_85
		)
		, SD_85_C(
			"C"
			, " Remain, Stay "
			, SEMANTIC_DOMAINS.SD_85
		)
		, SD_85_D(
			"D"
			, " Leave in a Place "
			, SEMANTIC_DOMAINS.SD_85
		)
		, SD_85_E(
			"E"
			, " Dwell, Reside "
			, SEMANTIC_DOMAINS.SD_85
		)
		, SD_86(
			""
			, "Weight"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_86_A(
			"A"
			, " Heavy, Light "
			, SEMANTIC_DOMAINS.SD_86
		)
		, SD_86_B(
			"B"
			, " Pound, Talent [Specific Units of Weight] "
			, SEMANTIC_DOMAINS.SD_86
		)
		, SD_87(
			""
			, "Status"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_87_A(
			"A"
			, " Position, Rank "
			, SEMANTIC_DOMAINS.SD_87
		)
		, SD_87_B(
			"B"
			, " Honor or Respect in Relation to Status "
			, SEMANTIC_DOMAINS.SD_87
		)
		, SD_87_C(
			"C"
			, " High Status or Rank [including persons of high status] "
			, SEMANTIC_DOMAINS.SD_87
		)
		, SD_87_D(
			"D"
			, " Low Status or Rank [including persons of low status] "
			, SEMANTIC_DOMAINS.SD_87
		)
		, SD_87_E(
			"E"
			, " Slave, Free "
			, SEMANTIC_DOMAINS.SD_87
		)
		, SD_88(
			""
			, "Moral and Ethical Qualities and Related Behavior"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_88_A(
			"A"
			, " Goodness "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_B(
			"B"
			, " Just, Righteous "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_C(
			"C"
			, " Holy, Pure "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_D(
			"D"
			, " Perfect, Perfection "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_E(
			"E"
			, " Honesty, Sincerity "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_F(
			"F"
			, " Modesty, Propriety "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_G(
			"G"
			, " Humility "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_H(
			"H"
			, " Gentleness, Mildness "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_I(
			"I"
			, " Kindness, Harshness "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_J(
			"J"
			, " Mercy, Merciless "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_K(
			"K"
			, " Self-Control, Lack of Self-Control "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_L(
			"L"
			, " Sensible Behavior, Senseless Behavior "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_M(
			"M"
			, " Mature Behavior "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_N(
			"N"
			, " Peaceful Behavior "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_O(
			"O"
			, " Bad, Evil, Harmful, Damaging "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_P(
			"P"
			, " Treat Badly "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_Q(
			"Q"
			, " Act Harshly "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_R(
			"R"
			, " Act Lawlessly "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_S(
			"S"
			, " Exploit "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_T(
			"T"
			, " Act Shamefully "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_U(
			"U"
			, " Mislead, Lead Astray, Deceive "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_V(
			"V"
			, " Envy, Jealousy "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_W(
			"W"
			, " Resentful, Hold a Grudge Against "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_X(
			"X"
			, " Anger, Be Indignant With "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_Y(
			"Y"
			, " Despise, Scorn, Contempt "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_Z(
			"Z"
			, " Hate, Hateful "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZA(
			"ZA"
			, " Arrogance, Haughtiness, Pride "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZB(
			"ZB"
			, " Stubbornness "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZC(
			"ZC"
			, " Hypocrisy, Pretense "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZD(
			"ZD"
			, " Show Favoritism, Prejudice "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZE(
			"ZE"
			, " Being a Busybody "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZF(
			"ZF"
			, " Laziness, Idleness "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZG(
			"ZG"
			, " Extravagant Living, Intemperate Living "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZH(
			"ZH"
			, " Impurity "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZI(
			"ZI"
			, " Licentiousness, Perversion "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZJ(
			"ZJ"
			, " Sexual Misbehavior "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZK(
			"ZK"
			, " Drunkenness "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_88_ZL(
			"ZL"
			, " Sin, Wrongdoing, Guilt "
			, SEMANTIC_DOMAINS.SD_88
		)
		, SD_89(
			""
			, "Relations"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_89_A(
			"A"
			, " Relation "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_B(
			"B"
			, " Dependency "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_C(
			"C"
			, " Derivation "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_D(
			"D"
			, " Specification "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_E(
			"E"
			, " Relations Involving Correspondences [Isomorphisms] "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_F(
			"F"
			, " Basis "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_G(
			"G"
			, " Cause and/or Reason "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_H(
			"H"
			, " Result "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_I(
			"I"
			, " Purpose "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_J(
			"J"
			, " Condition "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_K(
			"K"
			, " Concession "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_L(
			"L"
			, " Means "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_M(
			"M"
			, " Attendant Circumstances "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_N(
			"N"
			, " Manner "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_O(
			"O"
			, " Sequential Addition "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_P(
			"P"
			, " Distribution "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_Q(
			"Q"
			, " Addition "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_R(
			"R"
			, " Linkage "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_S(
			"S"
			, " Equivalence "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_T(
			"T"
			, " Association "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_U(
			"U"
			, " Dissociation "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_V(
			"V"
			, " Combinative Relation "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_W(
			"W"
			, " Contrast "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_X(
			"X"
			, " Alternative Relation "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_Y(
			"Y"
			, " Substance "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_89_Z(
			"Z"
			, " Mediation "
			, SEMANTIC_DOMAINS.SD_89
		)
		, SD_90(
			""
			, "Case"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_90_A(
			"A"
			, " Agent, Personal or Nonpersonal, Causative or Immediate, Direct or Indirect "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_B(
			"B"
			, " Instrument "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_C(
			"C"
			, " Source of Event or Activity "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_D(
			"D"
			, " Responsibility "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_E(
			"E"
			, " Viewpoint Participant "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_F(
			"F"
			, " Content "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_G(
			"G"
			, " Guarantor Participant with Oaths "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_H(
			"H"
			, " Opposition "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_I(
			"I"
			, " Benefaction "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_J(
			"J"
			, " Reason Participant "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_K(
			"K"
			, " Agent of a Numerable Event "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_L(
			"L"
			, " Agent in a Causative Role Marked by Verbs "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_M(
			"M"
			, " Experiencer "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_90_N(
			"N"
			, " To Cause To Experience "
			, SEMANTIC_DOMAINS.SD_90
		)
		, SD_91(
			""
			, "Discourse Markers"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_91_A(
			"A"
			, " Markers of Transition "
			, SEMANTIC_DOMAINS.SD_91
		)
		, SD_91_B(
			"B"
			, " Markers of Emphasis "
			, SEMANTIC_DOMAINS.SD_91
		)
		, SD_91_C(
			"C"
			, " Prompters of Attention "
			, SEMANTIC_DOMAINS.SD_91
		)
		, SD_91_D(
			"D"
			, " Marker of Direct Address "
			, SEMANTIC_DOMAINS.SD_91
		)
		, SD_91_E(
			"E"
			, " Markers of Identificational and Explanatory Clauses [Epexegetical] "
			, SEMANTIC_DOMAINS.SD_91
		)
		, SD_92(
			""
			, "Discourse Referentials"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_92_A(
			"A"
			, " Speaker "
			, SEMANTIC_DOMAINS.SD_92
		)
		, SD_92_B(
			"B"
			, " Speaker and Those Associated with the Speaker [exclusive and inclusive] "
			, SEMANTIC_DOMAINS.SD_92
		)
		, SD_92_C(
			"C"
			, " Receptor, Receptors "
			, SEMANTIC_DOMAINS.SD_92
		)
		, SD_92_D(
			"D"
			, " Whom or What Spoken or Written About "
			, SEMANTIC_DOMAINS.SD_92
		)
		, SD_92_E(
			"E"
			, " Reciprocal Reference "
			, SEMANTIC_DOMAINS.SD_92
		)
		, SD_92_F(
			"F"
			, " Relative Reference "
			, SEMANTIC_DOMAINS.SD_92
		)
		, SD_92_G(
			"G"
			, " Demonstrative or Deictic Reference "
			, SEMANTIC_DOMAINS.SD_92
		)
		, SD_92_H(
			"H"
			, " Emphatic Adjunct "
			, SEMANTIC_DOMAINS.SD_92
		)
		, SD_93(
			""
			, "Names of Persons and Places"
			, SEMANTIC_DOMAINS.ROOT
		)
		, SD_93_A(
			"A"
			, " Persons "
			, SEMANTIC_DOMAINS.SD_93
		)
		, SD_93_B(
			"B"
			, " Places "
			, SEMANTIC_DOMAINS.SD_93
		)	
	;

	public String keyname = "";
	public String description = "";
	public SEMANTIC_DOMAINS hyponym;
	
	private SEMANTIC_DOMAINS(
			String keyname
			, String description
			, SEMANTIC_DOMAINS hyponym
			) {
		this.keyname = keyname;
		this.description = description;
		this.hyponym = hyponym;
	}
		
	/**
	 * Find the Topic for this string
	 * @param topicname
	 * @return ONTOLOGY_TOPICS topic
	 */
	public static SEMANTIC_DOMAINS forName(String name) {
		for (SEMANTIC_DOMAINS t : SEMANTIC_DOMAINS.values()) {
			if (t.keyname.equals(name)) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Creates a delimited string id for this role, library, and user 
	 * @param library
	 * @param user
	 * @return
	 */
	public String toId(String library, String user) {
		return Joiner.on(Constants.ID_DELIMITER).join(this.keyname, library, user);
	}

	/**
	 * Gets a colon delimited set of strings that represent
	 * the ontology hierarchy for this entry,
	 * from more generic to less.
	 * @return
	 */
	public String toDelimitedLabels() {
		return toLabels(this);
	}

	/**
	 * Returns the ontology hierarchy for this entry,
	 * from more generic to less.
	 * @return
	 */
	public List<String> toLabelsList() {
		List<String> result = new ArrayList<String>();
		String [] labels = toLabels(this).split(":");
		for (String label : labels) {
			result.add(label);
		}
		return result;
	}

	private String toLabels(SEMANTIC_DOMAINS topic) {
		StringBuffer result = new StringBuffer();
		if (topic == SEMANTIC_DOMAINS.ROOT) {
			result.append(topic.keyname);
		} else {
			result.append(toLabels(topic.hyponym));
			if (result.length() > 0) {
				result.append(":");
			}
			result.append(topic.keyname);
		}
		return result.toString();
	}
	
	/**
	 * Get the keynames as a sorted list of DropDownItem
	 * @return
	 */
	public static List<DropdownItem> keyNamesToDropdown() {
		List<DropdownItem> result = new ArrayList<DropdownItem>();
		TreeSet<String> values = new TreeSet<String>();
		for (SEMANTIC_DOMAINS t : SEMANTIC_DOMAINS.values()) {
			values.add(t.keyname);
		}
		for (String value : values) {
			result.add(new DropdownItem(value, value));
		}
		return result;
	}

	/**
	 * Get the keynames as a JsonArray of DropDownItem
	 * @return
	 */
	public static JsonArray keyNamesToJsonArrayDropdown() {
		JsonArray result = new JsonArray();
		TreeSet<String> values = new TreeSet<String>();
		for (SEMANTIC_DOMAINS t : SEMANTIC_DOMAINS.values()) {
			values.add(t.keyname);
		}
		for (String value : values) {
			result.add(new DropdownItem(value, value).toJsonObject());
		}
		return result;
	}

}
