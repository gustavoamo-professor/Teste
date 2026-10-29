package com.example.jsonex.jsonexemplo.view;

import com.example.jsonex.jsonexemplo.dao.UsuarioDAO;
import com.example.jsonex.jsonexemplo.model.Usuario;
import com.example.jsonex.jsonexemplo.seguranca.Sessao;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
public class TelaLogin extends JDialog {
private final JTextField campoLogin;
private final JPasswordField campoSenha;
private final TelaPrincipal telaPrincipal;
public TelaLogin(TelaPrincipal telaPrincipal) {
super(telaPrincipal, "Login do sistema", true);
this.telaPrincipal = telaPrincipal;
campoLogin = new JTextField();
campoSenha = new JPasswordField();
configurarJanela();
JPanel painelFormulario = criarPainelFormulario();
JPanel painelBotoes = criarPainelBotoes();
add(painelFormulario, BorderLayout.CENTER);
add(painelBotoes, BorderLayout.SOUTH);
getRootPane().setDefaultButton(
localizarBotaoEntrar(painelBotoes)
);
}
private void configurarJanela() {
setSize(420, 260);
setLayout(new BorderLayout(10, 10));
setLocationRelativeTo(telaPrincipal);
setResizable(false);
setDefaultCloseOperation(DISPOSE_ON_CLOSE);
}
private JPanel criarPainelFormulario() {
JPanel painelExterno = new JPanel(new BorderLayout(10, 10));
painelExterno.setBorder(
BorderFactory.createEmptyBorder(20, 30, 10, 30)
);
JLabel titulo = new JLabel(
"Autenticação de usuário",
JLabel.CENTER
);
titulo.setFont(new Font("Arial", Font.BOLD, 20));
JPanel formulario = new JPanel(
new GridLayout(2, 2, 10, 15)
);
formulario.add(new JLabel("Login:"));
formulario.add(campoLogin);
formulario.add(new JLabel("Senha:"));
formulario.add(campoSenha);
painelExterno.add(titulo, BorderLayout.NORTH);
painelExterno.add(formulario, BorderLayout.CENTER);
return painelExterno;
}
private JPanel criarPainelBotoes() {
JPanel painel = new JPanel(
new FlowLayout(FlowLayout.CENTER, 10, 10)
);
JButton botaoEntrar = new JButton("Entrar");
JButton botaoCancelar = new JButton("Cancelar");
botaoEntrar.setName("botaoEntrar");
botaoEntrar.addActionListener(evento -> realizarLogin());
botaoCancelar.addActionListener(evento -> dispose());
painel.add(botaoEntrar);
painel.add(botaoCancelar);
return painel;
}
private JButton localizarBotaoEntrar(JPanel painel) {
for (var componente : painel.getComponents()) {
if (componente instanceof JButton botao
&& "botaoEntrar".equals(botao.getName())) {
return botao;
}
}
return null;
}
private void realizarLogin() {
String login = campoLogin.getText().trim();
String senha = new String(campoSenha.getPassword());
if (login.isBlank() || senha.isBlank()) {
JOptionPane.showMessageDialog(
this,
"Informe o login e a senha.",
"Campos obrigatórios",
JOptionPane.WARNING_MESSAGE
);
return;
}
UsuarioDAO usuarioDAO = new UsuarioDAO();
try {
Usuario usuario = usuarioDAO.autenticar(login, senha);
if (usuario == null) {
JOptionPane.showMessageDialog(
this,
"Login ou senha incorretos.",
"Falha na autenticação",
JOptionPane.ERROR_MESSAGE
);
campoSenha.setText("");
campoSenha.requestFocus();
return;
}
Sessao.iniciar(usuario);
telaPrincipal.atualizarTela();
JOptionPane.showMessageDialog(
this,
"Bem-vindo, " + usuario.getNome() + "!",
"Login realizado",
JOptionPane.INFORMATION_MESSAGE
);
dispose();
} catch (SQLException e) {
JOptionPane.showMessageDialog(
this,
"Não foi possível conectar ao banco de dados.\n"
+ "Detalhes: " + e.getMessage(),
"Erro de conexão",
JOptionPane.ERROR_MESSAGE
);
}
}
}
