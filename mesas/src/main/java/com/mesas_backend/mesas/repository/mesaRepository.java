package com.mesas_backend.mesas.repository;

import com.mesas_backend.mesas.model.mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface mesaRepository extends JpaRepository<mesa,Long> {
}
