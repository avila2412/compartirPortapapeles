/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package compartirportapapeles;

import java.awt.AWTException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Pruebas
 */
public class CompartirPortapapeles implements NativeKeyListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws AWTException {
      try {
          GlobalScreen.registerNativeHook();
   
      
        } catch (NativeHookException ex) {
            System.err.println("Error al registrar el hook nativo: " + ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new CompartirPortapapeles());
           Servidor server=new Servidor();
       server.iniciarServidor();
    }
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // No necesitamos hacer nada aquí
    }
@Override
    public void nativeKeyPressed(NativeKeyEvent e) {
       
        
        if (e.getKeyCode() == NativeKeyEvent.VC_C && (e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0) {
            // Llamar al método para obtener texto del portapapeles
              Servidor server=new Servidor();
           server.ObtenerTextoPortapapeles();
            //  server.enviarTextoCompartido(texto);
        }
    }
@Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // No necesitamos hacer nada aquí
    }
}
