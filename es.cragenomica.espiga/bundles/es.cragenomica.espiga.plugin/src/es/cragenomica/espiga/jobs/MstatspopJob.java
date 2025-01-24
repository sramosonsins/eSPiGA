package es.cragenomica.espiga.jobs;

import java.util.ArrayList;
import java.util.List;

import com.biotechvana.workflow.CmdListMergeStrategy;
import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.VariableCustomAction;
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
import com.biotechvana.workflow.variables.CheckedJobVariable;
import com.biotechvana.workflow.variables.FileVariable;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public abstract class MstatspopJob extends ISWFJob {
	public static String OPTIONAL_CUSTOM_ORDER = "OPTIONAL_CUSTOM_ORDER";

	static public final String JOB_ID = "mstatspop";
	static public final String JOB_NAME = "mstatspop";
	public static final String GENERAL_GROUP = "GENERAL PARAMETERS";
	public static final String INPUT_FILES_FORMAT_DUMMY = "fformat";

	public static String INPUT_FILES = eSPiGAVariableBag.INPUT_FILES;
	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;

	public static String INPUT_FILES_FORMAT = eSPiGAVariableBag.INPUT_FILES_FORMAT;
	public static String OUTPUT_FILES_FORMAT = eSPiGAVariableBag.OUTPUT_FILES_FORMAT;

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

	public static String OPTIONAL_N_SAMPLES = eSPiGAVariableBag.OPTIONAL_N_SAMPLES;
	public static String OPTIONAL_SAMPLES_OEDER = eSPiGAVariableBag.OPTIONAL_SAMPLES_OEDER;

	public MstatspopJob() {
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

	// Custom logic for this job
	private Integer nPopulation = null;
	private List<Integer> populationSizes = new ArrayList<Integer>();
	private Integer nSamples = null;

	public MstatspopJob(String jobId, String jobName, boolean createJobOutputFolderVar, boolean isParallelJob) {
		super(jobId, jobName, createJobOutputFolderVar, isParallelJob);
	}

	protected void createInputFasta() {
//	      -f [input format file: ms, fasta OR tfa (gz file indexed)]  
		VariableDescriptor inputFormatVar = VariableDescriptor.SelectionList(INPUT_FILES_FORMAT, VariableRole.Input)
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, eSPiGAWorkflowConstants.FILE_FORMAT_TFA,
						"SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA")
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_MS, eSPiGAWorkflowConstants.FILE_FORMAT_MS,
						"SIMULATION ANALYSIS OF A SINGLE REGION")
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,
						"WHOLE REGION ANALYSIS")
				.setDefaultIndexValue(2).setCommandParamater("-f").setVariableName("input format file")
				.setIsRequired(true).setReadOnly(true);
		jobDesc.addVariable(inputFormatVar);

		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES, VariableType.File,
				VariableRole.Input);
		varInputFile.isRequired = true;
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_MS, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, false, false);
		varInputFile.setVariableName("Input fasta file");
		varInputFile.setShortHelpMsg("Drag an input file [fasta]");
		varInputFile.setHelpMsg("input file: a fasta file");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);

	}

	protected void createInputTfasta() {

//	      -f [input format file: ms, fasta OR tfa (gz file indexed)]  
		VariableDescriptor inputFormatVar = VariableDescriptor.SelectionList(INPUT_FILES_FORMAT, VariableRole.Input)
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, eSPiGAWorkflowConstants.FILE_FORMAT_TFA,
						"SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA")
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_MS, eSPiGAWorkflowConstants.FILE_FORMAT_MS,
						"SIMULATION ANALYSIS OF A SINGLE REGION")
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,
						"WHOLE REGION ANALYSIS")
				.setDefaultIndexValue(0).setCommandParamater("-f").setVariableName("input format file")
				.setIsRequired(true).setReadOnly(true);
		jobDesc.addVariable(inputFormatVar);

		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES, VariableType.File,
				VariableRole.Input);
		varInputFile.isRequired = true;
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_MS, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, false, false);
		varInputFile.setVariableName("Input tfa file");
		varInputFile.setShortHelpMsg("Drag an input file [tfa]");
		varInputFile.setHelpMsg("input file: a tfa file (gz file indexed)");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);
	}

	protected void createInputMs() {
//	      -f [input format file: ms, fasta OR tfa (gz file indexed)]  
		VariableDescriptor inputFormatVar = VariableDescriptor.SelectionList(INPUT_FILES_FORMAT, VariableRole.Input)
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, eSPiGAWorkflowConstants.FILE_FORMAT_TFA,
						"SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA")
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_MS, eSPiGAWorkflowConstants.FILE_FORMAT_MS,
						"SIMULATION ANALYSIS OF A SINGLE REGION")
				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,
						"WHOLE REGION ANALYSIS")
				.setDefaultIndexValue(1).setCommandParamater("-f").setVariableName("input format file")
				.setIsRequired(true).setReadOnly(true);
		jobDesc.addVariable(inputFormatVar);

		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES, VariableType.File,
				VariableRole.Input);
		varInputFile.isRequired = true;
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_MS, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, false, false);
		varInputFile.setVariableName("Input ms file");
		varInputFile.setShortHelpMsg("Drag an input file [ms]");
		varInputFile.setHelpMsg("input file: a ms file");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);
	}

	@Override
	protected void createInputs() {
//	      -i [path and name of the input file]  
//	      -f [input format file: ms, fasta OR tfa (gz file indexed)]  

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
				.addOptionValue(OUTPUT_FILES_FORMAT_10, "10", OUTPUT_FILES_FORMAT_10)
				.setDefaultIndexValue(0)
				.setCommandParamater("-o")
				.setVariableName("Output file format")
				.setIsRequired(true)
				.setVariableGroup(WorkflowConstants.JOBS_OUTPUT_GROUP);
		jobDesc.addVariable(outputFormatVar);

//	    	      -N [#_pops] [#samples_pop1] ... [#samples_popN]
		// TODO :: we need ints list
		// TODO :: fix OPTION_AUTO with inputs
		VariableDescriptor nPopsVar = VariableDescriptor.Int(INPUT_N_POPULATION, VariableRole.Input)
				// .setCommandParamater("-N")
				.setMinValue(1)
				.setVariableName("N Population")
				.setIsRequired(true)
				.setHelpMsg("Number of Populations");
		jobDesc.addVariable(nPopsVar);

		jobDesc.addVariable(VariableDescriptor.Strings(INPUT_POPULATIONS_SIZE, VariableRole.Input)
				// .setCommandParamater("-N")
				.setCmdListMergerSep(" ")
				.setVariableName("Samples per Population")
				.setIsRequired(true)
				.setHelpMsg("N Samples per populations"));
//	      -n [name of a single scaffold to analyze. For tfa can be a list separated by commas(ex. -n chr1,chr2,chr3]
		VariableDescriptor scaffoldNamesVar = VariableDescriptor.File(INPUT_SCAFFOLD_NAMES, VariableRole.Input)
				.setCommandParamater("-n")
				.setVariableName("Scaffolds Info")
				.setHelpMsg("file containing the name(s) of scaffold(s) and their length (separated by a tab), one per line (ex. fai file)")
				.setIsRequired(true);
		jobDesc.addVariable(scaffoldNamesVar);

//	    	      -T [path and name of the output file]. DEFAULT stdout.

	}

	protected void createFastaTfasteOptional() {
		jobDesc.addVariable(VariableDescriptor.Int(eSPiGAVariableBag.OPTIONAL_N_SAMPLES, VariableRole.Option)
				.setVariableName("Number of Samples").setReadOnly(true));
		jobDesc.addVariable(VariableDescriptor.Checked(eSPiGAVariableBag.OPTIONAL_CUSTOM_ORDER, VariableRole.Option)
						.setVariableName("Provide Custom samples order"));

		jobDesc.addVariable(VariableDescriptor.Strings(eSPiGAVariableBag.OPTIONAL_SAMPLES_OEDER, VariableRole.Option)
						.setCmdListMergerSep(" ").setCmdListMergeStrategy(CmdListMergeStrategy.Repeat)
						.setVariableName("Samples order")
						// .setCommandParamater("-O")
						.setHelpMsg("Samples custom Order. Number 0 is the first sample")
						.addCustomAction(new VariableCustomAction("Reset", VariableCustomAction.Button,
								new VariableCustomAction.CustomActionCallback() {

									@Override
									public void call(JobVariable var) {
										WorkflowJob workflowJob = var.getJob();
										String value = workflowJob.getValue(eSPiGAVariableBag.OPTIONAL_N_SAMPLES);
										if (value.isEmpty()) {
											var.setValue("");
										} else {
											int n_samples = Integer.parseInt(value);
											String samples_ids = "";
											for (int i = 0; i < n_samples; i++) {
												samples_ids += i + JobVariable.LISTITEM_VALUE_EOL;
											}
											var.setValue(samples_ids);
										}
									}

								})));

//        -t [# permutations per window (H0: Fst=0). Only available with option -u 0]. DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setVariableName("permutations per window")
				.setHelpMsg("# permutations per window (H0: Fst=0). Only available with option -u 0]. DEFAULT 0")
				.setCommandParamater("-t"));
//        -s [seed]. DEFAULT 123456.
		VariableDescriptor varOptions = VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option).setVariableName("seed")
				.setHelpMsg("randown seed. DEFAULT 123456").setCommandParamater("-s");
		jobDesc.addVariable(varOptions);
	}

	

	@Override
	public boolean notifyVarValueChanged(String varKey, String value) {
		// TODO Auto-generated method stub
		if (varKey.equals(OPTIONAL_N_SAMPLES)) {
			if (value.isEmpty()) {
				setValue(OPTIONAL_SAMPLES_OEDER, "");
			} else {
				int n_samples = Integer.parseInt(value);
				String samples_ids = "";
				for (int i = 0; i < n_samples; i++) {
					samples_ids += i + JobVariable.LISTITEM_VALUE_EOL;
				}
				setValue(OPTIONAL_SAMPLES_OEDER, samples_ids);
			}

		}
		if (varKey.equals(INPUT_N_POPULATION)) {
			if (value.isEmpty()) {
				setValue(INPUT_POPULATIONS_SIZE, "");
				this.nPopulation = null;
			} else {
				int n_samples = Integer.parseInt(value);
				this.nPopulation = n_samples;
				String samples_ids = "";
				for (int i = 0; i < n_samples; i++) {
					samples_ids += "0" + JobVariable.LISTITEM_VALUE_EOL;
				}
				setValue(INPUT_POPULATIONS_SIZE, samples_ids);
			}

		}
		if (varKey.equals(INPUT_POPULATIONS_SIZE)) {
			if (value.isEmpty()) {
				this.nSamples = 0;
			} else {
				String[] nValues = value.trim().split(JobVariable.LISTITEM_VALUE_EOL);
				this.nSamples = 0;
				for (String nStr : nValues) {
					try {
						int n = Integer.parseInt(nStr);
						this.nSamples += n;
					} catch (Exception e) {
						getVariable(varKey).setExternalErrMsg("N Values are not valid");
					}
				}
				if (getVariable(varKey).isValid()) {
					setValue(OPTIONAL_N_SAMPLES, Integer.toString(this.nSamples));
				}

			}

		}
		return super.notifyVarValueChanged(varKey, value);
	}

	private boolean validatePopulationsSize() {
		String value = getValue(INPUT_POPULATIONS_SIZE);
		String[] nValues = value.trim().split(JobVariable.LISTITEM_VALUE_EOL);
		this.nSamples = 0;
		for (String nStr : nValues) {
			try {
				int n = Integer.parseInt(nStr);
				if (n <= 0) {
					getVariable(INPUT_POPULATIONS_SIZE)
							.setExternalErrMsg("N Values are not valid, Value should be greater than zero.");
					return false;
				}
				this.nSamples += n;
			} catch (Exception e) {
				getVariable(INPUT_POPULATIONS_SIZE).setExternalErrMsg("N Values are not valid");
				return false;
			}
		}
		value = getValue(INPUT_N_POPULATION);
		try {
			int n_populations = Integer.parseInt(value);
			if (n_populations != nValues.length) {
				getVariable(INPUT_POPULATIONS_SIZE).setExternalErrMsg("Populations sizes (" + nValues.length
						+ ") must equal N Population input (" + n_populations + ")");
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			getVariable(INPUT_N_POPULATION).setExternalErrMsg("N population is not valid");

			return false;
		}

		return true;
	}
	
	@Override
	protected void createOptions() {
		// TODO Auto-generated method stub
		VariableDescriptor varOptions = VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
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
//        File format: (average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,
//        fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
				.setVariableName("Alternative Spectrum File")
				.setHelpMsg(
						"Alternative Spectrum File (Only for Optimal Test): alternative_spectrum for each population (except outg). "
								+ "File format: (average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,\n"
								+ "fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]")
				.setCommandParamater("-A").setVariableGroup(GENERAL_GROUP));
//    -S [Null Spectrum File (only if -a is defined): null_spectrum for each population (except outg).
//        (average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,
//        fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]. DEFAULT SNM.
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
				.setVariableName("Null Spectrum File")
				.setHelpMsg(
						"Null Spectrum File (only if -a is defined): null_spectrum for each population (except outg).\n"
								+ "(average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,\n"
								+ "fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]. DEFAULT SNM.")
				.setCommandParamater("-S").setVariableGroup(GENERAL_GROUP));
//    -P [Only for Calculation of R2_p: first value is the number of values to include, 
//                     next are the number of lines to consider. ex: -P 6 1 2 4 8 16 64]
		jobDesc.addVariable(
				VariableDescriptor.String(OPTION_AUTO, VariableRole.Option).setVariableName("Calculation of R2_p")
						.setHelpMsg("Only for Calculation of R2_p: first value is the number of values to include, \n"
								+ "next are the number of lines to consider. ex: -P 6 1 2 4 8 16 64]")
						.setCommandParamater("-P").setVariableGroup(GENERAL_GROUP));
	}

	private boolean validateSamplesCustomOrder() {
		if(hasVariable(OPTIONAL_CUSTOM_ORDER) && JobVariable.TRUE_VALUE.equals(getValue(OPTIONAL_CUSTOM_ORDER))) {
			
			String value = getValue(OPTIONAL_SAMPLES_OEDER);
			String[] nValues = value.trim().split(JobVariable.LISTITEM_VALUE_EOL);
			if (nValues.length != this.nSamples) {
				getVariable(OPTIONAL_SAMPLES_OEDER)
						.setExternalErrMsg("N samples count does not match total samples from Populations");
				return false;

			}
		}
		
		return true;
	}

	@Override
	protected void createRules() {

		if(hasVariable(OPTIONAL_CUSTOM_ORDER) ) {
			new VariablesRule(getVariable(OPTIONAL_CUSTOM_ORDER), getVariable(OPTIONAL_SAMPLES_OEDER), Condition.True,
					Action.Enable, true);
		}
		

	}

	@Override
	public boolean isValid() {

		// validate INPUT_POPULATIONS_SIZE
		// validate OPTIONAL_N_SAMPLES
		return super.isValid() && validatePopulationsSize() && validateSamplesCustomOrder();
	}

}
