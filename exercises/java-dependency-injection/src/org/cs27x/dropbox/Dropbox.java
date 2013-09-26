package org.cs27x.dropbox;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.cs27x.filewatcher.FileEventSource;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * The root class of the application. This class is primarily responsible for
 * constructing the other classes using a dependency injection framework and
 * then starting the DropboxTransport and FileEventSource.
 * 
 * @author jules
 * 
 */
public class Dropbox {

	private final DropboxTransport transport_;
	@SuppressWarnings("unused")
	private final DropboxClient client_;
	private final FileEventSource fileEventSource_;

	public Dropbox(DropboxClient client, FileEventSource fileEventSource,
			DropboxTransport transport) {
		super();
		client_ = client;
		fileEventSource_ = fileEventSource;
		transport_ = transport;
	}

	public void connect(String server) throws Exception {
		transport_.connect(server);
		fileEventSource_.start();
	}

	public boolean connected() {
		return transport_.isConnected();
	}

	public void disconnect() {
		fileEventSource_.stop();
		transport_.disconnect();
	}

	public static void main(String[] args) throws Exception {

		DropboxOptions options = new DropboxOptions();
		JCommander jcommander = new JCommander(options,args);
		jcommander.setProgramName("org.cs27x.dropbox.Dropbox");
		
		if(options.getRootDir() == null){
			jcommander.usage();
		}
		else if(!Files.exists(Paths.get(options.getRootDir()))){
			System.err.println("The specified directory to share ["+options.getRootDir()+"] does not exist.");
			jcommander.usage();
		}
		else {
			/*
			 * Overview: The goal of this exercise is to show you how to use
			 * Google's Guice dependency injection. Initially, you will hand-code
			 * the construction of the Dropbox object and its dependencies. Then,
			 * you will refactor the implementation to use Google's Injector and
			 * Modules to automatically construct a Dropbox object for you.
			 * 
			 * The Injector takes a class or interface as a parameter and will automatically
			 * construct an instance of that class or interface for you. If the class
			 * depends on one or more interfaces (e.g., they are taken as constructor
			 * parameters), the Injector needs to know what concrete implementation of
			 * that interface should be provided (e.g., use FileWatcherSource for FileEventSource).
			 * In order to tell the Injector about these mappings, you create a class that
			 * inherits from Guice's AbstractModule and specify these interface to class
			 * mappings in the configure() method of your derived module.
			 * 
			 * Once you have created an Injector, Guice can automatically create objects
			 * for you -- including any of their dependencies.
			 * 
			 * 
			 */
			
			String rootdir = options.getRootDir();
			/*
			 * Step 1: Construct an instance of Dropbox and assign it to db.
			 * 
			 * 
			 */
			Dropbox db = null;
			
			
			/*
			 * Step 2: Create a new DropboxModule class that inherits from
			 * com.google.inject.AbstractModule. Your class should override
			 * the configure() method.
			 * 
			 * Within the DropboxModule.configure() method, use the
			 * inherited bind(...).to(...) methods to specify the mappings of
			 * interfaces to concrete implementations that you would like to
			 * use for this module.
			 * 
			 * E.g.:
			 * 
			 * bind(FileEventSource.class).to(FileWatcherSource.class);
			 * 
			 * 
			 * Step 3: For any constructor of a concrete implementation class that you 
			 * want to use that takes parameters, you will need to annotate that 
			 * constructor with @Inject.
			 */
			
			/*
			 * Step 5: Create a new Injector instance using Guice.createInjector(...) and your module
			 * implementation. Then, use the Injector to create a Dropbox instance rather than
			 * manually creating one as you did in step 1.
			 */
			
			
			db.connect(options.getHost());
		}
	}
}
