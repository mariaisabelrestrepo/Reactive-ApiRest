package com.co.javareactor.springwebfluxapirest.controllers;

import com.co.javareactor.springwebfluxapirest.models.document.Producto;
import com.co.javareactor.springwebfluxapirest.models.services.ProductoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoServices services;

    @GetMapping
    public Mono<ResponseEntity<Flux<Producto>>> lista(){
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(services.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> ver(@PathVariable String id){
        return services.findById(id).map(p -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
    @PostMapping
    public Mono<ResponseEntity<Producto>> crear(@RequestBody Producto producto){
        if(producto.getCreateAt()==null){
            producto.setCreateAt(new Date());
        }
        return services.save(producto).map(p -> ResponseEntity.created(URI.create("api/productos".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(p));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> actualizar(@RequestBody Producto producto, @PathVariable String id){
        return services.findById(id).flatMap(p -> {
            p.setNombre(producto.getNombre());
            p.setPrecio(producto.getPrecio());
            p.setCategoria(producto.getCategoria());
            return services.save(p);
        }).map(p -> ResponseEntity.created(URI.create("/api/productos".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
