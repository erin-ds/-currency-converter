package ru.skillbox.currency.exchange.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Component
@Slf4j
public class CurrencyDownloader {

    @Value("${currency-file.path}")
    private String path;

    @Value("${currency-file.url}")
    private String source;

    @Value("${currency-file.filename}")
    private String filename;


    void downloadCurrencyFile() {
        try {

            URL url = new URL(source);
            File file = new File(path, filename);

            log.info("Started updating currency list from out service.");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[50];
            int current;

            while ((current = bis.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer.toByteArray());
            fos.close();

            log.info("Updating currency list successfully finished");

        } catch (IOException e) {
            log.error("Error during updating currency list");
        }
    }
}
