/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dao.DAOAluguel;
import dao.DAOCategoria;
import dao.DAOCliente;
import dao.DAOProduto;
import dao.GenericDAO;
import domain.Aluguel;
import domain.Categoria;
import domain.Cliente;
import domain.Produto;
import domain.state.aluguel.EmAberto;
import domain.state.aluguel.EstadoAluguel;
import domain.state.aluguel.Fechado;
import domain.state.aluguel.NaoRetirado;
import domain.state.produto.DanoPermanente;
import domain.state.produto.EmAluguel;
import domain.state.produto.EmLoja;
import domain.state.produto.EmManutencao;
import domain.state.produto.EstadoProduto;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gudeck
 */
public class ControleDominio {

    private static final ControleDominio UNIQUEINSTANCE = new ControleDominio();
    private final GenericDAO genericDao;

    private ControleDominio() {

        dao.ConexaoHibernate.getSESSIONFACTORY();
        genericDao = new GenericDAO();

        genericDao.create_update(EmAberto.getInstance());
        genericDao.create_update(Fechado.getInstance());
        genericDao.create_update(NaoRetirado.getInstance());

        genericDao.create_update(DanoPermanente.getInstance());
        genericDao.create_update(EmAluguel.getInstance());
        genericDao.create_update(EmLoja.getInstance());
        genericDao.create_update(EmManutencao.getInstance());
    }

    public static ControleDominio getInstance() {
        return UNIQUEINSTANCE;
    }

    public GenericDAO getGenericDao() {
        return genericDao;
    }

    public void clienteCreate(String nome, String endereco, String email, String cpf, String dataNascimento, String telefone, char sexo) throws ParseException {

        Cliente cliente = new Cliente();

        cpf = cpf.replace(".", "").replace("-", "");
        telefone = telefone.replace("(", "").replace(")", "").replace(" ", "").replace("-", "");

        cliente.setNome(nome);
        cliente.setEndereco(endereco);
        cliente.setEmail(email);
        cliente.setCpf(cpf);
        cliente.setDataNascimento(MetodosUteis.stringTOjavaDate(dataNascimento));
        cliente.setTelefone(telefone);
        cliente.setSexo(sexo);

        genericDao.create_update(cliente);
    }

    public List<Cliente> clienteReadAll() {
        return genericDao.read(Cliente.class);
    }

    public void clienteUpdate(int codigo, String nome, String endereco, String email, String cpf, String dataNascimento, String telefone, char sexo) throws ParseException {

        Cliente cliente = new Cliente();

        cpf = cpf.replace(".", "").replace("-", "");
        telefone = telefone.replace("(", "").replace(")", "").replace(" ", "").replace("-", "");

        cliente.setCodCliente(codigo);
        cliente.setNome(nome);
        cliente.setEndereco(endereco);
        cliente.setEmail(email);
        cliente.setCpf(cpf);
        cliente.setDataNascimento(MetodosUteis.stringTOjavaDate(dataNascimento));
        cliente.setTelefone(telefone);
        cliente.setSexo(sexo);

        genericDao.create_update(cliente);
    }

    public void clienteDelete(Cliente cliente) {

        genericDao.delete(cliente);
    }

    public List<Cliente> clienteReadNome(String nome) {
        return DAOCliente.getInstance().readNome(nome);
    }

    public void categoriaCreate(String nome, String descricao) {

        Categoria categoria = new Categoria();

        categoria.setNome(nome);
        categoria.setDescricao(descricao);

        genericDao.create_update(categoria);
    }

    public List<Categoria> categoriaReadAll() {
        return genericDao.read(Categoria.class);
    }

    public void categoriaUpdate(int codigo, String nome, String descricao) {

        Categoria categoria = new Categoria();

        categoria.setCodCategoria(codigo);
        categoria.setNome(nome);
        categoria.setDescricao(descricao);

        genericDao.create_update(categoria);
    }

    public void categoriaDelete(Categoria categoria) {
        genericDao.delete(categoria);
    }

    public List<Categoria> categoriaReadNome(String nome) {
        return DAOCategoria.getInstance().readNome(nome);
    }

    public void produtoCreate(String nome, Double diaria, Double danoPermanente, Categoria categoria, String tamanho, String descricao) {

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setValorDiaria(diaria);
        produto.setValorDanoPermanente(danoPermanente);
        produto.setCategoria(categoria);
        produto.setTamanho(tamanho);
        produto.setDescricao(descricao);
        produto.setEstado(EmLoja.getInstance());

        genericDao.create_update(produto);
    }

    public List<Produto> produtoReadAll() {
        return genericDao.read(Produto.class);
    }

    public void produtoUpdate(int codProduto, String nome, Double diaria, Double danoPermanente, Categoria categoria, String tamanho, String descricao, EstadoProduto estado) {

        Produto produto = new Produto();
        produto.setCodProduto(codProduto);
        produto.setNome(nome);
        produto.setValorDiaria(diaria);
        produto.setValorDanoPermanente(danoPermanente);
        produto.setCategoria(categoria);
        produto.setTamanho(tamanho);
        produto.setDescricao(descricao);
        produto.setEstado(estado);

        genericDao.create_update(produto);
    }

    public void produtoUpdate(Produto produto) {
        genericDao.create_update(produto);
    }

    public void produtoDelete(Produto produto) {
        genericDao.delete(produto);
    }

    public List<Produto> produtoReadEstado(EstadoProduto estado) {
        return DAOProduto.getInstance().readEstado(estado);
    }

    public List<Produto> produtoReadTamanhoCategoria(String tamanho, Categoria categoria) {
        return DAOProduto.getInstance().readTamanhoCategoria(tamanho, categoria);
    }

    public List<Produto> produtoReadCategoria(Categoria categoria) {
        return DAOProduto.getInstance().readCategoria(categoria);
    }

    public void aluguelCreate(List<Produto> listaProdutos, Cliente cliente, String dataRetirada, String dataDevolucao, String valorTotal) throws ParseException {

        Date data = new Date();
        Aluguel aluguel = new Aluguel();

        aluguel.setCliente(cliente);
        aluguel.setDataDevolucao(MetodosUteis.stringTOjavaDate(dataDevolucao));
        aluguel.setDataReserva(data);
        aluguel.setDataRetirada(MetodosUteis.stringTOjavaDate(dataRetirada));
        aluguel.setValorPago(Double.valueOf(valorTotal) / 2);
        aluguel.setValorTotal(Double.valueOf(valorTotal));
        aluguel.setEstado(NaoRetirado.getInstance());

        listaProdutos.forEach((p) -> {
            p.setEstado(p.getEstado().setEmAluguel());
            genericDao.create_update(p);
        });

        aluguel.setProdutosAlugados(listaProdutos);

        genericDao.create_update(aluguel);
    }

    public void aluguelUpdate(Aluguel aluguel) {

        aluguel.getProdutosAlugados().forEach((pa) -> {
            genericDao.create_update(pa);
        });

        genericDao.create_update(aluguel);
    }

    public List<Aluguel> aluguelReadIndireto(Cliente cliente, EstadoAluguel estado) {
        return DAOAluguel.getInstance().readEstadoIndireto(cliente, estado);
    }

    public Aluguel aluguelReadDireto(Cliente cliente, EstadoAluguel estado) {
        return DAOAluguel.getInstance().readEstadoDireto(cliente, estado);
    }

}
