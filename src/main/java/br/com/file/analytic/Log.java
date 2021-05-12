package br.com.file.analytic;

import br.com.file.analytic.config.Config;
import br.com.file.analytic.utils.DirUtils;

import javax.annotation.processing.ProcessingEnvironment;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.*;

public class Log {

    Properties config = Config.loadConfig();

    private final String baseFolder = System.getenv("HOMEPATH") != null ? System.getenv("HOMEPATH") + "/" + "data" : config.getProperty("folder.base");

    public Logger logger;
    FileHandler fileHandler;

    public Log() throws IOException {

        String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

        String logFile = baseFolder + "/" + config.getProperty("folder.base") + "/" + date + "/" + "analytic.log";

        File filename = new File(logFile);

        DirUtils.createDirIfNotExists(baseFolder + "/" + config.getProperty("folder.base") + "/" + date);

        if(!filename.exists()) {
            filename.createNewFile();
        }

        fileHandler = new FileHandler(filename.getAbsoluteFile().toString(), true);

        logger = Logger.getLogger("analytic");
        logger.addHandler(fileHandler);

        fileHandler.setFormatter(formatter());
    }

    public Formatter formatter(){
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                String logTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                return  logTime + "  "
                        + record.getLevel() + "  "
                        + record.getMessage() + "\n";
            }
        };
    }

}
