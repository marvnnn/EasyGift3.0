package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParUsuarioLista implements RegistroArvoreBMais<ParUsuarioLista> {
    private int idUsuario;  // primeira chave
    private int idLista;    // segunda chave
    private final short TAMANHO = 8;  // tamanho fixo em bytes

    public ParUsuarioLista() {
        this.idUsuario = -1;
        this.idLista = -1;
    }

    public ParUsuarioLista(int idUsuario, int idLista) {
        this.idUsuario = idUsuario;
        this.idLista = idLista;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idUsuario);
        dos.writeInt(this.idLista);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idUsuario = dis.readInt();
        this.idLista = dis.readInt();
    }

    @Override
    public int compareTo(ParUsuarioLista obj) {
        // Primeiro compara por idUsuario
        int comparacaoUsuario = Integer.compare(this.idUsuario, obj.idUsuario);
        if (comparacaoUsuario != 0) {
            return comparacaoUsuario;
        }
        // Se idUsuario for igual, compara por idLista
        return Integer.compare(this.idLista, obj.idLista);
    }

    @Override
    public ParUsuarioLista clone() {
        return new ParUsuarioLista(this.idUsuario, this.idLista);
    }

    @Override
    public String toString() {
        return "(" + idUsuario + "," + idLista + ")";
    }
}