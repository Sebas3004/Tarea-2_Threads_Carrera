package model;

import java.awt.Color;
import java.util.Random;


public class Animal implements Runnable {


    private final String nombre;
    private final String emoji;
    private final Color  color;
    private final int    velocidadBase; 

   
    private volatile int     progreso  = 0;
    private volatile int     lugar     = 0;      
    private volatile String  evento    = "";       
    private volatile boolean corriendo = false;

    private CarreraModel modelo;

    public Animal(String nombre, String emoji, Color color, int velocidadBase) {
        this.nombre        = nombre;
        this.emoji         = emoji;
        this.color         = color;
        this.velocidadBase = velocidadBase;
    }

    public void setModelo(CarreraModel modelo) {
        this.modelo = modelo;
    }

    
    @Override
    public void run() {
        corriendo = true;
        Random rng = new Random();

        while (progreso < 100 && corriendo) {
            try {
                Thread.sleep(velocidadBase + rng.nextInt(40) - 20);

                int chance = rng.nextInt(100);

                if (chance < 8) {
                
                    int pausa = 600 + rng.nextInt(800);
                    evento = "descansando...";
                    modelo.registrarEvento(emoji + " " + nombre + " tomó una siesta de " + pausa + "ms");
                    Thread.sleep(pausa);
                    evento = "";

                } else if (chance < 14) {
                    
                    evento = "¡TURBO!";
                    modelo.registrarEvento(emoji + " " + nombre + " ¡TURBO BOOST!");
                    progreso = Math.min(100, progreso + 8);
                    Thread.sleep(100);
                    evento = "";

                } else if (chance < 18) {
                    evento = "tropezó";
                    modelo.registrarEvento(emoji + " " + nombre + " tropezó y perdió el ritmo");
                    Thread.sleep(400);
                    evento = "";
                }

                progreso = Math.min(100, progreso + 1);

                System.out.printf("[Thread: %-20s] %s %s: %d%%%n",
                        Thread.currentThread().getName(), emoji, nombre, progreso);

                if (progreso >= 100) {
                    modelo.registrarLlegada(this);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void detener() {
        corriendo = false;
    }

    public void reset() {
        progreso  = 0;
        lugar     = 0;
        evento    = "";
        corriendo = false;
    }

    public String  getNombre()        { return nombre;        }
    public String  getEmoji()         { return emoji;         }
    public Color   getColor()         { return color;         }
    public int     getVelocidadBase() { return velocidadBase; }
    public int     getProgreso()      { return progreso;      }
    public int     getLugar()         { return lugar;         }
    public String  getEvento()        { return evento;        }

    public void setLugar(int lugar)   { this.lugar = lugar;   }
}