package com.irojas.demojwt.Subasta;

import java.util.List;

import com.irojas.demojwt.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.Base64;
import com.irojas.demojwt.User.User;
import com.irojas.demojwt.User.UserRepository;
import com.irojas.demojwt.Subasta.AgregarRequest;
import com.irojas.demojwt.Subasta.Subasta.EstadoSubasta;


//archivos
import jakarta.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

//error http
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;




@Service
public class SubastaService {

    private final UserRepository userRepository; 
    private final SubastaRepository subastaRepository;

    public SubastaService(UserRepository userRepository, SubastaRepository subastaRepository) {
        this.userRepository = userRepository;
        this.subastaRepository = subastaRepository;
    }
    //recomendaciones por ahora genericas porque no hay categorias
    public List<SubastaDTO> obtenerRecomendacionesGenericas() {
        return subastaRepository.findTop10ByOrderByFechaCreacionDesc()
                .stream()
                .map(SubastaDTO::new)
                .collect(Collectors.toList());
    }

    //
    // seccion de crear y editar subastas
    //
    public SubastaDTO agregarSubasta(AgregarRequest request, int usuarioId) {
        User user = userRepository.findById(usuarioId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        List<String> rutasFotos = guardarFotos(request.getFotos());

        // Convierte el DTO a la entidad Subasta
        Subasta subasta = Subasta.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .precioInicial(request.getPrecioInicial())
            .fechaCreacion(new Date())
            .fechaCierre(request.getFechaCierre() != null ? request.getFechaCierre() : calcularFechaCierrePorDefecto())
            .fotos(rutasFotos)
            .user(user)
            .build();

        // Guarda el Subasta en la base de datos
        Subasta subastaGuardado = this.subastaRepository.save(subasta);

        // Devuelve el Subasta guardado como DTO
        return new SubastaDTO(subastaGuardado);
    }

    //guardar las fotos de las subastas
    private List<String> guardarFotos(List<String> fotosBase64) {
        List<String> rutasGuardadas = new ArrayList<>();
        try {
            for (String fotoBase64 : fotosBase64) {

                if (fotoBase64.contains(",")) {
                    fotoBase64 = fotoBase64.split(",")[1];
                }
                byte[] bytes = Base64.getDecoder().decode(fotoBase64);
                String nombreArchivo = System.currentTimeMillis() + ".jpg";
                Path rutaArchivo = Paths.get("uploads/fotos", nombreArchivo);

                // Crear directorios si no existen
                Files.createDirectories(rutaArchivo.getParent());
                Files.write(rutaArchivo, bytes);

                // Guardar la ruta como string
                rutasGuardadas.add(nombreArchivo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar fotos: " + e.getMessage());
        }
        return rutasGuardadas;
    }

    //funcion para leer fotos del directorio
    public byte[] obtenerFoto(String nombreArchivo) {
        String rutaFoto = "uploads/fotos/" + nombreArchivo;
        File file = new File(rutaFoto); // Ruta al archivo de la foto en el servidor

        // Si el archivo no existe o está vacío, retornamos un arreglo vacío
        if (!file.exists() || file.length() == 0) {
            return new byte[0]; // Foto vacía
        }
        try {
            // Leemos el archivo en bytes
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            // En caso de error al leer el archivo, devolvemos un arreglo vacío
            return new byte[0]; // Foto vacía
        }
    }

    // Método para modificar una subasta
    public SubastaDTO modificarSubasta(int subastaId, AgregarRequest request, int usuarioId) {
        // 1. Verificar que la subasta existe
        Subasta subastaExistente = subastaRepository.findById(Long.valueOf(subastaId)).orElse(null);
        if (subastaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subasta no encontrada");
        }

        // 2. Verificar que el usuario actual es el dueño de la subasta
        if (subastaExistente.getUser().getId() != usuarioId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para modificar esta subasta");
        }

        // 3. Actualizar los campos de la subasta
        if (request.getNombre() != null) {
            subastaExistente.setNombre(request.getNombre());
        }
        if (request.getDescripcion() != null) {
            subastaExistente.setDescripcion(request.getDescripcion());
        }
        if (request.getPrecioInicial() != null) {
            subastaExistente.setPrecioInicial(request.getPrecioInicial());
            if(subastaExistente.getPrecioActual() < subastaExistente.getPrecioInicial()){
                subastaExistente.setPrecioActual(request.getPrecioInicial());
            }
        }
        if (request.getFechaCierre() != null) {
            subastaExistente.setFechaCierre(request.getFechaCierre());
        } else {
            // Si no se proporciona una nueva fecha de cierre, no se actualiza
            subastaExistente.setFechaCierre(subastaExistente.getFechaCierre());
        }

        // 4. Eliminar las fotos viejas si es necesario
        if (request.getFotos() != null && !request.getFotos().isEmpty()) {
            // Eliminar fotos viejas del sistema de archivos
            eliminarFotosViejas(subastaExistente.getFotos());

            // Guardar las nuevas fotos
            List<String> rutasFotos = guardarFotos(request.getFotos());
            subastaExistente.setFotos(rutasFotos);
        }
        if (request.getEstado() != null){
            subastaExistente.setEstado(request.getEstado());
        }

        // 5. Guardar la subasta modificada
        Subasta subastaModificada = subastaRepository.save(subastaExistente);

        // 6. Convertir la entidad en DTO y devolverla
        return new SubastaDTO(subastaModificada);
    }

    // Método para eliminar fotos viejas
    private void eliminarFotosViejas(List<String> fotosViejas) {
        for (String foto : fotosViejas) {
            Path rutaArchivo = Paths.get("uploads/fotos", foto);
            try {
                // Eliminar archivo si existe
                Files.deleteIfExists(rutaArchivo);
            } catch (IOException e) {
                throw new RuntimeException("Error al eliminar fotos viejas: " + e.getMessage());
            }
        }
    }

    private Date calcularFechaCierrePorDefecto() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7); // Por defecto, la subasta cierra en 7 días
        return calendar.getTime();
    }
    //
    //  fin crear y editar subastas
    // 

    //
    //  cerrar y cancelar subastas
    //
    public SubastaDTO cerrarSubasta(int subastaId, int usuarioId) {
        // 1. Verificar que la subasta existe
        Subasta subastaExistente = subastaRepository.findById(Long.valueOf(subastaId)).orElse(null);
        if (subastaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subasta no encontrada");
        }
        // 2. Verificar que el usuario actual es el dueño de la subasta
        if (subastaExistente.getUser().getId() != usuarioId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para cerrar esta subasta");
        }
        // 3. Cambiar el estado a CERRADA si está activa
        if (subastaExistente.getEstado() != EstadoSubasta.ACTIVA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La subasta no está activa y no se puede cerrar");
        }
        subastaExistente.setEstado(EstadoSubasta.FINALIZADA);
        // 4. Guardar la subasta modificada
        Subasta subastaCerrada = subastaRepository.save(subastaExistente);
        // 5. Convertir la entidad en DTO y devolverla
        return new SubastaDTO(subastaCerrada);
    }

    public SubastaDTO cancelarSubasta(int subastaId, int usuarioId) {
        // 1. Verificar que la subasta existe
        Subasta subastaExistente = subastaRepository.findById(Long.valueOf(subastaId)).orElse(null);
        if (subastaExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subasta no encontrada");
        }

        // 2. Verificar que el usuario actual es el dueño de la subasta
        if (subastaExistente.getUser().getId() != usuarioId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para cancelar esta subasta");
        }

        // 3. Cambiar el estado a CANCELADA si está activa
        if (subastaExistente.getEstado() != EstadoSubasta.ACTIVA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La subasta no está activa y no se puede cancelar");
        }

        subastaExistente.setEstado(EstadoSubasta.CANCELADA);

        // 4. Guardar la subasta modificada
        Subasta subastaCancelada = subastaRepository.save(subastaExistente);

        // 5. Convertir la entidad en DTO y devolverla
        return new SubastaDTO(subastaCancelada);
    }

    //
    // fin cerrar y cancelar subastas
    //



    // Método para obtener una subasta por su ID
    public SubastaDTO obtenerSubastaPorId(int subastaId) {
        // Buscar la subasta en la base de datos
        Subasta subasta = subastaRepository.findById(Long.valueOf(subastaId)).orElse(null);
        
        // Si no se encuentra la subasta, lanzar un error 404
        if (subasta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subasta no encontrada");
        }

        // Convertir la subasta en DTO y devolverla
        return new SubastaDTO(subasta);
    }

    public List<SubastaDTO> obtenerSubastasPorUsuario(int id) {
        User usuario = userRepository.findById(id).orElse(null);
        if(usuario == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no existe");
        }
        List<Subasta> subastas = subastaRepository.findByUser(usuario);

        // Verificar si cada oferta es ganadora
        // for (Oferta oferta : ofertas) {
        //     verificar_ganadora(oferta);
        // }
        // Llama al constructor de OfertaDTO que toma una Oferta
        return  subastas.stream().map(SubastaDTO::new).toList();
    }

    //
    //  utiliades
    //

    //consegir id de nombre de usuario
    public int get_user_id(String usuario){
        User user=userRepository.findByUsername(usuario).orElseThrow();
        return user.getId();
    }
    //
    //  fin utilidades
    //

}


// @Transactional
    // public SubastaDTO agregarSubasta(SubastaDTO subastaDTO, int usuarioId) {
    //     User user = userRepository.findById(usuarioId).orElse(null);
    //     if (user == null) {
    //         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    //     }
    //     List<String> rutasFotos = guardarFotos(subastaDTO.getFotos());

    //     // Convierte el DTO a la entidad Subasta
    //     Subasta subasta = Subasta.builder()
    //         .nombre(subastaDTO.getNombre())
    //         .descripcion(subastaDTO.getDescripcion())
    //         .precioInicial(subastaDTO.getPrecioInicial())
    //         .fechaCreacion(new Date())// si fecha de cierre viene null se le asigna por defecto 7 dias
    //         .fechaCierre(subastaDTO.getFechaCierre() != null ? subastaDTO.getFechaCierre() : calcularFechaCierrePorDefecto())
    //         .fotos(rutasFotos)
    //         .user(user)
    //         .build();

    //     // Guarda el Subasta en la base de datos
    //     Subasta subastaGuardado = this.subastaRepository.save(subasta);

    //     // Devuelve el Subasta guardado como DTO
    //     return new SubastaDTO(subastaGuardado);
    // }