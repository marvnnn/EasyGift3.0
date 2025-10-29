package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIDGTIN implements RegistroHashExtensivel<ParIDGTIN> {
    private String gtin;  // chave
    private int id;       // valor
    private final short TAMANHO = 17;  // 13 bytes GTIN + 4 bytes int

    public ParIDGTIN() {
        this.gtin = "";
        this.id = -1;
    }

    public ParIDGTIN(String gtin, int id) {
        this.gtin = gtin;
        this.id = id;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.gtin.hashCode();
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // GTIN-13: exatamente 13 bytes, preenchido com espaços se necessário
        String gtinPadded = String.format("%-13s", this.gtin);
        dos.writeBytes(gtinPadded);

        dos.writeInt(this.id); // 4 bytes para o ID
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        if (ba.length != TAMANHO) {
            throw new IOException("Tamanho de registro inválido: esperado " + TAMANHO + " bytes");
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] gtinBytes = new byte[13];
        dis.readFully(gtinBytes);
        this.gtin = new String(gtinBytes).trim();

        this.id = dis.readInt();
    }

    @Override
    public String toString() {
        return "(" + gtin + ", " + id + ")";
    }
}
