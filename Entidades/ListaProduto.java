package Entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import aed3.*;

public class ListaProduto implements Registro {

    private int id;
    private int idLIsta;
    private int idProduto;
    private int quantidade;
    private String observacoes;

    // Construtor padrão
    public ListaProduto() {
        this.id = -1;
        this.idLIsta = -1;
        this.idProduto = -1;
        this.quantidade = 1; // padrão 1
        this.observacoes = "";
    }

    // Construtor completo
    public ListaProduto(int idLIsta, int idProduto, int quantidade, String observacoes) {
        this.id = -1;
        this.idLIsta = idLIsta;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdLIsta() {
        return idLIsta;
    }
    public void setIdLIsta(int idLIsta) {
        this.idLIsta = idLIsta;
    }

    public int getIdProduto() {
        return idProduto;
    }
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // Serialização para bytes
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(this.id);
        dos.writeInt(this.idLIsta);
        dos.writeInt(this.idProduto);
        dos.writeInt(this.quantidade);
        dos.writeUTF(this.observacoes);

        return baos.toByteArray();
    }

    // Desserialização a partir de bytes
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.id = dis.readInt();
        this.idLIsta = dis.readInt();
        this.idProduto = dis.readInt();
        this.quantidade = dis.readInt();
        this.observacoes = dis.readUTF();
    }
}
