package views;

import data.Persistencia;
import domain.Marca;
import domain.Sucursal;
import domain.Vehiculo;
import domain.VehiculoCombustible;
import domain.VehiculoElectrico;
import domain.VehiculoTipo;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class Controlador {

    public static ArrayList<VehiculoViewModel> getVehiculos() {
        ArrayList<VehiculoViewModel> vehiculos = new ArrayList<>();
        for (Vehiculo vehiculo : Persistencia.getVehiculos()) {
            vehiculos.add(new VehiculoViewModel(vehiculo));
        }
        return vehiculos;
    }

    public static ArrayList<Marca> getMarcas() {
        return Persistencia.getMarcas();
    }

    public static ArrayList<Sucursal> getSucursales() {
        return Persistencia.getSucursales();
    }

    public static double[] calcularConsumos(Map<String, Double> vehiculos) {
        double consumoElectricos = 0;
        double consumoCombustible = 0;
        for (Map.Entry<String, Double> entry : vehiculos.entrySet()) {
            double consumo = 0;
            Optional<Vehiculo> vehiculo = Persistencia.getVehiculo(entry.getKey());
            if (vehiculo.isPresent()) {
                consumo = vehiculo.get().calcularConsumo(entry.getValue());
                consumoElectricos += vehiculo.get().esDe(VehiculoTipo.ELECTRICO) ? consumo : 0;
                consumoCombustible += vehiculo.get().esDe(VehiculoTipo.COMBUSTIBLE) ? consumo : 0;
            }
        }
        return new double[]{consumoElectricos, consumoCombustible};
    }

    public static void agregarVehiculo(String patente, Marca marca, String modelo, int anio,
            double capacidadCarga, Sucursal sucursal, VehiculoTipo tipo,
            double kwhBase, double kilometrosPorLitro, double litrosExtra) {

        String patenteNormalizada = patente == null ? "" : patente.trim().toUpperCase();

        if (patenteNormalizada.isEmpty()) {
            throw new IllegalArgumentException("La patente es obligatoria.");
        }
        if (marca == null) {
            throw new IllegalArgumentException("Debe seleccionar una marca.");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio.");
        }
        if (anio <= 0) {
            throw new IllegalArgumentException("El año debe ser válido.");
        }
        if (capacidadCarga <= 0) {
            throw new IllegalArgumentException("La capacidad de carga debe ser mayor a 0.");
        }
        if (sucursal == null) {
            throw new IllegalArgumentException("Debe seleccionar una sucursal.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de vehículo.");
        }
        if (Persistencia.getVehiculo(patenteNormalizada).isPresent()) {
            throw new IllegalArgumentException("Ya existe un vehículo con esa patente.");
        }

        Vehiculo vehiculo;
        if (tipo == VehiculoTipo.ELECTRICO) {
            if (kwhBase <= 0) {
                throw new IllegalArgumentException("El kWh base debe ser mayor a 0.");
            }
            vehiculo = new VehiculoElectrico(
                    patenteNormalizada,
                    marca,
                    modelo.trim(),
                    anio,
                    capacidadCarga,
                    sucursal,
                    kwhBase
            );
        } else {
            if (kilometrosPorLitro <= 0) {
                throw new IllegalArgumentException("Los km por litro deben ser mayores a 0.");
            }
            if (litrosExtra < 0) {
                throw new IllegalArgumentException("Los litros extra no pueden ser negativos.");
            }
            vehiculo = new VehiculoCombustible(
                    patenteNormalizada,
                    marca,
                    modelo.trim(),
                    anio,
                    capacidadCarga,
                    sucursal,
                    kilometrosPorLitro,
                    litrosExtra
            );
        }

        Persistencia.agregarVehiculo(vehiculo);
    }
}
