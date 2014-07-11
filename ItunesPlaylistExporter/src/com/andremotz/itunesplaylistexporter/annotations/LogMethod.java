package com.andremotz.itunesplaylistexporter.annotations;

import java.lang.annotation.Documented;

import org.apache.log4j.Logger;

import com.andremotz.itunesplaylistexporter.mainscreens.MainClass;

@Documented
public @interface LogMethod {
	
	static Logger log = Logger.getLogger(MainClass.class.getName());
	

}
