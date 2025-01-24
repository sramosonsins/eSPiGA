package es.cragenomica.espiga.workflow.manager;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.InjectorFactory;
import org.osgi.service.component.annotations.Component;

import com.biotechvana.workflow.manager.IWorkflowManager;
import com.biotechvana.workflow.manager.JobsCatalog;
import com.biotechvana.workflow.manager.PipelinesCreatorHelper;
import com.biotechvana.workflow.manager.WorkflowCatalog;
import com.biotechvana.workflow.manager.WorkflowManager;
import com.biotechvana.workflow.ui.WorkflowSelectionViewfilters;

@Component(service = IContextFunction.class, property = "service.context.key=com.biotechvana.workflow.manager.IWorkflowManager")
public class WorkflowManagerCreationFunction  extends ContextFunction {
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		// The following order is subject to change with further framework updates
		
		// First create Job Catalog
		JobsCatalog jobCatalog = new eSPiGAJobsCatalog(eSPiGAJobsCatalog.class.getClassLoader());
		context.set(JobsCatalog.class, jobCatalog);

		// Then create Workflow Catalog
		WorkflowCatalog workflowCatalog = new eSPiGAWorkflowCatalog();
		context.set(WorkflowCatalog.class, workflowCatalog);
		
		// Create Pipeline Creater helper
		PipelinesCreatorHelper pipelinesCreatorHelper = ContextInjectionFactory.make(PipelinesCreatorHelper.class, context);
		context.set(PipelinesCreatorHelper.class, pipelinesCreatorHelper);

		
		IWorkflowManager workflowManager = ContextInjectionFactory.make(WorkflowManager.class, context); ;// new WorkflowManager(workflowCatalog,jobCatalog);
		context.set(IWorkflowManager.class, workflowManager);
		// No init any more , it is handled by workflow manager internally 
//		eSPiGAWorkflowCatalog.initCatalog(workflowManager);
		
		InjectorFactory.getDefault().addBinding(WorkflowSelectionViewfilters.class).implementedBy(eSPiGAWorkflowFilters.class);

		
		return workflowManager;
	}
}
