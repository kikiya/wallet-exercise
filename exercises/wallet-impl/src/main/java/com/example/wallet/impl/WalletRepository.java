package com.example.wallet.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.example.wallet.api.Currency;
import com.example.wallet.api.Wallet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Singleton
public class WalletRepository implements RepositoryTemplate<Wallet> {

    private  MongoCollection collection;
    private Materializer materializer;
    private ObjectMapper mapper;

    @Inject
    public WalletRepository(MongoCollection collection, Materializer materializer){
        this.collection = collection;
        this.materializer = materializer;
        this.mapper = new ObjectMapper();

    }

    @Override
    public CompletionStage<Done> create(Wallet wallet) throws JsonProcessingException {
        String walletString = mapper.writeValueAsString(wallet);
        final Source<Document, NotUsed> source =
                Source.fromPublisher(collection.insertOne(Document.parse(walletString)));
        return source.runWith(Sink.ignore(), materializer);
    }

    @Override
    public CompletionStage<Wallet> retrieve(String id) {
        final Source<Document, NotUsed> source = Source.fromPublisher(collection.find(eq("owner", id)));
        CompletionStage<Wallet> wallet = source.map(doc -> mapper.readValue(doc.toJson(), Wallet.class))
                .runWith(Sink.head(), materializer);
        return wallet;
    }

    @Override
    public CompletionStage<Done> update(Wallet wallet) throws JsonProcessingException {
        String walletString = mapper.writeValueAsString(wallet);
        final Source<Document, NotUsed> source = Source.fromPublisher(collection.replaceOne(
                eq("owner", wallet.owner), Document.parse(walletString) ));

        return source.runWith(Sink.ignore(), materializer);
    }

    @Override
    public CompletionStage<Done> delete(String id) {
        final Source<Document, NotUsed> source = Source.fromPublisher(collection.deleteOne(eq("Name", id)));
        return source.runWith(Sink.ignore(), materializer);
    }

    @Override
    public CompletionStage<List<Wallet>> showAllDocs (){
        final Source<Document, NotUsed> source = Source.fromPublisher(collection.find().limit(30));
        CompletionStage<List<Wallet>> documents  = source.map(doc-> mapper.readValue(doc.toJson(), Wallet.class))
                .runWith(Sink.seq(), materializer);
        return  documents;
    }

    public CompletionStage<Done> addCurrency(Wallet wallet, Currency currency) throws JsonProcessingException{
        BigDecimal amount = currency.amount.setScale(15, RoundingMode.CEILING);
        final Source<Document, NotUsed> source =
                Source.fromPublisher(collection.updateOne(
                       and( eq("owner", wallet.owner), eq("balances.type", currency.type)),
                        Updates.inc("balances.$.amount", amount)));

        return source.runWith(Sink.ignore(), materializer);
    }

}
