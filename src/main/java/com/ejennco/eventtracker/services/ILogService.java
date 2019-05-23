package com.ejennco.eventtracker.services;

import java.io.IOException;

public interface ILogService {
	void parseJsonFile(String filePath) throws IOException;

}
