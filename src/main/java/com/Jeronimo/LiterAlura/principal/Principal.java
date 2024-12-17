package com.Jeronimo.LiterAlura.principal;

import com.Jeronimo.LiterAlura.modelos.*;
import com.Jeronimo.LiterAlura.repositorio.IAutorRepositorio;
import com.Jeronimo.LiterAlura.servicios.ConexionApi;
import com.Jeronimo.LiterAlura.servicios.ConvierteDatos;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConexionApi conexionApi = new ConexionApi();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private IAutorRepositorio repositorio;


    public Principal(IAutorRepositorio repositorio) {

        this.repositorio = repositorio;
    }

    public void muestraElMenu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n ¡¡BIENVENIDO AL LITERALURA!! \n
                    1 - Buscar libro por título
                    2 - Buscar autor por nombre
                    3 - Mostrar libros Guardados
                    4 - Autores vivos en un determinado año
                    5 - Top 10 libros más descargados
                    6 - Mostrar autores nacidos en algún año
                    7 - Mostrar autores fallecidos en algún año
                    0 - Salir
                    """;

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    autorPorNombre();
                    break;
                case 3:
                    mostrarLibrosGuardados();
                    break;
                case 4:
                    autoresVivos();
                    break;
                case 5:
                    top10();
                    break;
                case 6:
                   autorPorFechaNacimiento();
                    break;
                case 7:
                    autorPorFechaFallecimiento();
                    break;
                case 0:
                    System.out.println("Cerrando aplicación ... \n");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }


    private void buscarLibroPorTitulo() {
        System.out.println("Ingresa el título del libro que desea buscar: ");
        var titulo = teclado.nextLine();
        var json = conexionApi.obtenerDatos(URL_BASE + "?search=" + titulo.replace(" ", "+").toLowerCase());

        if (json.isEmpty() || !json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {
            var datos = conversor.obtenerDatos(json, DatosDTO.class);

            Optional<DatosLibrosDTO> libroBuscado = datos.libros().stream()
                    .findFirst();

            if (libroBuscado.isPresent()) {
                System.out.println(
                        "\n------------- LIBRO --------------" +
                                "\nTítulo: " + libroBuscado.get().titulo() +
                                "\nAutor: " + libroBuscado.get().autores().stream()
                                .map(a -> a.nombre()).limit(1).collect(Collectors.joining()) +
                                "\nIdiomaEnum: " + libroBuscado.get().idiomas().stream()
                                .collect(Collectors.joining()) +
                                "\nNúmero de descargas: " + libroBuscado.get().numeroDeDescargas() +
                                "\n--------------------------------------\n"
                );

                try {
                    List<Libro> libroEncontrado = libroBuscado.stream()
                            .map(a -> new Libro(a))
                            .collect(Collectors.toList());

                    Autor autorAPI = libroBuscado.stream()
                            .flatMap(l -> l.autores().stream()
                                    .map(a -> new Autor(a)))
                            .collect(Collectors.toList()).stream().findFirst().get();

                    Optional<Autor> autorBD = repositorio.buscarAutorPorNombre(libroBuscado.get().autores().stream()
                            .map(a -> a.nombre())
                            .collect(Collectors.joining()));

                    Optional<Libro> libroOptional = repositorio.buscarLibroPorNombre(titulo);

                    if (libroOptional.isPresent()) {
                        System.out.println("El libro ya está guardado en la BD.");
                    } else {
                        Autor autor;
                        if (autorBD.isPresent()) {
                            autor = autorBD.get();
                            System.out.println("EL autor ya esta guardado en la BD");
                        } else {
                            autor = autorAPI;
                            repositorio.save(autor);
                        }
                        autor.setLibros(libroEncontrado);
                        repositorio.save(autor);
                    }
                } catch (Exception e) {
                    System.out.println("Warning! " + e.getMessage());
                }
            } else {
                System.out.println("Libro no encontrado");
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void autorPorNombre(){
        System.out.println("Ingrese el nombre del autor que desea buscar");

        try {
            var autorBuscado = teclado.nextLine();
            Optional<Autor> autor = repositorio.buscarAutorPorNombre(autorBuscado);
            if (autor.isPresent()){
                autor.stream()
                        .forEach(System.out::println);
            } else {
                System.out.println("Autor no encontrado");
            }
        } catch (Exception e){
            System.out.println("Ingrese un nombre correcto. - Warning: " + e.getMessage());
        }

    }

    private void mostrarLibrosGuardados() {
        List<Libro> libros = repositorio.librosGuardados();
        libros.forEach(System.out::println);
    }


    private void autoresVivos() {
        System.out.println("Ingrese un año para verificar el autor que desea buscar");

        try {
            var fecha = Integer.parseInt(teclado.nextLine());
            List<Autor> autores = repositorio.AutoresVivos(fecha);

            if (!autores.isEmpty()){
                autores.stream()
                        .sorted(Comparator.comparing(Autor::getNombre))
                        .forEach(System.out::println);
            } else {
                System.out.println("Ningún autor vivo encontrado en este año");
            }

        } catch (NumberFormatException e){
            System.out.println("Ingrese un año válido " + e.getMessage());
        }

    }

    private void top10() {
        System.out.println("\nTop 10 libros más descargados:\n");
        var json = conexionApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, DatosDTO.class);
        datos.libros().stream()
                .sorted(Comparator.comparing(DatosLibrosDTO::numeroDeDescargas).reversed())
                .limit(10)
                .forEach(l ->
                        System.out.println("[" + l.numeroDeDescargas() + " descargas] - " + l.titulo()));
    }

    private void autorPorFechaNacimiento(){
        System.out.println("Ingrese el año de nacimiento del autor");
        try {
            var nacimiento = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = repositorio.listarAutorPorFechaNacimiento(nacimiento);
            if (!autores.isEmpty()) {
                autores.stream()
                        .forEach(System.out::println);
            } else {
                System.out.println("No se encontró ningún actor nacido en este año");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ingrese un año válido. - Warning: " + e.getMessage());
        }
    }

    private void autorPorFechaFallecimiento(){
        System.out.println("Ingrese el año de fallecimiento del autor");
        try {
            var fallecimiento = Integer.valueOf(teclado.nextLine());
            List<Autor> autor = repositorio.listarAutorPorFechaFallecimiento(fallecimiento);
            if (!autor.isEmpty()) {
                autor.stream()
                        .forEach(System.out::println);
            } else {
                System.out.println("No se encontró ningún actor fallecido en este año");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un año válido. - Warning: " + e.getMessage());
        }
    }
}