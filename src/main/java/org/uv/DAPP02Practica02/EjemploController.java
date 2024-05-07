package org.uv.DAPP02Practica02;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author yodoeaoffi06
 */
@RestController
@RequestMapping("/data")
public class EjemploController {

    private ArrayList<Data> dataList = new ArrayList<>();

    @PostMapping("/create")
    public Data createData(@RequestBody Data newData) {
        dataList.add(newData);
        return newData;
    }

    @GetMapping("/getall")
    public ArrayList<Data> getAllData() {
        return dataList;
    }

    @GetMapping("/getbyid/{id}")
    public Data getDataById(@PathVariable int id) {
        return dataList.stream()
                .filter(data -> data.getClave().equals(Integer.toString(id)))
                .findFirst()
                .orElse(null);
    }

    @PutMapping("/update/{id}")
    public Data updateData(@PathVariable int id, @RequestBody Data updatedData) {
        Data existingData = getDataById(id);
        if (existingData != null) {
            existingData.setNombre(updatedData.getNombre());
            existingData.setDescripcion(updatedData.getDescripcion());
        }
        return existingData;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteData(@PathVariable int id) {
        dataList.removeIf(data -> data.getClave().equals(Integer.toString(id)));
    }

    @Autowired
    private RepositoryEmpleado repositoryEmpleado;

    @PostMapping(path = "/empleado/add", produces = {"application/json"})
    public ResponseEntity<Empleado> addEmpleado(@RequestBody Empleado empleado) {
         try {
            Empleado nuevoEmpleado = repositoryEmpleado.save(empleado);
            return ResponseEntity.ok().body(nuevoEmpleado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping(path = "/empleado/update/{id}", produces = {"application/json"})
    public ResponseEntity<Empleado> updateEmpleado(@PathVariable("id") Long id, @RequestBody Map<String, Object> camposActualizados) {
        Optional<Empleado> empleadoOptional = repositoryEmpleado.findById(id);

        if (empleadoOptional.isPresent()) {
            Empleado empleado = empleadoOptional.get();

            camposActualizados.forEach((nombreCampo, valorCampo) -> {
                switch (nombreCampo) {
                    case "nombre":
                        empleado.setNombre((String) valorCampo);
                        break;
                    case "direccion":
                        empleado.setDireccion((String) valorCampo);
                        break;
                    case "telefono":
                        empleado.setTelefono((String) valorCampo);
                        break;
                    default:
                        throw new IllegalArgumentException("Campo desconocido: " + nombreCampo);
                }
            });

            Empleado nuevoEmpleado = repositoryEmpleado.save(empleado);
            return ResponseEntity.ok().body(nuevoEmpleado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping(path = "empleado/delete/{id}", produces = {"application/json"})
    public ResponseEntity<String> deleteEmpleado(@PathVariable Long id) {
        try {
            repositoryEmpleado.deleteById(id);
            return ResponseEntity.ok().body("Empleado eliminado correctamente");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado no encontrado con ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el empleado");
        }
    }
    
    @GetMapping(path = "/empleado/get/{id}", produces = {"application/json"})
    public ResponseEntity<Empleado> getEmpleado(@PathVariable("id") Long id) {
        Optional<Empleado> res = repositoryEmpleado.findById(id);
        if (res.isPresent()) {
            Empleado empleado = res.get();
            return ResponseEntity.ok().body(empleado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @GetMapping(path = "empleado/all", produces = {"application/json"})
    public ResponseEntity<List<Empleado>> getAllEmpleados() {
        try {
            List<Empleado> empleados = new ArrayList<>();
            repositoryEmpleado.findAll().forEach(empleados::add);
            return ResponseEntity.ok().body(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
