package Entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import aed3.*;

public class Lista implements Registro {
    private int id;
    private String nome;
    private String descricao;
    private LocalDate dataCriacao;
    private LocalDate dataLimite;
    private String codigoCompartilhavel;
    private String nomeAutor;
    private int idUsuario;

    public Lista() {
        this.id = -1;
        this.nome = "";
        this.descricao = "";
        this.dataCriacao = LocalDate.now();
        this.dataLimite = null;
        this.nomeAutor = "";
        this.codigoCompartilhavel = "";
        this.idUsuario = -1;
    }

    public Lista(String nome, String descricao, LocalDate dataLimite, String nomeAutor, int idUsuario) {
        this.id = -1;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = LocalDate.now();
        this.dataLimite = dataLimite;
        this.codigoCompartilhavel = gerarCodigoCompartilhavel();
        this.nomeAutor = nomeAutor;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getNomeAutor() {
        return this.nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getCodigoCompartilhavel() {
        return codigoCompartilhavel;
    }

    public void setCodigoCompartilhavel(String codigoCompartilhavel) {
        this.codigoCompartilhavel = codigoCompartilhavel;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    private String gerarCodigoCompartilhavel() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = (int) (Math.random() * caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeUTF(this.dataCriacao.format(DateTimeFormatter.ISO_LOCAL_DATE));
        if (this.dataLimite != null) {
            dos.writeBoolean(true);
            dos.writeUTF(this.dataLimite.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            dos.writeBoolean(false);
        }
        dos.writeUTF(this.codigoCompartilhavel);
        dos.writeUTF(this.nomeAutor);
        dos.writeInt(this.idUsuario);
        
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.dataCriacao = LocalDate.parse(dis.readUTF(), DateTimeFormatter.ISO_LOCAL_DATE);
        if (dis.readBoolean()) {
            this.dataLimite = LocalDate.parse(dis.readUTF(), DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            this.dataLimite = null;
        }
        this.codigoCompartilhavel = dis.readUTF();
        this.nomeAutor = dis.readUTF();
        this.idUsuario = dis.readInt();
    }

    @Override
    public String toString() {
        return "Lista{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataLimite=" + dataLimite +
                ", codigoCompartilhavel='" + codigoCompartilhavel + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
}