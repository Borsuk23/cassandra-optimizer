package eu.asyroka.msc.service;

public interface CassandraInputValidator {
    boolean validateInput(String schemasPath, String queriesPath);
}
