package com.arbitr;

import com.arbitr.model.DexCurrency;
import com.arbitr.repository.impl.CassandraRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

    @Inject
    CassandraRepository repository;
    @GET

    public Multi<DexCurrency> hello() {
        return repository.findAllByAbsoluteValue(0.);
    }
}
