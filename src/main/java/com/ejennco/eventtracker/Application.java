package com.ejennco.eventtracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ejennco.eventtracker.services.LogService;

@SpringBootApplication
public class Application implements CommandLineRunner{
	private static Logger log = LoggerFactory.getLogger(Application.class);
	private final LogService logService;
	private static String filePath;
	
	public Application(LogService logService) {
	    this.logService = logService;
	  }
    
    public static void main(String[] args) {
    	log.info("Starting application");
    	SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

	@Override
	public void run(String... args) throws Exception {
		if (args == null || args.length != 1) {
			throw new IllegalArgumentException("Given arguments is not valid. There has to be 1 argument.");
		}
		filePath =  args[0];
		log.debug("Parsing json file {}", filePath);
		logService.parseJsonFile(filePath);
		
	}

}
