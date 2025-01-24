package es.cragenomica.espiga.jobs;

import com.biotechvana.workflow.VariablesRule;
import com.biotechvana.workflow.VariablesRule.Action;
import com.biotechvana.workflow.VariablesRule.Condition;
import com.biotechvana.workflow.descriptors.CheckedVariableDescriptor;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.executer.BashBlock;
import com.biotechvana.workflow.executer.BashConditions;
import com.biotechvana.workflow.executer.BashHelper;
import com.biotechvana.workflow.executer.GeneralCMLTemplate;
import com.biotechvana.workflow.executer.IFObject;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class SingleRegionAnalysesJob extends MstatspopJob {

	static public final String JOB_ID = "mstatspop_sra";
	static public final String JOB_NAME = "mstatspop - Single Region Analyses" ;


	public SingleRegionAnalysesJob() {
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
		
		this.jobDesc.setDesc("mstatspop: perfrom Single Region Analyses using fasta inputs.");
	}

	@Override
	protected void createInputs() {
		createInputFasta();
		super.createInputs();


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
		varInputFile.setVariableName("output file");
		varInputFile.setCommandParamater("-T");
		jobDesc.addVariable(varInputFile);

	}

//	public static String OPTIONAL_CUSTOM_ORDER = "OPTIONAL_CUSTOM_ORDER";

	@Override
	protected void createOptions() {
		
		createFastaTfasteOptional();
		// TODO Fix duplicate input and option var for such cases
		VariableDescriptor varOptions;
		

//	    Optional Parameters for fasta and tfa input files:
//	        -O [#_nsam] [number order of first sample, number 0 is the first sample] [second sample] ...etc. up to nsamples.
//	           DEFAULT current order.
//		jobDesc.addVariable(VariableDescriptor.Checked(OPTIONAL_CUSTOM_ORDER, VariableRole.Option)
//				.setVariableName("Provide Custom samples order"));
//		jobDesc.addVariable(
//				VariableDescriptor.Strings(OPTION_AUTO, VariableRole.Option).setVariableName("Samples order")
//						.setCommandParamater("-O").setHelpMsg("Samples custom Order. Number 0 is the first sample"));
//		



		
		
//		   PARAMETERS FOR FASTA INPUT (-f fasta): 'WHOLE REGION ANALYSIS'
//		    Optional:
//		      -p [Number of lineages per sequence (1/2)]. DEFAULT 1.
		jobDesc.addVariable(VariableDescriptor.Range(OPTION_AUTO, VariableRole.Option)
				.setMinValue(1)
				.setMaxValue(2)
				.setVariableName("Number of lineages per sequence ")
				.setHelpMsg("Number of lineages per sequence (1/2). DEFAULT 1")
				.setCommandParamater("-p").setVariableGroup("REGION ANALYSIS PARAMETERS"));
//		      -g [GFF_file]
//		         [add also: coding,noncoding,synonymous,nonsynonymous,silent, others (or whatever annotated)]
//		         [if 'synonymous', 'nonsynonymous', 'silent' add: Genetic_Code: Nuclear_Universal,mtDNA_Drosophila,mtDNA_Mammals,Other]
//		         [if 'Other', introduce the code for the 64 triplets in the order UUU UUC UUA UUG ... etc.].
//		         DEFAULT no annotation.
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
				.setVariableName("GFF annotation file")
				.setHelpMsg("GFF annotation file. DEFAULT no annotation.")
				.setCommandParamater("-g").setVariableGroup("REGION ANALYSIS PARAMETERS"));
//		      -c [in case use coding regions, criteria to consider transcripts (max/min/first/long)]. DEFAULT long.
		jobDesc.addVariable(VariableDescriptor.SelectionList(OPTION_AUTO, VariableRole.Option)
				.addOptionValue("long")
				.addOptionValue("first")
				.addOptionValue("max")
				.addOptionValue("min")
				.setVariableName("Criteria to consider transcripts")
				.setCommandParamater("-c")
				.setHelpMsg("in case use coding regions, criteria to consider transcripts").setVariableGroup("REGION ANALYSIS PARAMETERS")
			);
//		      -K [make a MASK file with the valid positions for this fasta. Useful for running ms simulations (1/0)]. DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1)
				.setVariableName("make a MASK file with the valid positions")
				.setHelpMsg("make a MASK file with the valid positions for this fasta. Useful for running ms simulations (1/0). DEFAULT 0")
				.setCommandParamater("-K").setVariableGroup("REGION ANALYSIS PARAMETERS"));
		
		super.createOptions();
		
////		   OPTIONAL GENERAL PARAMETERS:
////	      -G [outgroup (0/1)] (last population). DEFAULT 0.
//		varOptions = VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
//				.setCommandPostfix(CheckedVariableDescriptor.p_0_1).setVariableName("last population")
//				.setHelpMsg("outgroup (0/1) (last population). DEFAULT 0").setDefaultValue("").setCommandParamater("-G")
//				.setVariableGroup(GENERAL_GROUP);
//		jobDesc.addVariable(varOptions);
//
////	      -u [include unknown positions (0/1)].  DEFAULT 0.
//		jobDesc.addVariable(VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
//				.setCommandPostfix(CheckedVariableDescriptor.p_0_1).setVariableName("Include unknown positions")
//				.setHelpMsg("Include unknown positions (0/1). DEFAULT 0").setDefaultValue("").setCommandParamater("-u")
//				.setVariableGroup(GENERAL_GROUP));
////	      -A [Alternative Spectrum File (Only for Optimal Test): alternative_spectrum for each population (except outg)
////	          File format: (average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,
////	          fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]
//		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
//				.setVariableName("Alternative Spectrum File")
//				.setHelpMsg("Alternative Spectrum File (Only for Optimal Test): alternative_spectrum for each population (except outg). "
//						+ "File format: (average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,\n"
//						+ "fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]")
//				.setCommandParamater("-A").setVariableGroup(GENERAL_GROUP));
////	      -S [Null Spectrum File (only if -a is defined): null_spectrum for each population (except outg).
////	          (average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,
////	          fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]. DEFAULT SNM.
//		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
//				.setVariableName("Null Spectrum File")
//				.setHelpMsg("Null Spectrum File (only if -a is defined): null_spectrum for each population (except outg).\n"
//						+ "(average absolute values) header plus fr(0,1) fr(0,2) ... fr(0,n-1) theta(0)/nt,\n"
//						+ "fr(1,1) fr(1,2) ... fr(1,n-1) theta(1)/nt...]. DEFAULT SNM.")
//				.setCommandParamater("-S").setVariableGroup(GENERAL_GROUP));
////	      -P [Only for Calculation of R2_p: first value is the number of values to include, 
////	                       next are the number of lines to consider. ex: -P 6 1 2 4 8 16 64]
//		jobDesc.addVariable(VariableDescriptor.String(OPTION_AUTO, VariableRole.Option)
//				.setVariableName("Calculation of R2_p")
//				.setHelpMsg("Only for Calculation of R2_p: first value is the number of values to include, \n"
//						+ "next are the number of lines to consider. ex: -P 6 1 2 4 8 16 64]")
//				.setCommandParamater("-P").setVariableGroup(GENERAL_GROUP));
	}

	@Override
	public boolean notifyVarValueChanged(String varKey, String value) {
		try {
//			System.out.println(" Tophat notifyVarValueChanged" + varKey + " " + ((FileVariable) getVariable(INPUT_FASTQ_FILE)).getIsPairedFiles());
//			if (varKey.equals(INPUT_FILES_FORMAT)) {
//				getVariable(INPUT_FILES_FORMAT_DUMMY).setValue(value);
//			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		return super.notifyVarValueChanged(varKey, value);
	}

	@Override
	protected void createRules() {


		super.createRules();
		
		
		
		
		// Other variables
		new VariablesRule(getVariableByCMDSwitch("-u"), getVariableByCMDSwitch("-t"), Condition.True, Action.Reset,
				true);
		
	}
	@Override
	protected void generateCommand(StringBuilder builder) {
		BashBlock command_script = new BashBlock();

		JobVariable custom_order = getVariable(OPTIONAL_CUSTOM_ORDER);
		String custom_order_variable = "CUSTOM_ORDER";
		String population_sizes_variable = "N_POP";
		command_script.addStatment(
				new IFObject(BashConditions.True(custom_order))
				.Then(
						st(BashHelper.assign(custom_order_variable,"-O " + getVariable(OPTIONAL_N_SAMPLES).$() + " " + getVariable(OPTIONAL_SAMPLES_OEDER).get_cmdValue() ))
					)
				);
		command_script.addStatment(
				st(BashHelper.assign(population_sizes_variable,"-N " + getVariable(INPUT_N_POPULATION).$() + " " + getVariable(INPUT_POPULATIONS_SIZE).get_cmdValue() ))
				);
		JobVariable inputVariable = getVariable(INPUT_FILES);
		JobVariable outputVariable = getVariable(OUTPUT_FILES);
		
		
		GeneralCMLTemplate mstatspop = new GeneralCMLTemplate();
		mstatspop.getBefore().addStatment(st(
				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
				));
		
		mstatspop.getBefore().addStatment(
				st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.txt"))
			);
		mstatspop.setBaseCommand("mstatspop");
		
		mstatspop.setTrackInput(inputVariable);
		
		mstatspop.addArgs(getOptionParamatersStrAs$());
		mstatspop.addArgs($(custom_order_variable));
		mstatspop.addArgs($(population_sizes_variable));
//		mstatspop.addArgs("-N");
//		mstatspop.addArgs(getVariable(INPUT_N_POPULATION).$() );
//		mstatspop.addArgs(getVariable(INPUT_POPULATIONS_SIZE).get_cmdValue());

		mstatspop.addArgs("-T");
		mstatspop.addArgs(outputVariable.$());
		mstatspop.getAfter().addStatment(
				new IFObject(BashConditions.fileExist(outputVariable.$())).Then(st("cat " + outputVariable.$())));
		
		
		command_script.addStatment(mstatspop);
		
		
		
		command_script.generate(builder);
	}

}
