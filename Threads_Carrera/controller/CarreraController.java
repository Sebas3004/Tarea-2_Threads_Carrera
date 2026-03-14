package controller;

import model.Animal;
import model.CarreraModel;
import view.MainView;

import javax.swing.*;


public class CarreraController {

    private final CarreraModel modelo;
    private final MainView     vista;

    public CarreraController(CarreraModel modelo, MainView vista) {
        this.modelo = modelo;
        this.vista  = vista;

        configurarCallbacksModelo();
        configurarAccionesVista();
    }

    private void configurarCallbacksModelo() {

        modelo.setOnGanador(animal -> SwingUtilities.invokeLater(() -> {
            vista.mostrarGanador(animal.getEmoji() + " ¡" + animal.getNombre() + " GANÓ! 🏆");
        }));

        modelo.setOnLlegada(animal -> SwingUtilities.invokeLater(() -> {
            vista.actualizarEstado("Carrera en progreso...");
        }));

        modelo.setOnEvento(msg -> SwingUtilities.invokeLater(() -> {
            vista.agregarLog(msg);
        }));

  
        modelo.setOnCarreraFin(() -> SwingUtilities.invokeLater(() -> {
            vista.carreraFinalizada();
            vista.agregarLog("¡Carrera completada! Ver clasificación →");
        }));
    }


    private void configurarAccionesVista() {

        vista.setOnIniciar(() -> {
            modelo.iniciarCarrera();
            vista.carreraIniciada();
            vista.actualizarEstado("¡La carrera ha comenzado!");
        });

        vista.setOnReset(() -> {
            modelo.reset();
            vista.resetVista();
        });
    }
}