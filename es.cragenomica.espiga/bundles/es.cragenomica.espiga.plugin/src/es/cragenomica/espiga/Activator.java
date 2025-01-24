package es.cragenomica.espiga;

import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Activator implements BundleActivator{

	static public String BUNDLE_ID = "es.cragenomica.espiga.plugin";

	
	public Activator() {
		// TODO Auto-generated constructor stub
	}

	
	static public IEclipseContext getEclipseContext() {
		Bundle x = FrameworkUtil.getBundle(Activator.class);
		IEclipseContext serviceContext = EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(Activator.class).getBundleContext());
		IEclipseContext eclipseContext = serviceContext.get(IWorkbench.class).getApplication().getContext();
		return eclipseContext;
	}


	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		Logger LOG = LoggerFactory.getLogger(Activator.class);
//		org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(Activator.class);
		LOG.info("BBBB");
	}


	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
