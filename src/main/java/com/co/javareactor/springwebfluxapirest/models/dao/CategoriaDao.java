package com.co.javareactor.springwebfluxapirest.models.dao;


import com.co.javareactor.springwebfluxapirest.models.document.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria,String> {
}
