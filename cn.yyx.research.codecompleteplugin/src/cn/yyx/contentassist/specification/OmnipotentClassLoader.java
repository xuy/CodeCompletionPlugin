package cn.yyx.contentassist.specification;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

public class OmnipotentClassLoader {

	private static List<IJavaProject> javaProjects = null;
	private static List<URLClassLoader> loaders = null;

	public static Class<?> LoadClass(String classfullname) throws CoreException, MalformedURLException {
		Class<?> cls = null;
		try {
			cls = Class.forName(classfullname);
		} catch (ClassNotFoundException e) {
			try {
				cls = ComplexLoadClass(classfullname);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				throw e1;
			} catch (CoreException e1) {
				e1.printStackTrace();
				throw e1;
			}
		}
		return cls;
	}

	private static Class<?> ComplexLoadClass(String classfullname) throws MalformedURLException, CoreException {
		FirstInitialzie();
		return MultipleLoadClass(classfullname);
	}

	private static Class<?> MultipleLoadClass(String classfullname) {
		for (URLClassLoader loader : loaders) {
			try {
				Class<?> clazz = loader.loadClass(classfullname);
				return clazz;
			} catch (ClassNotFoundException e) {
			}
		}
		return null;
	}

	private static void FirstInitialzie() throws MalformedURLException, CoreException {
		if (javaProjects == null) { // || projects.length != javaProjects.size()
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			List<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
			for (IProject project : projects) {
				try {
					project.open(null /* IProgressMonitor */);
				} catch (CoreException e) {
					System.err.println("Project open false.");
					e.printStackTrace();
					System.exit(1);
				}
				IJavaProject javaProject = JavaCore.create(project);
				javaProjects.add(javaProject);
			}
			GenerateAllLoaders(true);
		}
	}

	private static void GenerateAllLoaders(boolean regenerate) throws MalformedURLException, CoreException {
		if (regenerate || loaders == null) {
			loaders = new ArrayList<URLClassLoader>();
			for (IJavaProject javaProject : javaProjects) {
				loaders.add(GetProjectClassLoader(javaProject));
			}
		}
	}

	private static URLClassLoader GetProjectClassLoader(IJavaProject project)
			throws CoreException, MalformedURLException {
		String[] classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(project);
		List<URL> urlList = new ArrayList<URL>();
		for (int i = 0; i < classPathEntries.length; i++) {
			String entry = classPathEntries[i];
			IPath path = new Path(entry);
			URL url = path.toFile().toURI().toURL();
			urlList.add(url);
		}
		ClassLoader parentClassLoader = project.getClass().getClassLoader();
		URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
		URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);
		return classLoader;
	}

}