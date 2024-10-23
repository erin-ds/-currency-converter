package ru.skillbox.currency.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.currency.exchange.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByIsoNumCode(Long isoNumCode);

    @Transactional
    @Modifying
    @Query("""
            update Currency c set c.name = ?1, c.nominal = ?2, c.value = ?3, c.isoNumCode = ?4
            where c.isoLiteralCode = ?5""")
    int updateNameAndNominalAndValueAndIsoNumCodeByIsoLiteralCode(String name, Long nominal, Double value, Long isoNumCode, String isoLiteralCode);

}
