q1/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.habistell;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sistema de Venta de Boletos para Rifa de Casa
 * Genera numeros de 4 digitos al azar para cada boleto
 * @author Kelin 2
 */

// Clase Cliente
class Cliente {
    private final String nombre;
    private final String cedula;
    private final String telefono;
    
    public Cliente(String nombre, String cedula, String telefono) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public String getCedula() { return cedula; }
    public String getTelefono() { return telefono; }
    
    @Override
    public String toString() {
        return "Cliente: " + nombre + " | Cedula: " + cedula + " | Tel: " + telefono;
    }
}

// Clase Boleto
class Boleto {
    private final String numeroRifa;
    private final double precio;
    
    public Boleto(String numeroRifa, double precio) {
        this.numeroRifa = numeroRifa;
        this.precio = precio;
    }
    
    public String getNumeroRifa() { return numeroRifa; }
    public double getPrecio() { return precio; }
    
    @Override
    public String toString() {
        return "Boleto: " + numeroRifa + " | Precio: $" + precio;
    }
}

// Clase Venta
class Venta {
    private static int contadorVentas = 1;
    private final int numeroVenta;
    private final Cliente cliente;
    private final ArrayList<Boleto> boletos;
    private final double total;
    private final String fecha;
    
    public Venta(Cliente cliente, ArrayList<Boleto> boletos) {
        this.numeroVenta = contadorVentas++;
        this.cliente = cliente;
        this.boletos = boletos;
        this.total = calcularTotal();
        // Formato de fecha mejorado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.fecha = LocalDateTime.now().format(formatter);
    }
    
    private double calcularTotal() {
        double suma = 0;
        for (Boleto b : boletos) {
            suma += b.getPrecio();
        }
        return suma;
    }
    
    public ArrayList<Boleto> getBoletos() { return boletos; }
    public Cliente getCliente() { return cliente; }
    public int getNumeroVenta() { return numeroVenta; }
    public double getTotal() { return total; }
    public String getFecha() { return fecha; }
    
    public void imprimirRecibo() {
        System.out.println("\n========================================================");
        System.out.println("       RECIBO DE VENTA - RIFA DE CASA #" + numeroVenta);
        System.out.println("========================================================");
        System.out.println("\n" + cliente);
        System.out.println("\n--- TUS NUMEROS DE LA SUERTE ---");
        for (Boleto b : boletos) {
            System.out.println("   >> " + b.getNumeroRifa() + " <<");
        }
        System.out.println("\n---------------------------");
        System.out.println("Cantidad de boletos: " + boletos.size());
        System.out.println("Precio por boleto: $" + String.format("%.2f", boletos.get(0).getPrecio()));
        System.out.println("TOTAL PAGADO: $" + String.format("%.2f", total));
        System.out.println("FECHA: " + fecha);
        System.out.println("========================================================");
        System.out.println("     CONSERVA ESTE RECIBO - MUCHA SUERTE!");
        System.out.println("========================================================\n");
    }
}

public class Habistell {
    
    private static final ArrayList<Cliente> clientes = new ArrayList<>();
    private static final ArrayList<Venta> ventas = new ArrayList<>();
    private static final HashSet<String> numerosVendidos = new HashSet<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static double PRECIO_BOLETO = 10.0;
    
    public static void main(String[] args) {
        int opcion;
        
        System.out.println("\n****************************************************");
        System.out.println("*                                                  *");
        System.out.println("*     BIENVENIDO AL SISTEMA DE RIFA DE CASA       *");
        System.out.println("*                                                  *");
        System.out.println("****************************************************\n");
        
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opcion: ");
            
            switch (opcion) {
                case 1 -> registrarCliente();
                case 2 -> realizarVenta();
                case 3 -> verHistorialVentas();
                case 4 -> verClientes();
                case 5 -> verTodosLosNumerosVendidos();
                case 6 -> cambiarPrecioBoleto();
                case 7 -> {
                    System.out.println("\n============================================");
                    System.out.println("  Gracias por usar el sistema!");
                    System.out.println("  Hasta pronto.");
                    System.out.println("============================================\n");
                }
                default -> System.out.println("\n[X] Opcion invalida. Intente nuevamente.\n");
            }
        } while (opcion != 7);
        
        scanner.close();
    }
    
    private static void mostrarMenuPrincipal() {
        System.out.println("\n================================================");
        System.out.println("        SISTEMA DE RIFA DE CASA");
        System.out.println("    Precio actual: $" + String.format("%.2f", PRECIO_BOLETO) + " por boleto");
        System.out.println("================================================");
        System.out.println("  1. Registrar nuevo cliente");
        System.out.println("  2. Vender boletos");
        System.out.println("  3. Ver historial de ventas");
        System.out.println("  4. Ver clientes registrados");
        System.out.println("  5. Ver todos los numeros vendidos");
        System.out.println("  6. Cambiar precio de boleto");
        System.out.println("  7. Salir");
        System.out.println("================================================");
        System.out.println("  Total de boletos vendidos: " + numerosVendidos.size());
        System.out.println("  Total de clientes: " + clientes.size());
        System.out.println("  Total de ventas: " + ventas.size());
        System.out.println("================================================");
    }
    
    private static void registrarCliente() {
        System.out.println("\n============================================");
        System.out.println("         REGISTRO DE CLIENTE");
        System.out.println("============================================");
        
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        
        if (nombre.isEmpty()) {
            System.out.println("\n[X] El nombre no puede estar vacio.\n");
            return;
        }
        
        System.out.print("Cedula/DNI: ");
        String cedula = scanner.nextLine().trim();
        
        if (cedula.isEmpty()) {
            System.out.println("\n[X] La cedula no puede estar vacia.\n");
            return;
        }
        
        // Verificar si la cedula ya existe
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                System.out.println("\n[X] Ya existe un cliente con esta cedula.\n");
                return;
            }
        }
        
        System.out.print("Telefono: ");
        String telefono = scanner.nextLine().trim();
        
        if (telefono.isEmpty()) {
            System.out.println("\n[X] El telefono no puede estar vacio.\n");
            return;
        }
        
        Cliente nuevoCliente = new Cliente(nombre, cedula, telefono);
        clientes.add(nuevoCliente);
        
        System.out.println("\n[OK] Cliente registrado exitosamente!");
        System.out.println("--------------------------------------------");
        System.out.println(nuevoCliente);
        System.out.println("--------------------------------------------\n");
    }
    
    private static void realizarVenta() {
        if (clientes.isEmpty()) {
            System.out.println("\n[X] No hay clientes registrados.");
            System.out.println("    Registre un cliente primero (Opcion 1).\n");
            return;
        }
        
        System.out.println("\n============================================");
        System.out.println("      VENTA DE BOLETOS PARA RIFA");
        System.out.println("============================================");
        
        Cliente clienteSeleccionado = seleccionarCliente();
        if (clienteSeleccionado == null) {
            return;
        }
        
        int cantidad = leerEntero("\nCuantos boletos desea comprar? (1-50): ");
        
        if (cantidad < 1 || cantidad > 50) {
            System.out.println("\n[X] Cantidad invalida. Debe ser entre 1 y 50.\n");
            return;
        }
        
        // Verificar si hay suficientes números disponibles
        if (numerosVendidos.size() + cantidad > 10000) {
            System.out.println("\n[X] No hay suficientes numeros disponibles.");
            System.out.println("    Solo quedan " + (10000 - numerosVendidos.size()) + " numeros disponibles.\n");
            return;
        }
        
        ArrayList<Boleto> boletos = new ArrayList<>();
        System.out.println("\nGENERANDO TUS NUMEROS DE LA SUERTE...\n");
        
        for (int i = 0; i < cantidad; i++) {
            String numeroRifa = generarNumeroUnico();
            boletos.add(new Boleto(numeroRifa, PRECIO_BOLETO));
            numerosVendidos.add(numeroRifa);
            System.out.println("   " + (i + 1) + ". Numero: " + numeroRifa);
        }
        
        double total = PRECIO_BOLETO * cantidad;
        System.out.println("\n============================================");
        System.out.println("         RESUMEN DE COMPRA");
        System.out.println("============================================");
        System.out.println("  Cliente: " + clienteSeleccionado.getNombre());
        System.out.println("  Cantidad de boletos: " + cantidad);
        System.out.println("  Precio por boleto: $" + String.format("%.2f", PRECIO_BOLETO));
        System.out.println("--------------------------------------------");
        System.out.println("  TOTAL A PAGAR: $" + String.format("%.2f", total));
        System.out.println("============================================");
        
        System.out.print("\nConfirmar compra? (S/N): ");
        String confirmacion = scanner.nextLine().toUpperCase();
        
        if (confirmacion.equals("S") || confirmacion.equals("SI")) {
            Venta nuevaVenta = new Venta(clienteSeleccionado, boletos);
            ventas.add(nuevaVenta);
            nuevaVenta.imprimirRecibo();
            System.out.println("[OK] Venta realizada exitosamente!");
            System.out.println("     MUCHA SUERTE EN LA RIFA!\n");
        } else {
            // Devolver los números al pool de disponibles
            for (Boleto b : boletos) {
                numerosVendidos.remove(b.getNumeroRifa());
            }
            System.out.println("\n[X] Venta cancelada.\n");
        }
    }
    
    private static String generarNumeroUnico() {
        String numero;
        do {
            int numeroAleatorio = random.nextInt(10000);
            numero = String.format("%04d", numeroAleatorio);
        } while (numerosVendidos.contains(numero));
        
        return numero;
    }
    
    private static Cliente seleccionarCliente() {
        System.out.println("\n============================================");
        System.out.println("        CLIENTES REGISTRADOS");
        System.out.println("============================================");
        
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + clientes.get(i).getNombre() + 
                             " (Cedula: " + clientes.get(i).getCedula() + ")");
        }
        System.out.println("--------------------------------------------");
        
        int seleccion = leerEntero("\nSeleccione el numero de cliente: ");
        
        if (seleccion < 1 || seleccion > clientes.size()) {
            System.out.println("\n[X] Seleccion invalida.\n");
            return null;
        }
        
        return clientes.get(seleccion - 1);
    }
    
    private static void verHistorialVentas() {
        if (ventas.isEmpty()) {
            System.out.println("\n============================================");
            System.out.println("  [INFO] No hay ventas registradas");
            System.out.println("============================================\n");
            return;
        }
        
        System.out.println("\n================================================");
        System.out.println("           HISTORIAL DE VENTAS");
        System.out.println("================================================");
        System.out.println("Total de ventas: " + ventas.size());
        System.out.println("------------------------------------------------\n");
        
        double totalRecaudado = 0;
        int totalBoletosVendidos = 0;
        
        for (Venta v : ventas) {
            v.imprimirRecibo();
            totalRecaudado += v.getTotal();
            totalBoletosVendidos += v.getBoletos().size();
        }
        
        System.out.println("\n================================================");
        System.out.println("           RESUMEN GENERAL");
        System.out.println("================================================");
        System.out.println("  Total de ventas: " + ventas.size());
        System.out.println("  Total de boletos vendidos: " + totalBoletosVendidos);
        System.out.println("  Total recaudado: $" + String.format("%.2f", totalRecaudado));
        System.out.println("================================================\n");
    }
    
    private static void verClientes() {
        if (clientes.isEmpty()) {
            System.out.println("\n============================================");
            System.out.println("  [INFO] No hay clientes registrados");
            System.out.println("============================================\n");
            return;
        }
        
        System.out.println("\n================================================");
        System.out.println("           CLIENTES REGISTRADOS");
        System.out.println("================================================");
        System.out.println("Total de clientes: " + clientes.size());
        System.out.println("------------------------------------------------\n");
        
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println((i + 1) + ". " + clientes.get(i));
            System.out.println("   ------------------------------------------------");
        }
        System.out.println();
    }
    
    private static void verTodosLosNumerosVendidos() {
        if (numerosVendidos.isEmpty()) {
            System.out.println("\n============================================");
            System.out.println("  [INFO] No hay numeros vendidos todavia");
            System.out.println("============================================\n");
            return;
        }
        
        System.out.println("\n================================================");
        System.out.println("         NUMEROS DE RIFA VENDIDOS");
        System.out.println("================================================");
        System.out.println("  Total de boletos vendidos: " + numerosVendidos.size());
        System.out.println("  Numeros disponibles: " + (10000 - numerosVendidos.size()));
        System.out.println("  Porcentaje vendido: " + String.format("%.2f", (numerosVendidos.size() * 100.0 / 10000)) + "%");
        System.out.println("================================================\n");
        
        ArrayList<String> numerosLista = new ArrayList<>(numerosVendidos);
        numerosLista.sort(String::compareTo);
        
        System.out.println("NUMEROS VENDIDOS (ordenados):");
        System.out.println("------------------------------------------------");
        
        int contador = 0;
        for (String numero : numerosLista) {
            System.out.print(numero + "  ");
            contador++;
            if (contador % 10 == 0) {
                System.out.println();
            }
        }
        
        if (contador % 10 != 0) {
            System.out.println();
        }
        
        System.out.println("------------------------------------------------\n");
    }
    
    private static void cambiarPrecioBoleto() {
        System.out.println("\n============================================");
        System.out.println("       CAMBIAR PRECIO DE BOLETO");
        System.out.println("============================================");
        System.out.println("Precio actual: $" + String.format("%.2f", PRECIO_BOLETO));
        
        System.out.print("\nIngrese el nuevo precio: $");
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print("[X] Por favor ingrese un precio valido: $");
        }
        double nuevoPrecio = scanner.nextDouble();
        scanner.nextLine();
        
        if (nuevoPrecio <= 0) {
            System.out.println("\n[X] Precio invalido. Debe ser mayor a 0.\n");
            return;
        }
        
        PRECIO_BOLETO = nuevoPrecio;
        System.out.println("\n[OK] Precio actualizado exitosamente!");
        System.out.println("     Nuevo precio: $" + String.format("%.2f", PRECIO_BOLETO) + "\n");
    }
    
    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("[X] Por favor ingrese un numero valido: ");
        }
        int numero = scanner.nextInt();
        scanner.nextLine();
        return numero;
    }
}