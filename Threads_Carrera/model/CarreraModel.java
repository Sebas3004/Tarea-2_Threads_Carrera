package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


public class CarreraModel {

    private final List<Animal> animales = new ArrayList<>();

    private final AtomicBoolean hayGanador   = new AtomicBoolean(false);
    private final AtomicInteger lugarActual  = new AtomicInteger(1);
    private volatile boolean    enCarrera    = false;
    private final List<String>  logEventos   = Collections.synchronizedList(new ArrayList<>());

    private Consumer<Animal>  onGanador;        
    private Consumer<Animal>  onLlegada;        
    private Consumer<String>  onEvento;       
    private Runnable          onCarreraFin;     

    public CarreraModel() {
        animales.add(new Animal("Tortuga", "🐢", new Color(80,  200, 120), 180));
        animales.add(new Animal("Conejo",  "🐇", new Color(255, 100, 130),  60));
        animales.add(new Animal("Perro",   "🐕", new Color(100, 160, 255),  90));
        animales.add(new Animal("Caballo", "🦄", new Color(200, 130, 255),  75));
        animales.add(new Animal("Caracol", "🐌", new Color(255, 170,  60), 220));

        for (Animal a : animales) a.setModelo(this);
    }

    public void iniciarCarrera() {
        hayGanador.set(false);
        lugarActual.set(1);
        enCarrera = true;
        logEventos.clear();

        registrarEvento("🏁 ¡Arrancaron todos!");

        for (Animal animal : animales) {
            animal.reset();
            Thread t = new Thread(animal, "Thread-" + animal.getNombre());
            t.setDaemon(true);
            t.start();
        }
    }

    public synchronized void registrarLlegada(Animal animal) {
        int lugar = lugarActual.getAndIncrement();
        animal.setLugar(lugar);

        if (lugar == 1 && hayGanador.compareAndSet(false, true)) {
            registrarEvento("🏆 ¡" + animal.getEmoji() + " " + animal.getNombre() + " GANÓ LA CARRERA!");
            if (onGanador != null) onGanador.accept(animal);
        } else {
            registrarEvento(animal.getEmoji() + " " + animal.getNombre() + " llegó en el puesto #" + lugar);
            if (onLlegada != null) onLlegada.accept(animal);
        }

        verificarFin();
    }

    private void verificarFin() {
        boolean todosFin = animales.stream().allMatch(a -> a.getProgreso() >= 100);
        if (todosFin && enCarrera) {
            enCarrera = false;
            if (onCarreraFin != null) onCarreraFin.run();
        }
    }

    public void registrarEvento(String msg) {
        logEventos.add(msg);
        if (onEvento != null) onEvento.accept(msg);
    }

    public void reset() {
        hayGanador.set(false);
        lugarActual.set(1);
        enCarrera = false;
        logEventos.clear();
        for (Animal a : animales) a.reset();
    }

    public List<Animal> getAnimales()  { return Collections.unmodifiableList(animales); }
    public boolean      isEnCarrera()  { return enCarrera; }

    public void setOnGanador(Consumer<Animal> cb)   { this.onGanador    = cb; }
    public void setOnLlegada(Consumer<Animal> cb)   { this.onLlegada    = cb; }
    public void setOnEvento(Consumer<String>  cb)   { this.onEvento     = cb; }
    public void setOnCarreraFin(Runnable      cb)   { this.onCarreraFin = cb; }
}