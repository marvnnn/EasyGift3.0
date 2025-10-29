package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCID implements RegistroHashExtensivel<ParCID> {
    private String codigo;  // chave
    private int id;         // valor
    private final short TAMANHO = 16;  // tamanho fixo em bytes (10 caracteres + 4 bytes para int)

    public ParCID() {
        this.codigo = "";
        this.id = -1;
    }

    public ParCID(String codigo, int id) {
        this.codigo = codigo;
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.codigo.hashCode();
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        // Escreve o código com tamanho fixo
        String codigoPadded = String.format("%-10s", this.codigo);
        dos.writeUTF(codigoPadded);
        dos.writeInt(this.id);
        
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.codigo = dis.readUTF().trim();
        this.id = dis.readInt();
    }

    @Override
    public String toString() {
        return "(" + codigo + "," + id + ")";
    }
}