 
package es.cragenomica.espiga.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;



import org.eclipse.e4.core.di.annotations.CanExecute;

import com.biotechvana.workflow.WorkflowJob;
import com.biotechvana.workflow.manager.IWorkflowManager;
import com.biotechvana.workflow.manager.JobsCatalog;

import es.cragenomica.espiga.UIConstants;
import jakarta.inject.Named;


public class ShowWorkflowJobHandlers {
	@Execute
	public void execute(MApplication application,
			EPartService partService, 
		    EModelService modelService,
			IWorkflowManager workflowManager,
			
			@Optional @Named(UIConstants.SHOW_WORKFLOW_TASK_CP_TASK_ID) String task_id ) {
		
		MPerspective element = 
		        (MPerspective) modelService.find(UIConstants.ESPIGA_PERSPECTIVE_DEFAULT, application);
		    // now switch perspective
		    partService.switchPerspective(element);
		
		    MPart part = partService.findPart(UIConstants.WORKFLOW_TASK_VIEW_ID);
		    if(part!= null) {
			   partService.showPart(part, PartState.ACTIVATE);
		    }
		    
		
		if(task_id != null) {
			final WorkflowJob job =   workflowManager.getJobsCatalog().createJobInstanceByFullName(task_id );
			application.getContext().set(WorkflowJob.class, job);
		}
			
	}
	
	
	@CanExecute
	public boolean canExecute(@Optional @Named(UIConstants.SHOW_WORKFLOW_TASK_CP_TASK_ID) String task_id ) {
		System.out.println(task_id + " can execute");
		//System.out.println(task_id);

		return true;
	}
		
}