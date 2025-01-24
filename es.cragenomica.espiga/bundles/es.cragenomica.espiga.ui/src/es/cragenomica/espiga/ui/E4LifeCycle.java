package es.cragenomica.espiga.ui;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;

import com.biotechvana.commons.GRPOApplication;
import com.biotechvana.commons.IGproApp;
import com.biotechvana.utils.Constants;

/**
 * This is a stub implementation containing e4 LifeCycle annotated methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the
 * <em>org.eclipse.core.runtime.products' extension point</em>) that references
 * this class.
 * The solo purpose of this pluging is to create the application model and the lifecycle for the product app
 * only as we need a diddfertn login for the lifecycle in RAP application
 * 
 **/
@SuppressWarnings("restriction")
public class E4LifeCycle {

	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext ) {
		Constants.MODULE_ID = "eSPiGA";
		GRPOApplication espigaAPP = new GRPOApplication();
		workbenchContext.set(GRPOApplication.class, espigaAPP);
		workbenchContext.set(IGproApp.class, espigaAPP);
	}

	@PreSave
	void preSave(IEclipseContext workbenchContext) {
		// TODO :: 
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) {
		// TODO :: 
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) {
		// TODO :: 
	}
}
