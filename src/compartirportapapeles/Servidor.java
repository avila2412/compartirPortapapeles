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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Servidor {
     public static Socket socket;
     DataInputStream in;
     DataOutputStream out;
    public void iniciarServidor() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Esperando una conexión entrante...");

            socket = serverSocket.accept();
            System.out.println("Conexión establecida.");
            in = new DataInputStream(socket.getInputStream());
           // out = new DataOutputStream(socket.getOutputStream());
            // Hilo para manejar la recepción de mensajes del cliente
         //   Thread receivingThread = new Thread(() -> {
                try {
                   
                    while (true) {
                        String mensajeRecibido = in.readUTF();
                       
                            System.out.println("Cliente: " + mensajeRecibido);
                            copiarAlportapapeles(mensajeRecibido);
                        
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
           // });
           // receivingThread.start();

            // Hilo para manejar el envío de mensajes al cliente
           /* Thread sendingThread = new Thread(() -> {
                try {
                    
                    while (true) {
                       
                        Thread.sleep(1000); // Esperar 1 segundo antes de enviar el siguiente mensaje
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            sendingThread.start();*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copiarAlportapapeles(String text) {
        String textoACopiar = text;

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(textoACopiar);
        clipboard.setContents(selection, null);

        System.out.println("Texto copiado al portapapeles: " + textoACopiar);
    }

    public void enviarTextoCompartido(String texto) throws IOException {
        System.out.println("Texto enviado:"+texto);
    
       out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(texto);
     //   out.println(texto);
    }

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

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciarServidor();
    }
}
