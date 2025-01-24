package es.cragenomica.espiga.jobs;

import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.VariablesRule;
import com.biotechvana.workflow.WorkflowConstants;
import com.biotechvana.workflow.WorkflowJob;
import com.biotechvana.workflow.VariablesRule.Action;
import com.biotechvana.workflow.VariablesRule.Condition;
import com.biotechvana.workflow.descriptors.CheckedVariableDescriptor;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.variables.FileVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class MstatspopGeneralJob extends MstatspopJob {

	static public final String JOB_ID = "mstatspop";
	static public final String JOB_NAME = "mstatspop";
	private static final String GENERAL_GROUP = "GENERAL PARAMETERS";
	private static final String INPUT_FILES_FORMAT_DUMMY = "fformat";

	public static String INPUT_FILES = eSPiGAVariableBag.INPUT_FILES;
	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;

	public static String OUTPUT_FILES_FORMAT = "OUTPUT_FILES_FORMAT";

	/**
	 * extended format
	 */
	public static String OUTPUT_FILES_FORMAT_0 = "extended";
	/**
	 * single line/window
	 */
	public static String OUTPUT_FILES_FORMAT_1 = " single line/window";
	/**
	 * single line SFS/window
	 */
	public static String OUTPUT_FILES_FORMAT_2 = "single line SFS/window";
	/**
	 * dadi-like format
	 */
	public static String OUTPUT_FILES_FORMAT_3 = "dadi-like format";
	/**
	 * single line pairwise distribution
	 */
	public static String OUTPUT_FILES_FORMAT_4 = "single line pairwise distribution";
	/**
	 * single line freq. variant per line/window
	 */
	public static String OUTPUT_FILES_FORMAT_5 = "single line freq. variant per line/window";
	/**
	 * SNP genotype matrix
	 */
	public static String OUTPUT_FILES_FORMAT_6 = "SNP genotype matrix";
	/**
	 * SweepFinder format [only first pop]
	 */
	public static String OUTPUT_FILES_FORMAT_7 = "SweepFinder format [only first pop]";
	/**
	 * full extended
	 */
	public static String OUTPUT_FILES_FORMAT_10 = "full extended";

	public static String INPUT_N_POPULATION = "INPUT_N_POPULATION";

	public static String INPUT_POPULATIONS_SIZE = "INPUT_POPULATIONS_SIZE";
	public static String INPUT_SCAFFOLD_NAMES = "INPUT_SCAFFOLD_NAMES";

	public MstatspopGeneralJob() {
		super(JOB_ID, JOB_NAME, true, false);

		this.jobDesc.addPublication(new Publication().setAuthors(
				"Sebastian E. Ramos-Onsins, Luca Ferretti, Emanuele Raineri, Giacomo Marmorini, William Burgos, Joan Jene and Gonzalo Vera")
				.setTitle(
						"Variability Analyses of multiple populations: Calculation and estimation of statistics and neutrality tests")
//				.setJournal("Genome research")
//				.setVolume(22)
//				.setIssue(3)
//				.setYear(2012)
//				.setPages("568-576")
//				.setDOI("10.1101/gr.129684.111")
//				.setPubmed("22300766")
		);
		this.jobDesc.addLink(new JobLink().setTitle("mstatspop").setUrl("https://github.com/CRAGENOMICA/mstatspop")
				.setType(JobLink.LinkType.Other));
	}

	@Override
	protected void createInputs() {

//	      -i [path and name of the input file]  
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES, VariableType.File,
				VariableRole.Input);
		varInputFile.isRequired = true;
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_MS, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, false, false);
		varInputFile.setVariableName("Input file");
		varInputFile.setShortHelpMsg("Drag an input file [ms, fasta or tfa]");
		varInputFile.setHelpMsg("input file: can be a ms, fasta or tfa (gz file indexed)");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);

//	      -f [input format file: ms, fasta OR tfa (gz file indexed)]  
		VariableDescriptor inputFormatVar = VariableDescriptor.SelectionList(INPUT_FILES_FORMAT, VariableRole.Input)
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFA,eSPiGAWorkflowConstants.FILE_FORMAT_TFA,"SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA")
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_MS,eSPiGAWorkflowConstants.FILE_FORMAT_MS,"SIMULATION ANALYSIS OF A SINGLE REGION")
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,"WHOLE REGION ANALYSIS")
				.setDefaultIndexValue(0)
				.setCommandParamater("-f").setVariableName("input format file").setIsRequired(true);
		jobDesc.addVariable(inputFormatVar);

//	      -o [output format file: 0 (extended),  
//        1 (single line/window),  
//        2 (single line SFS/window),  
//        3 (dadi-like format),  
//        4 (single line pairwise distribution)  
//        5 (single line freq. variant per line/window)  
//        6 (SNP genotype matrix)  
//        7 (SweepFiinder format -only first pop-)  
//       10 (full extended)]
		VariableDescriptor outputFormatVar = VariableDescriptor.SelectionList(OUTPUT_FILES_FORMAT, VariableRole.Input)
				.addOptionValue(OUTPUT_FILES_FORMAT_0, "0", OUTPUT_FILES_FORMAT_0)
				.addOptionValue(OUTPUT_FILES_FORMAT_1, "1", OUTPUT_FILES_FORMAT_1)
				.addOptionValue(OUTPUT_FILES_FORMAT_2, "2", OUTPUT_FILES_FORMAT_2)
				.addOptionValue(OUTPUT_FILES_FORMAT_3, "3", OUTPUT_FILES_FORMAT_3)
				.addOptionValue(OUTPUT_FILES_FORMAT_4, "4", OUTPUT_FILES_FORMAT_4)
				.addOptionValue(OUTPUT_FILES_FORMAT_5, "5", OUTPUT_FILES_FORMAT_5)
				.addOptionValue(OUTPUT_FILES_FORMAT_6, "6", OUTPUT_FILES_FORMAT_6)
				.addOptionValue(OUTPUT_FILES_FORMAT_7, "7", OUTPUT_FILES_FORMAT_7)
				.addOptionValue(OUTPUT_FILES_FORMAT_10, "10", OUTPUT_FILES_FORMAT_10).setDefaultIndexValue(0)
				.setCommandParamater("-o").setVariableName("Output file format").setIsRequired(true);
		jobDesc.addVariable(outputFormatVar);

//	    	      -N [#_pops] [#samples_pop1] ... [#samples_popN]
		// TODO :: we need ints list
		// TODO :: fix OPTION_AUTO with inputs
		VariableDescriptor nPopsVar = VariableDescriptor.Int(INPUT_N_POPULATION, VariableRole.Input)
				// .setCommandParamater("-N")
				.setMinValue(1).setVariableName("#N Population").setIsRequired(true)
				.setHelpMsg("Numner of Populations");
		jobDesc.addVariable(nPopsVar);

		jobDesc.addVariable(VariableDescriptor.Strings(INPUT_POPULATIONS_SIZE, VariableRole.Input)
				// .setCommandParamater("-N")
				.setVariableName("N Populations").setIsRequired(true).setHelpMsg("N Samples per populations"));
//	      -n [name of a single scaffold to analyze. For tfa can be a list separated by commas(ex. -n chr1,chr2,chr3]

		VariableDescriptor scaffoldNamesVar = VariableDescriptor.Strings(INPUT_SCAFFOLD_NAMES, VariableRole.Input)
				// .setCommandParamater("-N")
				.setVariableName("scaffold to analyze")
				.setHelpMsg("A single name of a single scaffold to analyze, or a list for tfa");
		jobDesc.addVariable(scaffoldNamesVar);

//	    	      -T [path and name of the output file]. DEFAULT stdout.

	}

	@Override
	protected void createOutputs() {
		// TODO Auto-generated method stub
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES, VariableType.FileList,
				VariableRole.Output);
		varInputFile.isRequired = true;
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_MS, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, false, false);
		varInputFile.setVariableName("Input file");
		varInputFile.setShortHelpMsg("Drag an input file [ms, fasta or tfa]");
		varInputFile.setHelpMsg("input file: can be a ms, fasta or tfa (gz file indexed)");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);

	}

	public static String OPTIONAL_CUSTOM_ORDER = "OPTIONAL_CUSTOM_ORDER";

	@Override
	protected void createOptions() {
		// TODO Fix duplicate input and option var for such cases
		VariableDescriptor varOptions;
		varOptions = VariableDescriptor.SelectionList(INPUT_FILES_FORMAT_DUMMY, VariableRole.Option)
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFA)
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_MS)
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA).setDefaultIndexValue(0)
				.setVariableName("input format file").setGUIVisible(false);
		jobDesc.addVariable(varOptions);

//	    Optional Parameters for fasta and tfa input files:
//	        -O [#_nsam] [number order of first sample, number 0 is the first sample] [second sample] ...etc. up to nsamples.
//	           DEFAULT current order.
		jobDesc.addVariable(VariableDescriptor.Checked(OPTIONAL_CUSTOM_ORDER, VariableRole.Option)
				.setVariableName("Provide Custom samples order"));
		jobDesc.addVariable(
				VariableDescriptor.Strings(OPTION_AUTO, VariableRole.Option).setVariableName("Samples order")
						.setCommandParamater("-O").setHelpMsg("Samples custom Order. Number 0 is the first sample"));
		
//	        -t [# permutations per window (H0: Fst=0). Only available with option -u 0]. DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setVariableName("permutations per window")
				.setHelpMsg("# permutations per window (H0: Fst=0). Only available with option -u 0]. DEFAULT 0")
				.setCommandParamater("-t"));
//	        -s [seed]. DEFAULT 123456.
		varOptions = VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option).setVariableName("seed")
				.setHelpMsg("randown seed. DEFAULT 123456").setDefaultValue("123456").setCommandParamater("-s")
				.setVariableGroup(GENERAL_GROUP);
		jobDesc.addVariable(varOptions);

//		   PARAMETERS FOR TFASTA INPUT (-f tfa): 'SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA'
//			      -w [window size].
		varOptions = VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input).setVariableName("Window size")
				.setHelpMsg("SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA.").setCommandParamater("-w");
		jobDesc.addVariable(varOptions);
//			    Optional:
//			      -z [slide size (must be a positive value)]. DEFAULT window size.
		varOptions = VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option).setMinValue(0)
				.setVariableName("slide size").setHelpMsg("slide size (must be a positive value). DEFAULT window size")
				.setCommandParamater("-z");
		jobDesc.addVariable(varOptions);
//			      -Z [first window size displacement [for comparing overlapped windows])]. DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option).setMinValue(0)
				.setVariableName("first window size displacement")
				.setHelpMsg("first window size displacement [for comparing overlapped windows]. DEFAULT 0")
				.setCommandParamater("-Z"));
//			      -Y [define window lengths in 'physical' positions (1) or in 'effective' positions (0)]. DEFAULT 1.
		jobDesc.addVariable(VariableDescriptor.SelectionList(OPTION_AUTO, VariableRole.Option)
				.addOptionValue("physical", "1", "define window lengths in physical positions")
				.addOptionValue("effective", "0", "define window lengths in effective positions")
				.setDefaultIndexValue(0)
				.setCommandParamater("-Y").setVariableName("Window Lengths Definition"));

//			      -W [file with the coordinates of each window [scaffold init end] (instead options -w and -z).
//			         DEFAULT one whole window.
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option).setCommandParamater("-W")
				.setVariableName("File with the coordinates of each window").setHelpMsg(
						"file with the coordinates of each window [scaffold init end] (instead options -w and -z)"));
//			      -E [input file with weights for positions:
//			         include three columns with a header,
//			         first the physical positions (1...end),
//			         second the weight for positions and
//			         third a boolean weight for the variant (eg. syn variant in nsyn counts is 0.000)].
//			         DEFAULT all 1.000
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option).setCommandParamater("-E")
				.setVariableName("Input file with weights for positions")
				.setHelpMsg("input file with weights for positions:\n" + "include three columns with a header,\n"
						+ "first the physical positions (1...end),\n" + "second the weight for positions and\n"
						+ "third a boolean weight for the variant (eg. syn variant in nsyn counts is 0.000)].\n"
						+ "DEFAULT all 1.000"));

//		  PARAMETERS FOR MS INPUT (-f ms):'SIMULATION ANALYSIS OF A SINGLE REGION'
//		      -l [length]
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input).setMinValue(0)
				.setVariableName("length")
				.setHelpMsg("Length")
				.setCommandParamater("-l"));
//		    Optional:
//		      -r [# ms iterations]. DEFAULT 1.
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option).setMinValue(1)
				.setVariableName("ms iterations")
				.setHelpMsg("#ms iterations")
				.setCommandParamater("-r"));
//		      -m [include mask_filename] DEFAULT -1 (all positions included).
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
				.setVariableName("Mask File")
				.setHelpMsg("mask_file format: 1st row with 'length' weights, next sample rows x lengths: missing 0, sequenced 1")
				.setCommandParamater("-m"));
//		         [mask_file format: 1st row with 'length' weights, next sample rows x lengths: missing 0, sequenced 1)].
//		         DEFAULT no mask.
//		      -v [ratio transitions/transversions]. DEFAULT 0.5.
		jobDesc.addVariable(VariableDescriptor.Double(OPTION_AUTO, VariableRole.Option).setMinValue(0)
				.setVariableName("ratio transitions/transversions")
				.setHelpMsg("ratio transitions/transversions. DEFAULT 0.5.")
				.setCommandParamater("-v"));
//		      -F [force analysis to include outgroup (0/1) (0 in ms means ancestral)]. DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1)
				.setVariableName("force analysis to include outgroup")
				.setHelpMsg("force analysis to include outgroup (0/1) (0 in ms means ancestral). DEFAULT 0")
				.setCommandParamater("-F"));
//		      -q [frequency of reverted mutation] (only with -F 1). DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Double(OPTION_AUTO, VariableRole.Option).setMinValue(0)
				.setVariableName("frequency of reverted mutation")
				.setHelpMsg("frequency of reverted mutation (only with -F 1). DEFAULT 0.")
				.setCommandParamater("-q"));
		
		
//		   PARAMETERS FOR FASTA INPUT (-f fasta): 'WHOLE REGION ANALYSIS'
//		    Optional:
//		      -p [Number of lineages per sequence (1/2)]. DEFAULT 1.
		jobDesc.addVariable(VariableDescriptor.Range(OPTION_AUTO, VariableRole.Option)
				.setMinValue(1)
				.setMaxValue(2)
				.setVariableName("Number of lineages per sequence ")
				.setHelpMsg("Number of lineages per sequence (1/2). DEFAULT 1")
				.setCommandParamater("-p"));
//		      -g [GFF_file]
//		         [add also: coding,noncoding,synonymous,nonsynonymous,silent, others (or whatever annotated)]
//		         [if 'synonymous', 'nonsynonymous', 'silent' add: Genetic_Code: Nuclear_Universal,mtDNA_Drosophila,mtDNA_Mammals,Other]
//		         [if 'Other', introduce the code for the 64 triplets in the order UUU UUC UUA UUG ... etc.].
//		         DEFAULT no annotation.
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
				.setVariableName("GFF annotation file")
				.setHelpMsg("GFF annotation file. DEFAULT no annotation.")
				.setCommandParamater("-g"));
//		      -c [in case use coding regions, criteria to consider transcripts (max/min/first/long)]. DEFAULT long.
		jobDesc.addVariable(VariableDescriptor.SelectionList(OPTION_AUTO, VariableRole.Option)
				.addOptionValue("long")
				.addOptionValue("first")
				.addOptionValue("max")
				.addOptionValue("min")
				.setVariableName("Criteria to consider transcripts")
				.setCommandParamater("-c")
				.setHelpMsg("in case use coding regions, criteria to consider transcripts")
			);
//		      -K [make a MASK file with the valid positions for this fasta. Useful for running ms simulations (1/0)]. DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1)
				.setVariableName("make a MASK file with the valid positions")
				.setHelpMsg("make a MASK file with the valid positions for this fasta. Useful for running ms simulations (1/0). DEFAULT 0")
				.setCommandParamater("-K"));
		
		
//		   OPTIONAL GENERAL PARAMETERS:
//	      -G [outgroup (0/1)] (last population). DEFAULT 0.
		varOptions = VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1).setVariableName("last population")
				.setHelpMsg("outgroup (0/1) (last population). DEFAULT 0").setDefaultValue("").setCommandParamater("-G")
				.setVariableGroup(GENERAL_GROUP);
		jobDesc.addVariable(varOptions);

//	      -u [include unknown positions (0/1)].  DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1).setVariableName("Include unknown positions")
				.setHelpMsg("Include unknown positions (0/1). DEFAULT 0").setDefaultValue("").setCommandParamater("-u")
				.setVariableGroup(GENERAL_GROUP));
//	      -A [Alternative Spectrum File (Only for Optimal Test): alternative_spectrum for each population (except outg)
//	          File format: (average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,
//	          fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]
//	      -S [Null Spectrum File (only if -a is defined): null_spectrum for each population (except outg).
//	          (average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,
//	          fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]. DEFAULT SNM.
//	      -P [Only for Calculation of R2_p: first value is the number of values to include, 
//	                       next are the number of lines to consider. ex: -P 6 1 2 4 8 16 64]
	}

	@Override
	public boolean notifyVarValueChanged(String varKey, String value) {
		try {
//			System.out.println(" Tophat notifyVarValueChanged" + varKey + " " + ((FileVariable) getVariable(INPUT_FASTQ_FILE)).getIsPairedFiles());
			if (varKey.equals(INPUT_FILES_FORMAT)) {
				getVariable(INPUT_FILES_FORMAT_DUMMY).setValue(value);
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		return true;
	}

	@Override
	protected void createRules() {
		// Input vars
		new VariablesRule(getVariable(INPUT_FILES_FORMAT), getVariableByCMDSwitch("-w"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);

		
		new VariablesRule(getVariable(INPUT_FILES_FORMAT), getVariableByCMDSwitch("-l"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_MS, true);
		// optional FOR tfs and fasta
				new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariable(OPTIONAL_CUSTOM_ORDER), Condition.EqualTo,
						Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);
				new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariable(OPTIONAL_CUSTOM_ORDER), Condition.EqualTo,
						Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, true);
				new VariablesRule(getVariable(OPTIONAL_CUSTOM_ORDER), getVariableByCMDSwitch("-O"), Condition.True,
						Action.Enable, true);
				
				new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-t"), Condition.EqualTo,
						Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);
				new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-t"), Condition.EqualTo,
						Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, true);
				
				new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-s"), Condition.EqualTo,
						Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);
				new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-s"), Condition.EqualTo,
						Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, true);
		
		
		
		// optional FOR tfs  only
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-z"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-Z"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-Y"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-W"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-E"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_TFA, true);
		
		// optional FOR fasta  only
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-p"), Condition.EqualTo,
						Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, true);
		
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-g"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, true);
		
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-c"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, true);
		
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-K"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, true);
		
		
		// optional FOR ms  only
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-r"), Condition.EqualTo,
							Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_MS, true);
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-m"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_MS, true);
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-v"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_MS, true);
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-F"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_MS, true);
		new VariablesRule(getVariable(INPUT_FILES_FORMAT_DUMMY), getVariableByCMDSwitch("-q"), Condition.EqualTo,
				Action.Enable, eSPiGAWorkflowConstants.FILE_FORMAT_MS, true);
		
		
		
		
		
		// Other variables
		new VariablesRule(getVariableByCMDSwitch("-u"), getVariableByCMDSwitch("-t"), Condition.True, Action.Reset,
				false);
		
	}

}
