package com.restaurant.authusuarios.users.repository;

import com.restaurant.authusuarios.users.model.Usuario;
import com.restaurant.authusuarios.users.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRol(Rol rol);
}
