package ru.skillbox.currency.exchange.dto;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.NONE)
@NoArgsConstructor
@Setter
public class ValCurs {

    private List<CurrencyDto> currencyDtoList = new ArrayList<>();

    @XmlElement(required = true,  name = "Valute")
    public List<CurrencyDto> getCurrencyDtoList() {
        return currencyDtoList;
    }
}
