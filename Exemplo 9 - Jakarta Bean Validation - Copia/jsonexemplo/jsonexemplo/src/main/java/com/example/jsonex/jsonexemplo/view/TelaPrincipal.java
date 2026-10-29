package com.example.jsonex.jsonexemplo.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.example.jsonex.jsonexemplo.model.Usuario;
import com.example.jsonex.jsonexemplo.seguranca.Sessao;
public class TelaPrincipal extends JFrame {
private final JLabel labelUsuario;
private final JButton botaoLogin;
private final JButton botaoLogout;
private final JButton botaoConsultarDados;
private final JButton botaoInserirDados;
private final JButton botaoAtualizarDados;
private final JButton botaoExcluirDados;
public TelaPrincipal() {
labelUsuario = new JLabel("", JLabel.CENTER);
botaoLogin = new JButton("Realizar login");
botaoLogout = new JButton("Sair da conta");
botaoConsultarDados =
new JButton("Consultar dados");
botaoInserirDados =
new JButton("Inserir dados");
botaoAtualizarDados =
new JButton("Atualizar dados");
botaoExcluirDados =
new JButton("Excluir dados");
configurarJanela();
adicionarComponentes();
configurarEventos();
atualizarTela();
}
private void configurarJanela() {
setTitle("Sistema de Controle de Acesso");
setSize(650, 450);
setLocationRelativeTo(null);
setDefaultCloseOperation(EXIT_ON_CLOSE);
setLayout(new BorderLayout(15, 15));
}
private void adicionarComponentes() {
JPanel painelCabecalho = new JPanel(
new GridLayout(2, 1, 5, 5)
);
painelCabecalho.setBorder(
BorderFactory.createEmptyBorder(20, 20, 10, 20)
);
JLabel titulo = new JLabel(
"Sistema de Controle de Acesso",
JLabel.CENTER
);
titulo.setFont(new Font("Arial", Font.BOLD, 24));
labelUsuario.setFont(
new Font("Arial", Font.PLAIN, 15)
);
painelCabecalho.add(titulo);
painelCabecalho.add(labelUsuario);
JPanel painelOpcoes = new JPanel(
new GridLayout(4, 1, 10, 10)
);
painelOpcoes.setBorder(
BorderFactory.createEmptyBorder(10, 80, 10, 80)
);
painelOpcoes.add(botaoInserirDados);
painelOpcoes.add(botaoConsultarDados);
painelOpcoes.add(botaoAtualizarDados);
painelOpcoes.add(botaoExcluirDados);
JPanel painelAcesso = new JPanel();
painelAcesso.setBorder(
BorderFactory.createEmptyBorder(10, 10, 20, 10)
);
painelAcesso.add(botaoLogin);
painelAcesso.add(botaoLogout);
add(painelCabecalho, BorderLayout.NORTH);
add(painelOpcoes, BorderLayout.CENTER);
add(painelAcesso, BorderLayout.SOUTH);
}
private void configurarEventos() {
botaoLogin.addActionListener(evento -> abrirTelaLogin());
botaoLogout.addActionListener(evento -> realizarLogout());
botaoInserirDados.addActionListener(evento ->
JOptionPane.showMessageDialog(
this,
"Inserir dados",
"Inserção",
JOptionPane.INFORMATION_MESSAGE
)
);
botaoConsultarDados.addActionListener(evento ->
JOptionPane.showMessageDialog(
this,
"Dados consultados com sucesso.",
"Consulta",
JOptionPane.INFORMATION_MESSAGE
)
);
botaoAtualizarDados.addActionListener(evento ->
JOptionPane.showMessageDialog(
this,
"Área de atualização de dados.",
"Atualização",
JOptionPane.INFORMATION_MESSAGE
)
);
botaoExcluirDados.addActionListener(evento ->
JOptionPane.showMessageDialog(
this,
"Área de exclusão de dados.",
"Exclusão",
JOptionPane.INFORMATION_MESSAGE
)
);
}
private void abrirTelaLogin() {
TelaLogin telaLogin = new TelaLogin(this);
telaLogin.setVisible(true);
}
private void realizarLogout() {
int resposta = JOptionPane.showConfirmDialog(
this,
"Deseja realmente sair da conta?",
"Confirmar logout",
JOptionPane.YES_NO_OPTION
);
if (resposta == JOptionPane.YES_OPTION) {
Sessao.encerrar();
atualizarTela();
JOptionPane.showMessageDialog(
this,
"Logout realizado com sucesso.",
"Logout",
JOptionPane.INFORMATION_MESSAGE
);
}
}
public void atualizarTela() {
boolean usuarioEstaLogado = Sessao.estaLogado();
botaoLogin.setVisible(!usuarioEstaLogado);
botaoLogout.setVisible(usuarioEstaLogado);
/*
* A opção pública fica sempre disponível.
*/
botaoInserirDados.setEnabled(true);
/*
* Por padrão, as opções internas ficam bloqueadas.
*/
botaoConsultarDados.setEnabled(false);
botaoAtualizarDados.setEnabled(false);
botaoExcluirDados.setEnabled(false);
if (!usuarioEstaLogado) {
labelUsuario.setText(
"Usuário não autenticado. Acesso limitado."
);
return;
}
Usuario usuario = Sessao.getUsuarioLogado();
labelUsuario.setText(
"Usuário: " + usuario.getNome()
+ " | Perfil: " + usuario.getPerfil()
);
liberarOpcoesPorPerfil(usuario);
}
private void liberarOpcoesPorPerfil(Usuario usuario) {
/*
* Todos os usuários autenticados podem consultar
* os dados internos.
*/
if (usuario.isAdministrador() || usuario.isOperador()) {
botaoInserirDados.setEnabled(true);
}
botaoConsultarDados.setEnabled(true);
/*
* Gerentes e administradores podem acessar relatórios.
*/

botaoAtualizarDados.setEnabled(true);


if (usuario.isAdministrador()) {
botaoExcluirDados.setEnabled(true);
}
}
}
