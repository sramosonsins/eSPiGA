package es.cragenomica.espiga.workflow.manager;

import com.biotechvana.workflow.VariableBag;

/**
 * class to hold sum static variables that can be fixed amonge all jobs
 * @author ahafez
 *
 */
public class eSPiGAVariableBag extends VariableBag {

	public static final String INPUT_CSV_FILES= INPUT_FILES;
	
	public static final String INPUT_CSV_FILE = INPUT_FILES;
	public static String INPUT_FILES_FORMAT = "INPUT_FILES_FORMAT";
	public static String OUTPUT_FILES_FORMAT = "OUTPUT_FILES_FORMAT";
	public static String OPTIONAL_CUSTOM_ORDER = "OPTIONAL_CUSTOM_ORDER";
	public static String OPTIONAL_N_SAMPLES = "OPTIONAL_N_SAMPLES";
	public static String OPTIONAL_SAMPLES_OEDER = "OPTIONAL_SAMPLES_OEDER";

	
	public static String INPUT_GFF_FILE = "INPUT_GFF_FILE";
	public static String INPUT_GFF_TYPE = "INPUT_GFF_TYPE";
	public static String INPUT_GENETIC_CODE = "INPUT_GENETIC_CODE";

	
	public static String OPTION_GFF_TYPE_CODING = "coding";
	public static String OPTION_GFF_TYPE_NONCODING = "noncoding";
	public static String OPTION_GFF_TYPE_SYNONYMOUS = "synonymous";
	public static String OPTION_GFF_TYPE_NONSYNONYMOUS = "nonsynonymous";
	public static String OPTION_GFF_TYPE_SILENT = "silent";
	public static String OPTION_GFF_TYPE_OTHERS = "others";
	
	public static String OPTION_GENETIC_CODE_NUCLEAR_UNIVERSAL = "Nuclear_Universal";
	public static String OPTION_GENETIC_CODE_MTDNA_DROSOPHILA = "mtDNA_Drosophila";
	public static String OPTION_GENETIC_CODE_MTDNA_MAMMALS = "mtDNA_Mammals";
	public static String OPTION_GENETIC_CODE_OTHER = "Other";

	
	
}
