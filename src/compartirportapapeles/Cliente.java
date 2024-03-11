/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package compartirportapapeles;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Cliente implements NativeKeyListener {
     
   public static  Socket socket;
     DataInputStream in;
     DataOutputStream out;
    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Error al registrar el hook nativo: " + ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new Cliente());
        Cliente cliente=new Cliente();
        cliente.iniciarCliente();
    }

    public void iniciarCliente() {
    
        try {
            socket = new Socket("192.168.0.167", 12345); // Reemplaza con la dirección IP del servidor
           // in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
           
            // Hilo para manejar la recepción de mensajes del servidor
           // Thread receivingThread = new Thread(() -> {
                try {
                    while (true) {
                        String mensajeRecibido = in.readUTF();
                        System.out.println("Servidor: " + mensajeRecibido);

                        if (mensajeRecibido != null) {
                            copiarAlPortapapeles(mensajeRecibido);
                           
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
           // });
          ///  receivingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarTextoCompartido(String texto) throws IOException {
         System.out.println("estado socket"+socket.isConnected());
      DataOutputStream out;
       out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(texto);
            System.out.println("texto enviado "+texto);
            
            
          
        
    }

  /*  public void ObtenerTextoPortapapeles() {
        try {
       
            CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
  //***Aquí agregamos el proceso a ejecutar.
           Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clipboard.getContents(null);
               if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String textoPortapapeles;
               try {
                   textoPortapapeles = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                   System.out.println("Texto en el portapapeles: " + textoPortapapeles);
             
                enviarTextoCompartido(textoPortapapeles);
               } catch (UnsupportedFlavorException ex) {
                   Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
               } catch (IOException ex) {
                   Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
               }
                
            } else {
                System.out.println("No se ha encontrado texto en el portapapeles.");
            }
});
            

         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    
    public void ObtenerTextoPortapapeles() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    //***Aquí agregamos el proceso a ejecutar.
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable transferable = clipboard.getContents(null);

                    if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String textoPortapapeles = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                        System.out.println("Texto en el portapapeles: " + textoPortapapeles);
                        enviarTextoCompartido(textoPortapapeles);
                    } else {
                        System.out.println("No se ha encontrado texto en el portapapeles.");
                        // return "";
                    }
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                    //  return "";
                }
            }
        }, 3000);

    }
    public void copiarAlPortapapeles(String text) {
        String textoACopiar = text;

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(textoACopiar);
        clipboard.setContents(selection, null);

        System.out.println("Texto copiado al portapapeles: " + textoACopiar);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // No necesitamos hacer nada aquí
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        
               System.out.println(e.getKeyCode() == NativeKeyEvent.VC_C);
                System.out.println((e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0);
        if (e.getKeyCode() == NativeKeyEvent.VC_C && (e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0) {
            // Llamar al método para obtener texto del portapapeles
            ObtenerTextoPortapapeles();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // No necesitamos hacer nada aquí
    }
}
