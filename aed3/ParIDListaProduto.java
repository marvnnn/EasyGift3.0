package aed3;

import java.io.*;

public class ParIDListaProduto implements RegistroHashExtensivel<ParIDListaProduto> {
    private int idLista;
    private int idProduto;
    private final short TAMANHO = 8; // 4 bytes + 4 bytes

    // 🔹 Construtor padrão (necessário para HashExtensivel)
    public ParIDListaProduto() {
        this.idLista = -1;
        this.idProduto = -1;
    }

    // 🔹 Construtor com parâmetros
    public ParIDListaProduto(int idLista, int idProduto) {
        this.idLista = idLista;
        this.idProduto = idProduto;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    @Override
    public int hashCode() {
        // Combina os dois IDs para gerar o hash
        return Integer.hashCode(idLista * 31 + idProduto);
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idLista);
        dos.writeInt(idProduto);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idLista = dis.readInt();
        this.idProduto = dis.readInt();
    }

    @Override
    public String toString() {
        return "(" + idLista + ", " + idProduto + ")";
    }
}
