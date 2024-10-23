package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;
    private final CurrencyFileParser parser;
    private final CurrencyDownloader downloader;

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    public List<CurrencyDto> getAllCurrencies() {
        return repository.findAll()
                .stream()
                .map(mapper::convertToDto)
                .toList();
    }

    private void saveOrUpdateParsedResults() {
        log.info("Parse file started");

        HashMap<String, CurrencyDto> currencyDtoMap = new HashMap<>();

        try {
            List<CurrencyDto> currencyDtoList = parser.parseFile();
            currencyDtoList.forEach(currencyDto -> currencyDtoMap.put(currencyDto.getIsoLiteralCode(), currencyDto));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Currency> currencies = repository.findAll();
        currencies.forEach(currency -> {
            String isoLiteralCode = currency.getIsoLiteralCode();
            if (isoLiteralCode != null) {
                CurrencyDto currencyDto = currencyDtoMap.get(isoLiteralCode);
                repository.updateNameAndNominalAndValueAndIsoNumCodeByIsoLiteralCode(
                        currencyDto.getName(), currencyDto.getNominal(), currencyDto.getValue(),
                        currencyDto.getIsoNumCode(), isoLiteralCode);
                currencyDtoMap.remove(isoLiteralCode);
            }
        });
        List<Currency> forSave = currencyDtoMap.values().stream().map(mapper::convertToEntity).toList();
        repository.saveAll(forSave);
    }

    @Scheduled(fixedDelay = 60 * 10 * 1000)
    private void updateCurrenciesFileAndSave() {
        downloader.downloadCurrencyFile();
        saveOrUpdateParsedResults();
    }

}
