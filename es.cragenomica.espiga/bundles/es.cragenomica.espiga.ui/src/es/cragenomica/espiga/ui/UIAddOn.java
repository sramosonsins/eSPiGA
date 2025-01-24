package es.cragenomica.espiga.ui;

import java.lang.reflect.InvocationTargetException;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.progress.IProgressService;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.event.Event;

import com.biotechvana.ui.rcp.status.CStatusLineManager;
import com.biotechvana.users.IUsersService;
import com.biotechvana.utils.WorkspaceUtils;


public class UIAddOn {
	@PostConstruct
	public void init(MApplication application, IEclipseContext context) {
		IEclipseContext appContext = application.getContext();
		
		CStatusLineManager statusLineManager = ContextInjectionFactory.make(CStatusLineManager.class, context);
		appContext.set(IStatusLineManager.class, statusLineManager);
		
		ContextInjectionFactory.make(WorkspaceUtils.class,context);

	}
	@Inject
	@Optional
	public void applicationStarted(
			@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event ,
			IUsersService usersService,
			Display display,
			IProgressService progressService
			) {
		//System.out.println("applicationStarted");
		
		
//		Job loginJob = new Job("Login In progress") {
//			
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				monitor.beginTask("Login to remote", IProgressMonitor.UNKNOWN);
//				try {
//					boolean isOk  = usersService.initLogin();
//					if(isOk)
//						return  Status.OK_STATUS;
//				}
//				catch (InvalidLoginInfoException e) {
//					e.printStackTrace();
//					return Status.error("Invalid Login, Please check");
//				}
//				 catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return  Status.error("Can not Init login.");
//				}
//				
//				return Status.error("Invalid Login, Please check");
//			}
//		};
//		loginJob.setSystem(true);
		
		AppLoginStartup appLoginStartup = new AppLoginStartup(usersService,display);
		
		try {
			progressService.run(true, false,appLoginStartup);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//progressService.showInDialog(display.getActiveShell(),loginJob);
		//loginJob.schedule();
			

	}
}
