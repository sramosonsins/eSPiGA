 
package es.cragenomica.espiga.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import es.cragenomica.espiga.UIConstants;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class ShowTrackingView {
	@Execute
	public void execute(EPartService partService, EModelService modelService,MApplication application) {
		
		MPerspective element = 
		        (MPerspective) modelService.find(UIConstants.ESPIGA_PERSPECTIVE_DEFAULT, application);
		    // now switch perspective
		    partService.switchPerspective(element);
		
		   MPart part = partService.createPart(UIConstants.ESPIGA_TRACKING_VIEW);
		   partService.showPart(part, PartState.ACTIVATE);
	}
	
	
	@CanExecute
	public boolean canExecute() {
		
		return true;
	}
		
}