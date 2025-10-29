package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParEmailID implements RegistroHashExtensivel<ParEmailID> {
    private String email;   // chave
    private int id;         // valor
    private final short TAMANHO = 256;  // tamanho fixo em bytes (para acomodar emails longos)

    public ParEmailID() {
        this.email = "";
        this.id = -1;
    }

    public ParEmailID(String email, int id) {
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        // Escreve o email com tamanho fixo
        String emailPadded = String.format("%-100s", this.email);
        dos.writeUTF(emailPadded);
        dos.writeInt(this.id);
        
        // Preenche o restante com zeros para atingir o tamanho fixo
        byte[] buffer = baos.toByteArray();
        byte[] result = new byte[TAMANHO];
        System.arraycopy(buffer, 0, result, 0, Math.min(buffer.length, TAMANHO));
        
        return result;
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.email = dis.readUTF().trim();
        this.id = dis.readInt();
    }

    @Override
    public String toString() {
        return "(" + email + "," + id + ")";
    }
}