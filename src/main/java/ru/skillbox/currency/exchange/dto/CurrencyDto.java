package ru.skillbox.currency.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.currency.exchange.service.DoubleAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    @Getter
    private Long id;

    private String name;

    private Long nominal;

    private Double value;

    private Long isoNumCode;

    private String isoLiteralCode;

    @XmlElement(name = "Name")
    public String getName() {
        return name;
    }

    @XmlElement(name = "Nominal")
    public Long getNominal() {
        return nominal;
    }

    @XmlElement(name = "Value")
    @XmlJavaTypeAdapter(DoubleAdapter.class)
    public Double getValue() {
        return value;
    }

    @XmlElement(name = "NumCode")
    public Long getIsoNumCode() {
        return isoNumCode;
    }

    @XmlElement(name = "CharCode")
    public String getIsoLiteralCode() {
        return isoLiteralCode;
    }
}