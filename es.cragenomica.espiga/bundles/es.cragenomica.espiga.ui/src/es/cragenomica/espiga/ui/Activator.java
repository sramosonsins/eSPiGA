package es.cragenomica.espiga.ui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {

	static final String BUNDLE_ID = "es.cragenomica.espiga.ui";
	
	private static BundleContext context;
	private static String bundleName;
	static BundleContext getContext() {
		return context;
	}

	static String getSymbolicName() {
		return bundleName;
	}
	
	
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.bundleName  = bundleContext.getBundle().getSymbolicName();
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	
	
	
	
	
	
	
	

}
