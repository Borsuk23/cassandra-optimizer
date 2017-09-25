package eu.asyroka.msc.service.impl;

import eu.asyroka.msc.service.CassandraInputValidator;
import org.springframework.stereotype.Service;

@Service
public class CassandraInputValidatorImpl implements CassandraInputValidator {
    @Override
    public boolean validateInput(String schemasPath, String queriesPath) {
        return true;
    }
}
