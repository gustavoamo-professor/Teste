package com.example.jsonex.jsonexemplo;

import com.example.jsonex.jsonexemplo.view.TelaPrincipal;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
public class Main {
public static void main(String[] args) {
configurarAparencia();
SwingUtilities.invokeLater(() -> {
TelaPrincipal telaPrincipal = new TelaPrincipal();
telaPrincipal.setVisible(true);
});
}
private static void configurarAparencia() {
try {
UIManager.setLookAndFeel(
UIManager.getSystemLookAndFeelClassName()
);
} catch (Exception e) {
JOptionPane.showMessageDialog(
null,
"Não foi possível carregar a aparência do sistema.",
"Aviso",
JOptionPane.WARNING_MESSAGE
);
}
}
}

