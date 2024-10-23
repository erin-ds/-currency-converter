package ru.skillbox.currency.exchange.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.ValCurs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Component
public class CurrencyFileParser {

    @Value("${currency-file.path}")
    private String path;

    @Value("${currency-file.filename}")
    private String filename;

    private List<CurrencyDto>  toObject(InputStream xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(ValCurs.class);
            Unmarshaller unmarshaller  = context.createUnmarshaller();
            ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(xml);
            return valCurs.getCurrencyDtoList();
        } catch (JAXBException e) {
            throw new RuntimeException("Cannot import data from file");
        }
    }

    public List<CurrencyDto> parseFile() throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of(path + filename));
        return toObject(inputStream);
    }
}
