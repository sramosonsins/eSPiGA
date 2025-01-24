package es.cragenomica.espiga.converters;

import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.VariablesRule;
import com.biotechvana.workflow.VariablesRule.Action;
import com.biotechvana.workflow.VariablesRule.Condition;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;

/**
 * Base class for fastaConvtr tool
 * @author ahafez
 *
 */
public abstract class FastaConvtr extends ISWFJob {
	public static String INPUT_FILES = eSPiGAVariableBag.INPUT_FILES;
	public static String COMPRESS_OUTPUT = "COMPRESS_OUTPUT";
	public FastaConvtr(String job_id, String jobName, boolean createJobOutputFolderVar, boolean isParallelJob) {
		super(job_id, jobName, createJobOutputFolderVar, isParallelJob);
	}
	
	
	
	
	@Override
	protected void createOptions() {
	    FastaconvrtrHelper.addSharedOptions(this);
		FastaconvrtrHelper.addSharedOptions2(this.jobDesc);	
	}
	
	@Override
	protected void createRules() {
		// Condition on TODO ::
		new VariablesRule(getVariable(eSPiGAVariableBag.OPTIONAL_CUSTOM_ORDER), getVariable(eSPiGAVariableBag.OPTIONAL_N_SAMPLES), Condition.True,
				Action.Enable, true);
		new VariablesRule(getVariable(eSPiGAVariableBag.OPTIONAL_CUSTOM_ORDER), getVariable(eSPiGAVariableBag.OPTIONAL_SAMPLES_OEDER), Condition.True,
				Action.Enable, true);
	}
	
	@Override
	protected void createOutputs() {
		VariableDescriptor varOutputFile = VariableDescriptor.createDescriptorFor(eSPiGAVariableBag.OUTPUT_FILES,VariableType.File,VariableRole.Output);
		varOutputFile.isRequired = true;
		varOutputFile.setVariableName("Output File");
		varOutputFile.setCommandParamater("-o");
		this.jobDesc.addVariable(varOutputFile);
	}
	
	@Override
	public boolean notifyVarValueChanged(String varKey, String value) {
		if(varKey.equals(eSPiGAVariableBag.OPTIONAL_N_SAMPLES)) {
			if(value.isEmpty()) {
				setValue(eSPiGAVariableBag.OPTIONAL_SAMPLES_OEDER, "");
			} else {
				int n_samples = Integer.parseInt(value);
				String samples_ids = "";
				for(int i=0;i<n_samples;i++ ) {
					samples_ids+=i+ JobVariable.LISTITEM_VALUE_EOL;
				}
				setValue(eSPiGAVariableBag.OPTIONAL_SAMPLES_OEDER, samples_ids);
			}
			
		}
		return super.notifyVarValueChanged(varKey, value);
	}
	
}
