package br.com.file.analytic;

import br.com.file.analytic.config.Config;
import br.com.file.analytic.utils.DirUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        Log log = new Log();

        Properties config = Config.loadConfig();

        ReadFile readFile = new ReadFile();

        String baseFolder = System.getenv("HOMEPATH") != null ? System.getenv("HOMEPATH") + "/" + "data" : config.getProperty("folder.base");

        File folderIn = new File(baseFolder + "/" + config.getProperty("folder.in"));
        File folderProcessing = new File(baseFolder + "/" + config.getProperty("folder.processing"));
        File folderOut = new File(baseFolder + "/" + config.getProperty("folder.out"));
        File folderBkp = new File(baseFolder + "/" + config.getProperty("folder.backup"));

        DirUtils.createDirIfNotExists(folderIn.getAbsolutePath());
        DirUtils.createDirIfNotExists(folderProcessing.getAbsolutePath());
        DirUtils.createDirIfNotExists(folderOut.getAbsolutePath());
        DirUtils.createDirIfNotExists(folderBkp.getAbsolutePath());

        System.out.println("Procurando arquivos ... ");

        while(true){

            for (File fileEntry : folderIn.listFiles()) {

                if (fileEntry.isFile()) {

                    String fileToProcess = folderProcessing.getAbsolutePath() + "/" + fileEntry.getName();

                    try {
                        DirUtils.moveFile(fileEntry.getAbsoluteFile().toString(), fileToProcess);
                    } catch (Exception err) {
                        System.out.println("Problemas ao mover arquivos para diretório de processamento");
                        log.logger.warning("Problemas ao mover o arquivo " + fileEntry.getName() + " para diretório de processamento");
                        continue;
                    }

                    log.logger.info("Arquivo " + fileEntry.getName() + " enviado para diretório de processamento");

                    String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

                    DirUtils.createDirIfNotExists(folderOut.getAbsolutePath() + "/" + date);
                    DirUtils.createDirIfNotExists(folderBkp.getAbsolutePath() + "/" + date);

                    Long filenamePrefix = new Date().getTime();

                    readFile.readCsvFile(fileToProcess);

                    log.logger.info("Arquivo " + fileEntry.getName() + " processado");

                    String outputFile = filenamePrefix + "_" + fileEntry.getName();

                    readFile.writeCsvFile(folderOut.getAbsolutePath() + "/" + date + "/" + outputFile);

                    log.logger.info("Arquivo " + outputFile + " gerado no diretório " + folderOut.getAbsolutePath() + "/" + date);

                    try {
                        DirUtils.copyFile(fileToProcess, folderBkp.getAbsolutePath() + "/" + date + "/" + filenamePrefix + "_" + fileEntry.getName());
                        log.logger.info("Arquivo " + fileEntry.getName() + " copiado para diretório de backup");
                    } catch (Exception err) {
                        System.out.println("Problemas ao copiar arquivo para diretório de backup: " + err.getMessage());
                        log.logger.warning("Problemas ao copiar o arquivo " + fileEntry.getName() + " para diretório de backup");
                    }

                    try {
                        DirUtils.deleteFile(fileToProcess);
                        log.logger.info("Arquivo " + fileEntry.getName() + " removido do diretório de processamento");
                    } catch (IOException err) {
                        System.out.println("Problemas ao deletar o arquivo " + fileEntry.getName() + " do diretório de processamento");
                    }
                }
            }
            Thread.sleep(10000);
        }
    }
}
