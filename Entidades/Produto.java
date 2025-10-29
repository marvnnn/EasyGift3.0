package Entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import aed3.*;

public class Produto implements Registro {
    private int id;
    private String gtin13;
    private String nome;
    private String descricao;
    private boolean ativo; // ✅ novo atributo

    public Produto() {
        this.id = -1;
        this.gtin13 = "0000000000000";
        this.nome = "";
        this.descricao = "";
        this.ativo = true; // produto inicia como ativo
    }

    public Produto(String gtin13, String nome, String descricao) {
        this.id = -1;
        this.gtin13 = gtin13;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = true; // produto inicia como ativo
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGtin13() {
        return gtin13;
    }

    public void setGtin13(String gtin13) {
        this.gtin13 = gtin13;
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

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(this.id);
        dos.writeUTF(this.gtin13);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeBoolean(this.ativo); // ✅ salvar estado ativo/inativo

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.id = dis.readInt();
        this.gtin13 = dis.readUTF();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.ativo = dis.readBoolean(); // ✅ ler estado ativo/inativo
    }
}
