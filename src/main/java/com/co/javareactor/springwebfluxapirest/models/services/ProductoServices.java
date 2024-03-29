package com.co.javareactor.springwebfluxapirest.models.services;

import com.co.javareactor.springwebfluxapirest.models.document.Categoria;
import com.co.javareactor.springwebfluxapirest.models.document.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoServices {

    public Flux<Producto> findAll();
    public Flux<Producto>findAllConNombre();
    public Mono<Producto>findById(String id);
    public Mono<Producto>save(Producto producto);
    public Mono<Void>delete(Producto producto);
    public Flux<Categoria>findAllCategoria();
    public Mono<Categoria>findCategoriaById(String id);
    public Mono<Categoria>saveCategoria(Categoria categoria);
}
