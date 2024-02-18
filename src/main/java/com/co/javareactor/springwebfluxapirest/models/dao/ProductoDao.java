package com.co.javareactor.springwebfluxapirest.models.dao;

import com.co.javareactor.springwebfluxapirest.models.document.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoDao extends ReactiveMongoRepository<Producto,String> {

}