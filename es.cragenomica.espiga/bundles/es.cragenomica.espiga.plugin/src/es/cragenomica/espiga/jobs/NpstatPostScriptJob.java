package es.cragenomica.espiga.jobs;


import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.executer.BashBlock;
import com.biotechvana.workflow.executer.BashConditions;
import com.biotechvana.workflow.executer.BashHelper;
import com.biotechvana.workflow.executer.GeneralCMLTemplate;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class NpstatPostScriptJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "npstat_plot";
	static public final String JOB_NAME = "Visualize npstat results";
	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	
	public NpstatPostScriptJob() {
		super(JOB_ID, JOB_NAME, true,false);
		


		this.jobDesc.setDesc("npstat-vis: Visualize npstat results");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.Files,VariableRole.Input);
		varInputFile.isRequired = true;
		
		varInputFile.setVariableName("Input npstat stats file");
		varInputFile.setHelpMsg("npstat stats file");
		jobDesc.addVariable(varInputFile);
	}

	@Override
	protected void createOutputs() {
		VariableDescriptor varOutputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES, VariableType.File,
				VariableRole.Output);
		varOutputFile.isRequired = true;
		varOutputFile.setVariableName("output file");
		jobDesc.addVariable(varOutputFile);
		
	}

	@Override
	protected void createOptions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createRules() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void generateCommand(StringBuilder builder) {
		BashBlock command_script = new BashBlock();


		JobVariable inputVariable = getVariable(INPUT_FILES);
		JobVariable outputVariable = getVariable(OUTPUT_FILES);

		
		
		
		
		GeneralCMLTemplate plot_data = new GeneralCMLTemplate();
		plot_data.getBefore().addStatment(st(
				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
				
				)
				
				
				);
		plot_data.getBefore().addStatment(
				st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.pdf"))
				);
		
		plot_data.setBaseCommand("npstat_plot_windows.R");
		plot_data.setTrackInput(inputVariable.$());
		plot_data.addArgs(inputVariable.$());
		plot_data.addArgs(outputVariable.$());
		command_script.addStatment(plot_data);
		
		command_script.generate(builder);
	}
}
