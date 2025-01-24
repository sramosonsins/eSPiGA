package es.cragenomica.espiga.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

import com.biotechvana.servercommons.dialogs.preferences.DockerHelper;
import com.biotechvana.servercommons.dialogs.preferences.PreferencesSSHDialog;
import com.biotechvana.users.IUsersService;

public class AppLoginStartup implements IRunnableWithProgress {
	IUsersService usersService;
	Display display;
	public AppLoginStartup(IUsersService usersService, Display display) {
		this.display =display;
		this.usersService = usersService;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		
		monitor.beginTask("Login in progress, please wait ...", IProgressMonitor.UNKNOWN);

		//SubMonitor subMonitor = SubMonitor.convert(monitor, 2);

		//SubMonitor activeMonitor = subMonitor.newChild(1); 
		if(usersService.getUserManager().get(PreferencesSSHDialog.USE_LOCAL_GPRO_PROP,
				PreferencesSSHDialog.FALSE_CNST).equals(PreferencesSSHDialog.TRUE_CNST)) {
			// wait to activate Docker first
			//activeMonitor.beginTask("Starting Docker Container", IProgressMonitor.UNKNOWN);
			if(!DockerHelper.checkStart(display,false) ) {
				// can not continue here
				//activeMonitor.done();
				return;
			}
			//activeMonitor.done();

		} else {
			//activeMonitor.beginTask("Skipping ...", IProgressMonitor.UNKNOWN);
			//activeMonitor.done();
		}
		//activeMonitor = subMonitor.newChild(1);
		//activeMonitor.beginTask("Login in progress, please wait ...", IProgressMonitor.UNKNOWN);
		boolean isOk = false;
		try {
			isOk = usersService.initLogin();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!isOk)
			display.asyncExec(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					MessageDialog.openError(display.getActiveShell(), "Connection error",
							"Can not connect to Remote Server");
				}
			});
		//activeMonitor.done();


	}

}
