package ru.skillbox.currency.exchange.service;


import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DoubleAdapter extends XmlAdapter<String, Double> {

    @Override
    public Double unmarshal(String s) {
        return Double.parseDouble(s.replace(',', '.'));
    }

    @Override
    public String marshal(Double aDouble) {
        return aDouble.toString();
    }

}
