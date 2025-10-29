package Arquivos;

import Entidades.Usuario;
import aed3.Arquivo;
import aed3.HashExtensivel;
import aed3.ParEmailID;

public class ArquivoUsuario extends Arquivo<Usuario> {

    public HashExtensivel<ParEmailID> indiceEmail;

    public ArquivoUsuario() throws Exception {
        super("usuario", Usuario.class.getConstructor());

        // inicializa índice CPF → ID
        indiceEmail = new HashExtensivel<>(
            ParEmailID.class.getConstructor(),
            4,
            ".\\Dados\\usuario\\usuario_Email.d.db",
            ".\\Dados\\usuario\\usuario_Email.c.db"
        );
    }

    public int create(Usuario u) throws Exception {
        int id = super.create(u); // grava no arquivo principal e índice direto
        
        //indiceCPF.create(new ParCPFID(u.getCPF(), id)); // grava no índice CPF
        return id;
    }
}
