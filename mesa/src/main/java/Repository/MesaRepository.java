package Repository;

import Model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer> {

    // Buscar mesa por número
    Optional<Mesa> findByNumeroMesa(Integer numeroMesa);

    // Buscar mesas por estado (ejemplo: "Libre", "Reservado")
    List<Mesa> findByEstado(String estado);
}
