package com.example.wallet.impl;

import akka.Done;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface RepositoryTemplate<T> {

    CompletionStage<Done> create(T t) throws JsonProcessingException;

    CompletionStage<T> retrieve(String id);

    CompletionStage<Done> update(T t) throws JsonProcessingException;

    CompletionStage<Done> delete(String id);

    CompletionStage<List<T>>  showAllDocs();

}
