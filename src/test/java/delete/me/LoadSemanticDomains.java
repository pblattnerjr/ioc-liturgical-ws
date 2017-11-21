package delete.me;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import net.ages.alwb.utils.core.file.AlwbFileUtils;

public class LoadSemanticDomains {

	/**
	 * 1 Geographical Objects and Features
	 * A Universe, Creation (1.1-1.4)
	 * 
	 * {1}{1 Geographic Objects and Features}%    
	 * {1.A}{A. Universe, Creation}%
	 */
	public static void main(String[] args) {
		String pathIn = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/main/java/net/ages/alwb/utils/nlp/constants/semanticDomains.txt";
		String pathOutEnums = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/main/java/net/ages/alwb/utils/nlp/constants/semanticDomainsEnums.txt";
		String pathOutLatex = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/main/java/net/ages/alwb/utils/nlp/constants/semanticDomainsLatex.txt";
		String pathOutLatexPrint = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/main/java/net/ages/alwb/utils/nlp/constants/semanticDomainsLatexPrint.txt";
		StringBuffer sbEnums = new StringBuffer();
		StringBuffer sbLatex = new StringBuffer();
		StringBuffer sbLatexPrint = new StringBuffer();
		String[] lineParts = null;
		String parent = "SEMANTIC_DOMAINS.ROOT";
		String level1 = "";
		String level2 = "";
		String text = "";
		for (String line : AlwbFileUtils.linesFromFile(new File(pathIn))) {
			String id = line.split(" ")[0].trim();
			if (StringUtils.isNumeric(id)) {
				parent = "SEMANTIC_DOMAINS.ROOT";
				level1 = id;
				level2 = "";
				text = line.substring(level1.length()).trim();
				sbLatex.append("{");
				sbLatex.append(id);
				sbLatex.append("}{\\ltSt{");
				sbLatex.append(id);
				sbLatex.append(".");
				sbLatex.append(text);
				sbLatex.append("}}%\n");
				
			} else {
				parent = "SEMANTIC_DOMAINS." + "SD_" + level1;
				level2 = id;
				if (level2.endsWith("'")) {
					level2 = "Z" + level2.substring(0, 1);
				}
				lineParts = line.split("\\(");		
				text = lineParts[0].substring(id.length());
				sbLatex.append("{");
				sbLatex.append(level1);
				sbLatex.append(".");
				sbLatex.append(level2);
				sbLatex.append("}{\\ltSt{");
				sbLatex.append(level2);
				sbLatex.append(".");
				sbLatex.append(text);
				sbLatex.append("}}%\n");			
			}
			sbEnums.append(", SD_");
			sbEnums.append(level1);
			if (level2.length() > 0) {
				sbEnums.append("_");
				sbEnums.append(level2);
			}
			sbEnums.append("(\n");
			sbEnums.append("\t\"");
			sbEnums.append(level2);
			sbEnums.append("\"\n\t, \"");
			sbEnums.append(text);
			sbEnums.append("\"\n\t, ");
			sbEnums.append(parent);
			sbEnums.append("\n)\n");		

			sbLatexPrint.append("%");
			if (level2.length() < 1 ) {
				sbLatexPrint.append("\n%\\textbf{");
			}
			sbLatexPrint.append(line);
			if (level2.length() < 1) {
				sbLatexPrint.append("}");
				sbLatexPrint.append("\\\\ \\newline\n");
			} else {
				sbLatexPrint.append("\\\\ \n");
			}
		}
		AlwbFileUtils.writeFile(pathOutEnums, sbEnums.toString());
		AlwbFileUtils.writeFile(pathOutLatex, sbLatex.toString());
		AlwbFileUtils.writeFile(pathOutLatexPrint, sbLatexPrint.toString());
	}

}
