 
package es.cragenomica.espiga.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import es.cragenomica.espiga.UIConstants;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class WorkspacePersepective {
	@Execute
	public void execute(MApplication app, EPartService partService, 
		      EModelService modelService) {
		 MPerspective element = 
			        (MPerspective) modelService.find(UIConstants.ESPIGA_PERSPECTIVE_DEFAULT, app);
			    // now switch perspective
			    partService.switchPerspective(element);
	}
	
	
	@CanExecute
	public boolean canExecute(EModelService modelService,MWindow window) {
		MPerspective p =  modelService.getActivePerspective(window);
		
		return !UIConstants.ESPIGA_PERSPECTIVE_DEFAULT.equals(p.getElementId());
	}
		
}