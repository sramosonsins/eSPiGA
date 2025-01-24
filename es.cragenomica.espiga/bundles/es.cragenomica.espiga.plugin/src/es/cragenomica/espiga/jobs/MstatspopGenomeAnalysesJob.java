package es.cragenomica.espiga.jobs;

import com.biotechvana.workflow.VariablesRule;
import com.biotechvana.workflow.VariablesRule.Action;
import com.biotechvana.workflow.VariablesRule.Condition;
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

public class MstatspopGenomeAnalysesJob extends MstatspopJob {

	static public final String JOB_ID = "mstatspop_ga";
	static public final String JOB_NAME = "mstatspop - Genome Analyses";
	

	public MstatspopGenomeAnalysesJob() {
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
		this.jobDesc.setDesc("mstatspop: perfrom Genome Analyses using tFasta inputs.");

	}

	@Override
	protected void createInputs() {
		createInputTfasta();
		super.createInputs();
	}

	@Override
	protected void createOutputs() {
		VariableDescriptor varOutputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES, VariableType.FileList,
				VariableRole.Output);
		varOutputFile.isRequired = true;
		varOutputFile.setVariableName("output file");
		varOutputFile.setCommandParamater("-T");
		jobDesc.addVariable(varOutputFile);

	}


	@Override
	protected void createOptions() {
		// TODO Fix duplicate input and option var for such cases
		createFastaTfasteOptional();
		
		VariableDescriptor varOptions;




//		   PARAMETERS FOR TFASTA INPUT (-f tfa): 'SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA'
//			      -w [window size].
		varOptions = VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input).setVariableName("Window size")
				.setHelpMsg("SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA.")
				.setCommandParamater("-w")
				.setIsRequired(true);
		jobDesc.addVariable(varOptions);
//			    Optional:
//			      -z [slide size (must be a positive value)]. DEFAULT window size.
		varOptions = VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option).setMinValue(0)
				.setVariableName("slide size").setHelpMsg("slide size (must be a positive value). DEFAULT window size")
				.setCommandParamater("-z").setVariableGroup("SLIDING WINDOW ANALYSIS PARAMETERS");
		jobDesc.addVariable(varOptions);
//			      -Z [first window size displacement [for comparing overlapped windows])]. DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option).setMinValue(0)
				.setVariableName("first window size displacement")
				.setHelpMsg("first window size displacement [for comparing overlapped windows]. DEFAULT 0")
				.setCommandParamater("-Z").setVariableGroup("SLIDING WINDOW ANALYSIS PARAMETERS"));
//			      -Y [define window lengths in 'physical' positions (1) or in 'effective' positions (0)]. DEFAULT 1.
		jobDesc.addVariable(VariableDescriptor.SelectionList(OPTION_AUTO, VariableRole.Option)
				.addOptionValue("physical", "1", "define window lengths in physical positions")
				.addOptionValue("effective", "0", "define window lengths in effective positions")
				.setDefaultIndexValue(0)
				.setCommandParamater("-Y").setVariableName("Window Lengths Definition").setVariableGroup("SLIDING WINDOW ANALYSIS PARAMETERS"));

//			      -W [file with the coordinates of each window [scaffold init end] (instead options -w and -z).
//			         DEFAULT one whole window.
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option).setCommandParamater("-W")
				.setVariableName("File with the coordinates of each window").setHelpMsg(
						"file with the coordinates of each window [scaffold init end] (instead options -w and -z)").setVariableGroup("SLIDING WINDOW ANALYSIS PARAMETERS"));
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
						+ "DEFAULT all 1.000").setVariableGroup("SLIDING WINDOW ANALYSIS PARAMETERS"));


		
		

		
		super.createOptions();
	}

	

	@Override
	protected void createRules() {
		// Input vars
		
		super.createRules();

		// Other variables
		new VariablesRule(getVariableByCMDSwitch("-u"), getVariableByCMDSwitch("-t"), Condition.True, Action.Reset,
				false);
		
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


		mstatspop.addArgs("-T");
		mstatspop.addArgs(outputVariable.$());
		
		
		command_script.addStatment(mstatspop);
		command_script.generate(builder);
	}
}
