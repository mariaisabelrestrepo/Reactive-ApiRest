package com.co.javareactor.springwebfluxapirest.controllers;

import com.co.javareactor.springwebfluxapirest.models.document.Producto;
import com.co.javareactor.springwebfluxapirest.models.services.ProductoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoServices services;


    @Value("${config.uploads.path}")
    private String path;
    @PostMapping("/v2")
    public Mono<ResponseEntity<Producto>> crear(Producto producto,@RequestPart FilePart file) {
        if (producto.getCreateAt() == null) {
            producto.setCreateAt(new Date());
        }
        producto.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                .replace(" ", "")
                .replace(":", "")
                .replace("\\", ""));

        return file.transferTo(new File(path + producto.getFoto()))
                .then(services.save(producto))
                .map(p -> ResponseEntity.created(URI.create("api/productos".concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(p));
    }

    @PostMapping("/upload/{id}")
    public Mono<ResponseEntity<Producto>> upload(@PathVariable String id, @RequestPart FilePart file) {
        return services.findById(id).flatMap(p -> {
                    p.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                            .replace(" ", "")
                            .replace(":", "")
                            .replace("\\", ""));
                    return file.transferTo(new File(path + p.getFoto())).then(services.save(p));
                }).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping
    public Mono<ResponseEntity<Flux<Producto>>> lista() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(services.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> ver(@PathVariable String id) {
        return services.findById(id).map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @PostMapping
    public Mono<ResponseEntity<Producto>> crear(@RequestBody Producto producto) {
        if (producto.getCreateAt() == null) {
            producto.setCreateAt(new Date());
        }
        return services.save(producto).map(p -> ResponseEntity.created(URI.create("api/productos".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(p));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> actualizar(@RequestBody Producto producto, @PathVariable String id) {
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

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return services.findById(id).flatMap(p -> {
            return services.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

}
